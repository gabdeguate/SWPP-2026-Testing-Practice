# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `internal fun dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Build the task-list D-day label, giving completion priority. Count the total calendar days between today and the due date across month ends, year ends, and leap days.

**Pre-conditions**
- Contract source: the function documentation on the locally available origin/feature/due-date branch (commit 7e091a7); the current main checkout does not contain this function.
- today is a valid LocalDate; dueDate is either a valid LocalDate or null. isCompleted may be true or false. Dates carry no time of day or time zone.

**Post-conditions**
- Success cases
  - Completed tasks return "CLEAR" regardless of whether their due date is absent, past, present, or future. `[Unit]`
    - Tip: Given dueDate=null, today=2026-10-14, isCompleted=true / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "CLEAR"
  - Completion takes precedence over an overdue date. `[Unit]`
    - Tip: Given dueDate=2026-10-13, today=2026-10-14, isCompleted=true / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "CLEAR"
  - Completion takes precedence over a date due today. `[Unit]`
    - Tip: Given dueDate=2026-10-14, today=2026-10-14, isCompleted=true / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "CLEAR"
  - Completion takes precedence over a future date. `[Unit]`
    - Tip: Given dueDate=2026-10-15, today=2026-10-14, isCompleted=true / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "CLEAR"
  - An active task without a due date returns the empty string. `[Unit]`
    - Tip: Given dueDate=null, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is ""
  - An active task due today returns "D-Day". `[Unit]`
    - Tip: Given dueDate=2026-10-14, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-Day"
  - An active task due in N calendar days returns "D-N", with N as an unsigned decimal integer. `[Unit]`
    - Tip: Given dueDate=2026-10-24, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-10"
  - An active task overdue by N calendar days returns "D+N", with N as an unsigned decimal integer. `[Unit]`
    - Tip: Given dueDate=2026-10-04, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D+10"
- Error cases
  - None. All inputs satisfying the pre-conditions are valid.
- Edge cases
  - The overdue/today/future boundary uses whole calendar days (one day before, at, and one day after today). `[Unit]`
    - Tip: Given dueDate=2026-10-13, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D+1"
  - The overdue/today/future boundary uses whole calendar days (one day before, at, and one day after today). `[Unit]`
    - Tip: Given dueDate=2026-10-14, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-Day"
  - The overdue/today/future boundary uses whole calendar days (one day before, at, and one day after today). `[Unit]`
    - Tip: Given dueDate=2026-10-15, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-1"
  - Calendar-day counting remains continuous immediately before, at, and immediately after month end. `[Unit]`
    - Tip: Given dueDate=2026-01-30, today=2026-01-30, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-Day"
  - Calendar-day counting remains continuous immediately before, at, and immediately after month end. `[Unit]`
    - Tip: Given dueDate=2026-01-31, today=2026-01-30, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-1"
  - Calendar-day counting remains continuous immediately before, at, and immediately after month end. `[Unit]`
    - Tip: Given dueDate=2026-02-01, today=2026-01-30, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-2"
  - Calendar-day counting remains continuous immediately before, at, and immediately after year end. `[Unit]`
    - Tip: Given dueDate=2026-12-30, today=2026-12-30, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-Day"
  - Calendar-day counting remains continuous immediately before, at, and immediately after year end. `[Unit]`
    - Tip: Given dueDate=2026-12-31, today=2026-12-30, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-1"
  - Calendar-day counting remains continuous immediately before, at, and immediately after year end. `[Unit]`
    - Tip: Given dueDate=2027-01-01, today=2026-12-30, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-2"
  - Calendar-day counting remains continuous immediately before, at, and immediately after leap day. `[Unit]`
    - Tip: Given dueDate=2028-02-28, today=2028-02-27, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-1"
  - Calendar-day counting remains continuous immediately before, at, and immediately after leap day. `[Unit]`
    - Tip: Given dueDate=2028-02-29, today=2028-02-27, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-2"
  - Calendar-day counting remains continuous immediately before, at, and immediately after leap day. `[Unit]`
    - Tip: Given dueDate=2028-03-01, today=2028-02-27, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-3"
  - Calendar-day counting remains continuous immediately before, at, and immediately after a non-leap February end. `[Unit]`
    - Tip: Given dueDate=2027-02-27, today=2027-02-27, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-Day"
  - Calendar-day counting remains continuous immediately before, at, and immediately after a non-leap February end. `[Unit]`
    - Tip: Given dueDate=2027-02-28, today=2027-02-27, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-1"
  - Calendar-day counting remains continuous immediately before, at, and immediately after a non-leap February end. `[Unit]`
    - Tip: Given dueDate=2027-03-01, today=2027-02-27, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-2"
  - N is the total calendar-day distance, including complete months, years, and leap days, rather than a remaining day component. `[Unit]`
    - Tip: Given dueDate=2026-02-01, today=2026-01-01, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-31"
  - N is the total calendar-day distance, including complete months, years, and leap days, rather than a remaining day component. `[Unit]`
    - Tip: Given dueDate=2026-01-01, today=2026-02-01, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D+31"
  - N is the total calendar-day distance, including complete months, years, and leap days, rather than a remaining day component. `[Unit]`
    - Tip: Given dueDate=2027-01-01, today=2026-01-01, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-365"
  - N is the total calendar-day distance, including complete months, years, and leap days, rather than a remaining day component. `[Unit]`
    - Tip: Given dueDate=2026-01-01, today=2027-01-01, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D+365"
  - N is the total calendar-day distance, including complete months, years, and leap days, rather than a remaining day component. `[Unit]`
    - Tip: Given dueDate=2028-02-28, today=2028-03-01, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D+2"

**Undefined behavior** (optional)
- None for values admitted by the Kotlin signature.
