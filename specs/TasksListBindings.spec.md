# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `fun setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: Bind the task's D-day or completion label (from `dueDateLabel`) to the task-row TextView, using the device's current local date. When the label is empty, clear the text and hide the view (`View.GONE`), also when a recycled row previously showed a label.

**Pre-conditions**
- Contract source: the `app:dueDateLabel` binding adapter and `task_item.xml` on branch `origin/feature/due-date` (commit 7e091a7). The `main` checkout does not contain this function. Clearing the text and using `View.GONE` for an empty label is the intended UI contract for a missing label and for recycled rows.
- `textView` is a valid Android `TextView`. Call the binding on the UI thread. `task` may be `null`.
- The function reads the device local date with `LocalDate.now()`. Tests cannot fix this date, so each Tip reads `today = LocalDate.now()` once in Given and builds every due date from `today`. Expected strings are exact for that `today`.
- All `Task` values below use `title="Read"`, `description="Chapter 1"`, `id="task-1"`. Only `isCompleted` and `dueDate` change.

**Post-conditions**
- Success cases
  - A null task clears the label and hides the view. `[Integration]`
    - Tip: Given today=LocalDate.now(), a TextView with text="stale" and visibility=View.VISIBLE, and task=null / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "" and textView.visibility is View.GONE
  - An active task without a due date clears the label and hides the view. `[Integration]`
    - Tip: Given today=LocalDate.now(), a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=null) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "" and textView.visibility is View.GONE
  - A completed task without a due date shows "CLEAR". `[Integration]`
    - Tip: Given today=LocalDate.now(), a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=true, dueDate=null) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "CLEAR" and textView.visibility is View.VISIBLE
  - A completed overdue task shows "CLEAR", not its overdue count. `[Integration]`
    - Tip: Given today=LocalDate.now(), a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=true, dueDate=today.minusDays(1)) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "CLEAR" and textView.visibility is View.VISIBLE
  - An active task due today shows "D-Day". `[Integration]`
    - Tip: Given today=LocalDate.now(), a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=today) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-Day" and textView.visibility is View.VISIBLE
  - An active future task shows its countdown. `[Integration]`
    - Tip: Given today=LocalDate.now(), a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=today.plusDays(10)) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-10" and textView.visibility is View.VISIBLE
  - An active overdue task shows its overdue count. `[Integration]`
    - Tip: Given today=LocalDate.now(), a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=today.minusDays(10)) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D+10" and textView.visibility is View.VISIBLE
- Error cases
  - None. Every input that satisfies the pre-conditions is valid.
- Edge cases
  - Today boundary, one day before: a task due yesterday shows "D+1". `[Integration]`
    - Tip: Given today=LocalDate.now(), a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=today.minusDays(1)) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D+1" and textView.visibility is View.VISIBLE
  - Today boundary, at: a task due today shows "D-Day". `[Integration]`
    - Tip: Given today=LocalDate.now(), a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=today) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-Day" and textView.visibility is View.VISIBLE
  - Today boundary, one day after: a task due tomorrow shows "D-1". `[Integration]`
    - Tip: Given today=LocalDate.now(), a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=today.plusDays(1)) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-1" and textView.visibility is View.VISIBLE
  - One-month boundary (future), one day before: the row shows the total day count. `[Integration]`
    - Tip: Given today=LocalDate.now(), due=today.plusMonths(1).minusDays(1), n=ChronoUnit.DAYS.between(today, due), a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=due) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-$n" and textView.visibility is View.VISIBLE
  - One-month boundary (future), at: exactly one calendar month ahead shows the total day count, not "D-Day". `[Integration]`
    - Tip: Given today=LocalDate.now(), due=today.plusMonths(1), n=ChronoUnit.DAYS.between(today, due), a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=due) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-$n" and textView.visibility is View.VISIBLE
  - One-month boundary (future), one day after: the row shows the total day count, not "D-1". `[Integration]`
    - Tip: Given today=LocalDate.now(), due=today.plusMonths(1).plusDays(1), n=ChronoUnit.DAYS.between(today, due), a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=due) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-$n" and textView.visibility is View.VISIBLE
  - One-month boundary (overdue), one day before: the row shows the total overdue day count. `[Integration]`
    - Tip: Given today=LocalDate.now(), due=today.minusMonths(1).minusDays(1), n=ChronoUnit.DAYS.between(due, today), a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=due) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D+$n" and textView.visibility is View.VISIBLE
  - One-month boundary (overdue), at: exactly one calendar month overdue shows the total day count, not "D-Day". `[Integration]`
    - Tip: Given today=LocalDate.now(), due=today.minusMonths(1), n=ChronoUnit.DAYS.between(due, today), a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=due) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D+$n" and textView.visibility is View.VISIBLE
  - One-month boundary (overdue), one day after: the row shows the total overdue day count. `[Integration]`
    - Tip: Given today=LocalDate.now(), due=today.minusMonths(1).plusDays(1), n=ChronoUnit.DAYS.between(due, today), a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=due) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D+$n" and textView.visibility is View.VISIBLE
  - Recycled row, label to null task: the old label is cleared and the view is hidden. `[Integration]`
    - Tip: Given today=LocalDate.now(), a TextView with text="D-1" and visibility=View.VISIBLE, and task=null / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "" and textView.visibility is View.GONE
  - Recycled row, label to active undated task: the old label is cleared and the view is hidden. `[Integration]`
    - Tip: Given today=LocalDate.now(), a TextView with text="D-1" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=null) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "" and textView.visibility is View.GONE
  - Recycled row, label to a different label: the new label replaces the old one. `[Integration]`
    - Tip: Given today=LocalDate.now(), a TextView with text="D-1" and visibility=View.VISIBLE, and task=Task(isCompleted=true, dueDate=null) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "CLEAR" and textView.visibility is View.VISIBLE
  - Recycled row, hidden to dated task: the label is set and the view becomes visible. `[Integration]`
    - Tip: Given today=LocalDate.now(), a TextView with text="" and visibility=View.GONE, and task=Task(isCompleted=false, dueDate=today.plusDays(1)) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-1" and textView.visibility is View.VISIBLE
  - Recycled row, hidden to completed undated task: the view shows "CLEAR" and becomes visible. `[Integration]`
    - Tip: Given today=LocalDate.now(), a TextView with text="" and visibility=View.GONE, and task=Task(isCompleted=true, dueDate=null) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "CLEAR" and textView.visibility is View.VISIBLE
  - Visible, hidden, visible rebinding on the same view keeps no stale text or visibility. `[Integration]`
    - Tip: Given today=LocalDate.now() and a TextView with text="" and visibility=View.GONE / When setDueDateLabel is called on the UI thread on the same TextView three times, with Task(isCompleted=false, dueDate=today.plusDays(1)), then task=null, then Task(isCompleted=false, dueDate=today.minusDays(1)) / Then text and visibility after each call are "D-1"/View.VISIBLE, then ""/View.GONE, then "D+1"/View.VISIBLE

**Undefined behavior** (optional)
- A call from outside the UI thread.
- A test that runs across local midnight, so `LocalDate.now()` in the test and in the binding return different dates.
