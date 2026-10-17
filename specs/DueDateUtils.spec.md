# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `internal fun dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Build the D-day label shown next to a task in the list. Completion has priority over the due date. N is the total number of calendar days between today and the due date, so it stays correct across month ends, year ends, and leap days.

**Pre-conditions**
- Contract source: the KDoc of `dueDateLabel` on branch `origin/feature/due-date` (commit 7e091a7). The `main` checkout does not contain this function.
- `today` is a valid `LocalDate`. `dueDate` is a valid `LocalDate` or `null`. `isCompleted` is `true` or `false`. Dates have no time of day and no time zone.

**Post-conditions**
- Success cases
  - A completed task without a due date returns "CLEAR". `[Unit]`
    - Tip: Given dueDate=null, today=2026-10-14, isCompleted=true / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "CLEAR"
  - A completed task returns "CLEAR" even when it is overdue. `[Unit]`
    - Tip: Given dueDate=2026-10-13, today=2026-10-14, isCompleted=true / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "CLEAR"
  - A completed task returns "CLEAR" even when it is due today. `[Unit]`
    - Tip: Given dueDate=2026-10-14, today=2026-10-14, isCompleted=true / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "CLEAR"
  - A completed task returns "CLEAR" even when it is due in the future. `[Unit]`
    - Tip: Given dueDate=2026-10-15, today=2026-10-14, isCompleted=true / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "CLEAR"
  - An active task without a due date returns the empty string. `[Unit]`
    - Tip: Given dueDate=null, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is ""
  - An active task due today returns "D-Day". `[Unit]`
    - Tip: Given dueDate=2026-10-14, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-Day"
  - An active task due in N calendar days returns "D-N", with N as a decimal integer without sign or padding. `[Unit]`
    - Tip: Given dueDate=2026-10-24, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-10"
  - An active task overdue by N calendar days returns "D+N", with N as a decimal integer without sign or padding. `[Unit]`
    - Tip: Given dueDate=2026-10-04, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D+10"
- Error cases
  - None. Every input that satisfies the pre-conditions is valid.
