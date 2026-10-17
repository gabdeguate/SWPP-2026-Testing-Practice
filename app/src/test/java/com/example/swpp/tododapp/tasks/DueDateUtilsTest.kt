package com.example.swpp.tododapp.tasks

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class DueDateUtilsTest {

    // DueDateUtils.spec.md Tip 1.
    @Test
    fun dueDateLabel_completedWithoutDueDate_returnsClear() {
        // Given
        val dueDate: LocalDate? = null
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = true

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("CLEAR", label)
    }

    // DueDateUtils.spec.md Tip 2.
    @Test
    fun dueDateLabel_completedOverdue_returnsClear() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-10-13")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = true

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("CLEAR", label)
    }

    // DueDateUtils.spec.md Tip 3.
    @Test
    fun dueDateLabel_completedDueToday_returnsClear() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-10-14")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = true

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("CLEAR", label)
    }

    // DueDateUtils.spec.md Tip 4.
    @Test
    fun dueDateLabel_completedFuture_returnsClear() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-10-15")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = true

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("CLEAR", label)
    }

    // DueDateUtils.spec.md Tip 5.
    @Test
    fun dueDateLabel_activeWithoutDueDate_returnsEmpty() {
        // Given
        val dueDate: LocalDate? = null
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("", label)
    }

    // DueDateUtils.spec.md Tip 6.
    @Test
    fun dueDateLabel_activeDueToday_returnsDDay() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-10-14")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-Day", label)
    }

    // DueDateUtils.spec.md Tip 7.
    @Test
    fun dueDateLabel_activeTenDaysFuture_returnsDMinus10() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-10-24")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-10", label)
    }

    // DueDateUtils.spec.md Tip 8.
    @Test
    fun dueDateLabel_activeTenDaysOverdue_returnsDPlus10() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-10-04")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D+10", label)
    }

    // DueDateUtils.spec.md Tip 9.
    @Test
    fun dueDateLabel_yesterdayBoundary_returnsDPlus1() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-10-13")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D+1", label)
    }

    // DueDateUtils.spec.md Tip 10.
    @Test
    fun dueDateLabel_todayBoundary_returnsDDay() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-10-14")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-Day", label)
    }

    // DueDateUtils.spec.md Tip 11.
    @Test
    fun dueDateLabel_tomorrowBoundary_returnsDMinus1() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-10-15")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-1", label)
    }

    // DueDateUtils.spec.md Tip 12.
    @Test
    fun dueDateLabel_futureMonthBoundaryBefore_returnsDMinus30() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-11-13")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-30", label)
    }

    // DueDateUtils.spec.md Tip 13.
    @Test
    fun dueDateLabel_futureMonthBoundaryAt_returnsDMinus31() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-11-14")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-31", label)
    }

    // DueDateUtils.spec.md Tip 14.
    @Test
    fun dueDateLabel_futureMonthBoundaryAfter_returnsDMinus32() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-11-15")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-32", label)
    }

    // DueDateUtils.spec.md Tip 15.
    @Test
    fun dueDateLabel_overdueMonthBoundaryBefore_returnsDPlus31() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-09-13")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D+31", label)
    }

    // DueDateUtils.spec.md Tip 16.
    @Test
    fun dueDateLabel_overdueMonthBoundaryAt_returnsDPlus30() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-09-14")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D+30", label)
    }

    // DueDateUtils.spec.md Tip 17.
    @Test
    fun dueDateLabel_overdueMonthBoundaryAfter_returnsDPlus29() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-09-15")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D+29", label)
    }

    // DueDateUtils.spec.md Tip 18.
    @Test
    fun dueDateLabel_futureMonthEndAt_returnsDMinus1() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-01-31")
        val today = LocalDate.parse("2026-01-30")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-1", label)
    }

    // DueDateUtils.spec.md Tip 19.
    @Test
    fun dueDateLabel_futureMonthEndAfter_returnsDMinus2() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-02-01")
        val today = LocalDate.parse("2026-01-30")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-2", label)
    }

    // DueDateUtils.spec.md Tip 20.
    @Test
    fun dueDateLabel_overdueMonthEndAt_returnsDPlus1() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-01-31")
        val today = LocalDate.parse("2026-02-01")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D+1", label)
    }

    // DueDateUtils.spec.md Tip 21.
    @Test
    fun dueDateLabel_overdueMonthEndBefore_returnsDPlus2() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-01-30")
        val today = LocalDate.parse("2026-02-01")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D+2", label)
    }

    // DueDateUtils.spec.md Tip 22.
    @Test
    fun dueDateLabel_futureYearEndAt_returnsDMinus1() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-12-31")
        val today = LocalDate.parse("2026-12-30")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-1", label)
    }

    // DueDateUtils.spec.md Tip 23.
    @Test
    fun dueDateLabel_futureYearEndAfter_returnsDMinus2() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2027-01-01")
        val today = LocalDate.parse("2026-12-30")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-2", label)
    }

    // DueDateUtils.spec.md Tip 24.
    @Test
    fun dueDateLabel_overdueYearEndAt_returnsDPlus1() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-12-31")
        val today = LocalDate.parse("2027-01-01")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D+1", label)
    }

    // DueDateUtils.spec.md Tip 25.
    @Test
    fun dueDateLabel_overdueYearEndBefore_returnsDPlus2() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-12-30")
        val today = LocalDate.parse("2027-01-01")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D+2", label)
    }

    // DueDateUtils.spec.md Tip 26.
    @Test
    fun dueDateLabel_futureLeapDayBefore_returnsDMinus1() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2028-02-28")
        val today = LocalDate.parse("2028-02-27")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-1", label)
    }

    // DueDateUtils.spec.md Tip 27.
    @Test
    fun dueDateLabel_futureLeapDayAt_returnsDMinus2() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2028-02-29")
        val today = LocalDate.parse("2028-02-27")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-2", label)
    }

    // DueDateUtils.spec.md Tip 28.
    @Test
    fun dueDateLabel_futureLeapDayAfter_returnsDMinus3() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2028-03-01")
        val today = LocalDate.parse("2028-02-27")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-3", label)
    }

    // DueDateUtils.spec.md Tip 29.
    @Test
    fun dueDateLabel_overdueLeapDayAt_returnsDPlus1() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2028-02-29")
        val today = LocalDate.parse("2028-03-01")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D+1", label)
    }

    // DueDateUtils.spec.md Tip 30.
    @Test
    fun dueDateLabel_overdueLeapDayBefore_returnsDPlus2() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2028-02-28")
        val today = LocalDate.parse("2028-03-01")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D+2", label)
    }

    // DueDateUtils.spec.md Tip 31.
    @Test
    fun dueDateLabel_nonLeapFebruaryEndAt_returnsDMinus1() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2027-02-28")
        val today = LocalDate.parse("2027-02-27")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-1", label)
    }

    // DueDateUtils.spec.md Tip 32.
    @Test
    fun dueDateLabel_nonLeapFebruaryEndAfter_returnsDMinus2() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2027-03-01")
        val today = LocalDate.parse("2027-02-27")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-2", label)
    }

    // DueDateUtils.spec.md Tip 33.
    @Test
    fun dueDateLabel_fullNonLeapYearFuture_returnsDMinus365() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2027-01-01")
        val today = LocalDate.parse("2026-01-01")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-365", label)
    }

    // DueDateUtils.spec.md Tip 34.
    @Test
    fun dueDateLabel_fullNonLeapYearOverdue_returnsDPlus365() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2026-01-01")
        val today = LocalDate.parse("2027-01-01")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D+365", label)
    }

    // DueDateUtils.spec.md Tip 35.
    @Test
    fun dueDateLabel_fullLeapYearFuture_returnsDMinus366() {
        // Given
        val dueDate: LocalDate? = LocalDate.parse("2029-01-01")
        val today = LocalDate.parse("2028-01-01")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-366", label)
    }
}
