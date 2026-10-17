# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `fun setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: Bind the current task's D-day or completion label to a task-row TextView using the device local date. An empty label must clear the text and hide the view, including when task rows are reused.

**Pre-conditions**
- Contract context: the app:dueDateLabel task-row binding on the locally available origin/feature/due-date branch (commit 7e091a7); the current main checkout does not contain this function. Clearing text and using View.GONE for empty labels is the intended UI contract for absence and recycled rows.
- textView is a valid Android TextView; invoke the binding on the UI thread. task may be null.
- Use the device local calendar date at the call. Integration examples fix the date to 2026-10-14 in Asia/Seoul throughout each call; the date must not change during the scenario.

**Post-conditions**
- Success cases
  - An absent task clears the label and removes its space from the row. `[Integration]`
    - Tip: Given the device local date is fixed at 2026-10-14 (Asia/Seoul), a TextView has text="stale", visibility=View.VISIBLE, and task=null / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "" and textView.visibility is View.GONE
  - An active task without a due date clears the label and removes its space from the row. `[Integration]`
    - Tip: Given the device local date is fixed at 2026-10-14 (Asia/Seoul), a TextView has text="stale", visibility=View.VISIBLE, and task=Task(title="Read", description="Chapter 1", isCompleted=false, id="task-1", dueDate=null) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "" and textView.visibility is View.GONE
  - A completed task without a due date displays "CLEAR". `[Integration]`
    - Tip: Given the device local date is fixed at 2026-10-14 (Asia/Seoul), a TextView has text="stale", visibility=View.VISIBLE, and task=Task(title="Read", description="Chapter 1", isCompleted=true, id="task-1", dueDate=null) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "CLEAR" and textView.visibility is View.VISIBLE
  - A completed task displays "CLEAR" even when overdue. `[Integration]`
    - Tip: Given the device local date is fixed at 2026-10-14 (Asia/Seoul), a TextView has text="stale", visibility=View.VISIBLE, and task=Task(title="Read", description="Chapter 1", isCompleted=true, id="task-1", dueDate=2026-10-13) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "CLEAR" and textView.visibility is View.VISIBLE
  - A completed task displays "CLEAR" even when due today. `[Integration]`
    - Tip: Given the device local date is fixed at 2026-10-14 (Asia/Seoul), a TextView has text="stale", visibility=View.VISIBLE, and task=Task(title="Read", description="Chapter 1", isCompleted=true, id="task-1", dueDate=2026-10-14) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "CLEAR" and textView.visibility is View.VISIBLE
  - A completed task displays "CLEAR" even when due in the future. `[Integration]`
    - Tip: Given the device local date is fixed at 2026-10-14 (Asia/Seoul), a TextView has text="stale", visibility=View.VISIBLE, and task=Task(title="Read", description="Chapter 1", isCompleted=true, id="task-1", dueDate=2026-10-15) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "CLEAR" and textView.visibility is View.VISIBLE
  - An active task due today displays "D-Day". `[Integration]`
    - Tip: Given the device local date is fixed at 2026-10-14 (Asia/Seoul), a TextView has text="stale", visibility=View.VISIBLE, and task=Task(title="Read", description="Chapter 1", isCompleted=false, id="task-1", dueDate=2026-10-14) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-Day" and textView.visibility is View.VISIBLE
  - An active future task displays its total calendar-day countdown. `[Integration]`
    - Tip: Given the device local date is fixed at 2026-10-14 (Asia/Seoul), a TextView has text="stale", visibility=View.VISIBLE, and task=Task(title="Read", description="Chapter 1", isCompleted=false, id="task-1", dueDate=2026-11-14) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-31" and textView.visibility is View.VISIBLE
  - An active overdue task displays its total calendar-day overdue count. `[Integration]`
    - Tip: Given the device local date is fixed at 2026-10-14 (Asia/Seoul), a TextView has text="stale", visibility=View.VISIBLE, and task=Task(title="Read", description="Chapter 1", isCompleted=false, id="task-1", dueDate=2026-09-14) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D+30" and textView.visibility is View.VISIBLE
- Error cases
  - None. All inputs satisfying the pre-conditions are valid.