- Edge cases
  - Today boundary, one day before: a task due yesterday is overdue by 1. `[Unit]`
    - Tip: Given dueDate=2026-10-13, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D+1"
  - Today boundary, at: a task due today returns "D-Day". `[Unit]`
    - Tip: Given dueDate=2026-10-14, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-Day"
  - Today boundary, one day after: a task due tomorrow is due in 1. `[Unit]`
    - Tip: Given dueDate=2026-10-15, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-1"
  - One-month boundary (future), one day before: 30 days ahead returns "D-30". `[Unit]`
    - Tip: Given dueDate=2026-11-13, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-30"
  - One-month boundary (future), at: exactly one calendar month ahead returns the total day count, not "D-Day". `[Unit]`
    - Tip: Given dueDate=2026-11-14, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-31"
  - One-month boundary (future), one day after: one month and one day ahead returns the total day count, not "D-1". `[Unit]`
    - Tip: Given dueDate=2026-11-15, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-32"
  - One-month boundary (overdue), one day before: one month and one day overdue returns the total day count. `[Unit]`
    - Tip: Given dueDate=2026-09-13, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D+31"
  - One-month boundary (overdue), at: exactly one calendar month overdue returns the total day count, not "D-Day". `[Unit]`
    - Tip: Given dueDate=2026-09-14, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D+30"
  - One-month boundary (overdue), one day after: 29 days overdue returns "D+29". `[Unit]`
    - Tip: Given dueDate=2026-09-15, today=2026-10-14, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D+29"
  - Month end (future), one day before: a due date on January 30 is due today. `[Unit]`
    - Tip: Given dueDate=2026-01-30, today=2026-01-30, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-Day"
  - Month end (future), at: a due date on the last day of the month counts 1 day. `[Unit]`
    - Tip: Given dueDate=2026-01-31, today=2026-01-30, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-1"
  - Month end (future), one day after: a due date on the first day of the next month counts 2 days. `[Unit]`
    - Tip: Given dueDate=2026-02-01, today=2026-01-30, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-2"
  - Month end (overdue), at: a due date on the last day of the previous month is overdue by 1. `[Unit]`
    - Tip: Given dueDate=2026-01-31, today=2026-02-01, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D+1"
  - Month end (overdue), one day before: a due date one day before the previous month end is overdue by 2. `[Unit]`
    - Tip: Given dueDate=2026-01-30, today=2026-02-01, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D+2"
  - Month end (overdue), one day after: a due date on February 1 is due today. `[Unit]`
    - Tip: Given dueDate=2026-02-01, today=2026-02-01, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-Day"
  - Year end (future), one day before: a due date on December 30 is due today. `[Unit]`
    - Tip: Given dueDate=2026-12-30, today=2026-12-30, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-Day"
  - Year end (future), at: a due date on December 31 counts 1 day. `[Unit]`
    - Tip: Given dueDate=2026-12-31, today=2026-12-30, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-1"
  - Year end (future), one day after: a due date on January 1 of the next year counts 2 days. `[Unit]`
    - Tip: Given dueDate=2027-01-01, today=2026-12-30, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-2"
  - Year end (overdue), at: a due date on December 31 of the previous year is overdue by 1. `[Unit]`
    - Tip: Given dueDate=2026-12-31, today=2027-01-01, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D+1"
  - Year end (overdue), one day before: a due date on December 30 of the previous year is overdue by 2. `[Unit]`
    - Tip: Given dueDate=2026-12-30, today=2027-01-01, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D+2"
  - Year end (overdue), one day after: a due date on January 1 is due today. `[Unit]`
    - Tip: Given dueDate=2027-01-01, today=2027-01-01, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-Day"
  - Leap day (future), one day before: February 28 of a leap year counts 1 day. `[Unit]`
    - Tip: Given dueDate=2028-02-28, today=2028-02-27, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-1"
  - Leap day (future), at: February 29 counts 2 days. `[Unit]`
    - Tip: Given dueDate=2028-02-29, today=2028-02-27, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-2"
  - Leap day (future), one day after: March 1 counts 3 days, including February 29. `[Unit]`
    - Tip: Given dueDate=2028-03-01, today=2028-02-27, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-3"
  - Leap day (overdue), at: February 29 is overdue by 1 on March 1. `[Unit]`
    - Tip: Given dueDate=2028-02-29, today=2028-03-01, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D+1"
  - Leap day (overdue), one day before: February 28 is overdue by 2 on March 1, including February 29. `[Unit]`
    - Tip: Given dueDate=2028-02-28, today=2028-03-01, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D+2"
  - Leap day (overdue), one day after: a due date on March 1 is due today. `[Unit]`
    - Tip: Given dueDate=2028-03-01, today=2028-03-01, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-Day"
  - Non-leap February end, one day before: a due date on February 27 is due today. `[Unit]`
    - Tip: Given dueDate=2027-02-27, today=2027-02-27, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-Day"
  - Non-leap February end, at: February 28 counts 1 day. `[Unit]`
    - Tip: Given dueDate=2027-02-28, today=2027-02-27, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-1"
  - Non-leap February end, one day after: March 1 counts 2 days, with no February 29. `[Unit]`
    - Tip: Given dueDate=2027-03-01, today=2027-02-27, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-2"
  - One full non-leap year ahead returns the total day count, not "D-Day". `[Unit]`
    - Tip: Given dueDate=2027-01-01, today=2026-01-01, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-365"
  - One full non-leap year overdue returns the total day count, not "D-Day". `[Unit]`
    - Tip: Given dueDate=2026-01-01, today=2027-01-01, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D+365"
  - One full leap year ahead includes February 29 in the count. `[Unit]`
    - Tip: Given dueDate=2029-01-01, today=2028-01-01, isCompleted=false / When dueDateLabel(dueDate, today, isCompleted) is called / Then the returned string is "D-366"

**Undefined behavior** (optional)
- None for values that the Kotlin signature admits.
