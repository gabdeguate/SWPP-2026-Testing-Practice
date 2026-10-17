# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `fun setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: Bind the task's D-day or completion label (from `dueDateLabel`) to the task-row TextView, using the device's current local date. When the label is empty, clear the text and hide the view (`View.GONE`), also when a recycled row previously showed a label.

**Pre-conditions**
- Contract source: the `app:dueDateLabel` binding adapter and `task_item.xml` on branch `origin/feature/due-date` (commit 7e091a7). The `main` checkout does not contain this function. Clearing the text and using `View.GONE` for an empty label is the intended UI contract for a missing label and for recycled rows.
- `textView` is a valid Android `TextView`. Call the binding on the UI thread. `task` may be `null`.
- Each Tip is an exact reference fixture with referenceToday=2026-10-14. The binding still reads `LocalDate.now()`: for execution, read runtimeToday once, compute shiftDays as the whole-day distance from referenceToday to runtimeToday, and add shiftDays to each non-null reference due date. This shifts both dates equally, so the expected label stays the literal string in the Tip. The unit spec checks the calendar boundaries at their exact dates; the binding spec checks that those fixtures are displayed correctly after shifting.
- All `Task` values below use `title="Read"`, `description="Chapter 1"`, `id="task-1"`. Only `isCompleted` and `dueDate` change.

**Post-conditions**
- Success cases
  - A null task clears the label and hides the view. `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="stale" and visibility=View.VISIBLE, and task=null / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "" and textView.visibility is View.GONE
  - An active task without a due date clears the label and hides the view. `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=null) / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "" and textView.visibility is View.GONE
  - A completed task without a due date shows "CLEAR". `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=true, dueDate=null) / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "CLEAR" and textView.visibility is View.VISIBLE
  - A completed overdue task shows "CLEAR", not its overdue count. `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=true, dueDate=2026-10-13) / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "CLEAR" and textView.visibility is View.VISIBLE
  - An active task due today shows "D-Day". `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=2026-10-14) / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-Day" and textView.visibility is View.VISIBLE
  - An active future task shows its countdown. `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=2026-10-24) / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-10" and textView.visibility is View.VISIBLE
  - An active overdue task shows its overdue count. `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=2026-10-04) / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D+10" and textView.visibility is View.VISIBLE
- Error cases
  - None. Every input that satisfies the pre-conditions is valid.
- Edge cases
  - Today boundary, one day before: a task due yesterday shows "D+1". `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=2026-10-13) / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D+1" and textView.visibility is View.VISIBLE
  - Today boundary, at: a task due today shows "D-Day". `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=2026-10-14) / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-Day" and textView.visibility is View.VISIBLE
  - Today boundary, one day after: a task due tomorrow shows "D-1". `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=2026-10-15) / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-1" and textView.visibility is View.VISIBLE
  - Reference one-month boundary (future), one day before: the row shows the total day count. `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=2026-11-13) / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-30" and textView.visibility is View.VISIBLE
  - Reference one-month boundary (future), at: the 31-day reference gap shows "D-31", not "D-Day". `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=2026-11-14) / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-31" and textView.visibility is View.VISIBLE
  - Reference one-month boundary (future), one day after: the row shows the total day count, not "D-1". `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=2026-11-15) / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-32" and textView.visibility is View.VISIBLE
  - Reference one-month boundary (overdue), one day before: the row shows the total overdue day count. `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=2026-09-13) / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D+31" and textView.visibility is View.VISIBLE
  - Reference one-month boundary (overdue), at: the 30-day reference gap shows "D+30", not "D-Day". `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=2026-09-14) / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D+30" and textView.visibility is View.VISIBLE
  - Reference one-month boundary (overdue), one day after: the row shows the total overdue day count. `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="stale" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=2026-09-15) / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D+29" and textView.visibility is View.VISIBLE
  - Recycled row, label to null task: the old label is cleared and the view is hidden. `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="D-1" and visibility=View.VISIBLE, and task=null / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "" and textView.visibility is View.GONE
  - Recycled row, label to active undated task: the old label is cleared and the view is hidden. `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="D-1" and visibility=View.VISIBLE, and task=Task(isCompleted=false, dueDate=null) / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "" and textView.visibility is View.GONE
  - Recycled row, label to a different label: the new label replaces the old one. `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="D-1" and visibility=View.VISIBLE, and task=Task(isCompleted=true, dueDate=null) / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "CLEAR" and textView.visibility is View.VISIBLE
  - Recycled row, hidden to dated task: the label is set and the view becomes visible. `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="" and visibility=View.GONE, and task=Task(isCompleted=false, dueDate=2026-10-15) / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-1" and textView.visibility is View.VISIBLE
  - Recycled row, hidden to completed undated task: the view shows "CLEAR" and becomes visible. `[Integration]`
    - Tip: Given referenceToday=2026-10-14, a TextView with text="" and visibility=View.GONE, and task=Task(isCompleted=true, dueDate=null) / When the dated fixture is shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "CLEAR" and textView.visibility is View.VISIBLE
  - Visible, hidden, visible rebinding on the same view keeps no stale text or visibility. `[Integration]`
    - Tip: Given referenceToday=2026-10-14 and a TextView with text="" and visibility=View.GONE / When the dated fixtures are shifted to runtimeToday=LocalDate.now() as described above and setDueDateLabel is called on the UI thread on the same TextView with Task(isCompleted=false, dueDate=2026-10-15), then task=null, then Task(isCompleted=false, dueDate=2026-10-13) / Then text and visibility after each call are "D-1"/View.VISIBLE, then ""/View.GONE, then "D+1"/View.VISIBLE

**Undefined behavior** (optional)
- A call from outside the UI thread.
- A test that runs across local midnight, so `LocalDate.now()` in the test and in the binding return different dates.