- Edge cases
  - The displayed date boundary covers yesterday, today, and tomorrow. `[Integration]`
    - Tip: Given the device local date is fixed at 2026-10-14 (Asia/Seoul), a TextView has text="stale", visibility=View.VISIBLE, and task=Task(title="Read", description="Chapter 1", isCompleted=false, id="task-1", dueDate=2026-10-13) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D+1" and textView.visibility is View.VISIBLE
  - The displayed date boundary covers yesterday, today, and tomorrow. `[Integration]`
    - Tip: Given the device local date is fixed at 2026-10-14 (Asia/Seoul), a TextView has text="stale", visibility=View.VISIBLE, and task=Task(title="Read", description="Chapter 1", isCompleted=false, id="task-1", dueDate=2026-10-14) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-Day" and textView.visibility is View.VISIBLE
  - The displayed date boundary covers yesterday, today, and tomorrow. `[Integration]`
    - Tip: Given the device local date is fixed at 2026-10-14 (Asia/Seoul), a TextView has text="stale", visibility=View.VISIBLE, and task=Task(title="Read", description="Chapter 1", isCompleted=false, id="task-1", dueDate=2026-10-15) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-1" and textView.visibility is View.VISIBLE
  - Reusing a view with an existing label must clear and hide it when the new binding has no label. `[Integration]`
    - Tip: Given the device local date is fixed at 2026-10-14 (Asia/Seoul), a TextView has text="D-1", visibility=View.VISIBLE, and task=null / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "" and textView.visibility is View.GONE
  - Reusing a view with an existing label must clear and hide it when the new binding has no label. `[Integration]`
    - Tip: Given the device local date is fixed at 2026-10-14 (Asia/Seoul), a TextView has text="D-1", visibility=View.VISIBLE, and task=Task(title="Read", description="Chapter 1", isCompleted=false, id="task-1", dueDate=null) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "" and textView.visibility is View.GONE
  - Reusing a hidden view for a dated task replaces the empty label and makes it visible. `[Integration]`
    - Tip: Given the device local date is fixed at 2026-10-14 (Asia/Seoul), a TextView has text="", visibility=View.GONE, and task=Task(title="Read", description="Chapter 1", isCompleted=false, id="task-1", dueDate=2026-10-15) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "D-1" and textView.visibility is View.VISIBLE
  - Reusing a hidden view for a completed undated task displays the completion label. `[Integration]`
    - Tip: Given the device local date is fixed at 2026-10-14 (Asia/Seoul), a TextView has text="", visibility=View.GONE, and task=Task(title="Read", description="Chapter 1", isCompleted=true, id="task-1", dueDate=null) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "CLEAR" and textView.visibility is View.VISIBLE
  - The discrete completion boundary for an undated task covers both false and true. `[Integration]`
    - Tip: Given the device local date is fixed at 2026-10-14 (Asia/Seoul), a TextView has text="stale", visibility=View.VISIBLE, and task=Task(title="Read", description="Chapter 1", isCompleted=false, id="task-1", dueDate=null) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "" and textView.visibility is View.GONE
  - The discrete completion boundary for an undated task covers both false and true. `[Integration]`
    - Tip: Given the device local date is fixed at 2026-10-14 (Asia/Seoul), a TextView has text="stale", visibility=View.VISIBLE, and task=Task(title="Read", description="Chapter 1", isCompleted=true, id="task-1", dueDate=null) / When setDueDateLabel(textView, task) is called on the UI thread / Then textView.text.toString() is "CLEAR" and textView.visibility is View.VISIBLE
  - A visible-to-hidden-to-visible rebinding sequence cannot retain stale text or visibility. `[Integration]`
    - Tip: Given the device local date is fixed at 2026-10-14 (Asia/Seoul), textView.text="", textView.visibility=View.GONE, and Task values use title="Read", description="Chapter 1", id="task-1", isCompleted=false / When setDueDateLabel is called on the UI thread on the same TextView with dueDate=2026-10-15, then task=null, then dueDate=2026-10-13 / Then the text/visibility after each call is respectively "D-1"/View.VISIBLE, ""/View.GONE, and "D+1"/View.VISIBLE

**Undefined behavior** (optional)
- Calls from outside the UI thread or while the device date changes during a call are outside these pre-conditions.
