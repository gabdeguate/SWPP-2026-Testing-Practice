package com.example.swpp.tododapp.tasks

import android.view.View
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.swpp.tododapp.data.Task
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import org.junit.Assert.assertEquals
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class TasksListBindingsTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()

    // Reading the date and binding on the UI thread keeps the interval short.
    // Midnight-spanning calls are outside the reviewed spec; skip them explicitly.

    // TasksListBindings.spec.md Tip 1.
    @Test
    fun setDueDateLabel_nullTask_clearsTextAndHides() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "stale"
                visibility = View.VISIBLE
            }
            val task: Task? = null

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("", textView.text.toString())
            assertEquals(View.GONE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 2.
    @Test
    fun setDueDateLabel_activeWithoutDueDate_clearsTextAndHides() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "stale"
                visibility = View.VISIBLE
            }
            val task: Task? = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = false, dueDate = null)

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("", textView.text.toString())
            assertEquals(View.GONE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 3.
    @Test
    fun setDueDateLabel_completedWithoutDueDate_showsClear() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "stale"
                visibility = View.VISIBLE
            }
            val task: Task? = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = true, dueDate = null)

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("CLEAR", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 4.
    @Test
    fun setDueDateLabel_completedOverdue_showsClear() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "stale"
                visibility = View.VISIBLE
            }
            val task: Task? = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = true, dueDate = today.minusDays(1))

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("CLEAR", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 5.
    @Test
    fun setDueDateLabel_activeDueToday_showsDDay() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "stale"
                visibility = View.VISIBLE
            }
            val task: Task? = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = false, dueDate = today)

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("D-Day", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 6.
    @Test
    fun setDueDateLabel_activeTenDaysFuture_showsDMinus10() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "stale"
                visibility = View.VISIBLE
            }
            val task: Task? = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = false, dueDate = today.plusDays(10))

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("D-10", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 7.
    @Test
    fun setDueDateLabel_activeTenDaysOverdue_showsDPlus10() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "stale"
                visibility = View.VISIBLE
            }
            val task: Task? = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = false, dueDate = today.minusDays(10))

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("D+10", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 8.
    @Test
    fun setDueDateLabel_yesterdayBoundary_showsDPlus1() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "stale"
                visibility = View.VISIBLE
            }
            val task: Task? = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = false, dueDate = today.minusDays(1))

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("D+1", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 9.
    @Test
    fun setDueDateLabel_todayBoundary_showsDDay() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "stale"
                visibility = View.VISIBLE
            }
            val task: Task? = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = false, dueDate = today)

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("D-Day", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 10.
    @Test
    fun setDueDateLabel_tomorrowBoundary_showsDMinus1() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "stale"
                visibility = View.VISIBLE
            }
            val task: Task? = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = false, dueDate = today.plusDays(1))

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("D-1", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 11.
    @Test
    fun setDueDateLabel_futureMonthBoundaryBefore_showsTotalDays() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val due = today.plusMonths(1).minusDays(1)
            val n = ChronoUnit.DAYS.between(today, due)
            val textView = TextView(instrumentation.targetContext).apply {
                text = "stale"
                visibility = View.VISIBLE
            }
            val task: Task? = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = false, dueDate = due)

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("D-$n", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 12.
    @Test
    fun setDueDateLabel_futureMonthBoundaryAt_showsTotalDays() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val due = today.plusMonths(1)
            val n = ChronoUnit.DAYS.between(today, due)
            val textView = TextView(instrumentation.targetContext).apply {
                text = "stale"
                visibility = View.VISIBLE
            }
            val task: Task? = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = false, dueDate = due)

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("D-$n", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 13.
    @Test
    fun setDueDateLabel_futureMonthBoundaryAfter_showsTotalDays() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val due = today.plusMonths(1).plusDays(1)
            val n = ChronoUnit.DAYS.between(today, due)
            val textView = TextView(instrumentation.targetContext).apply {
                text = "stale"
                visibility = View.VISIBLE
            }
            val task: Task? = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = false, dueDate = due)

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("D-$n", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 14.
    @Test
    fun setDueDateLabel_overdueMonthBoundaryBefore_showsTotalDays() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val due = today.minusMonths(1).minusDays(1)
            val n = ChronoUnit.DAYS.between(due, today)
            val textView = TextView(instrumentation.targetContext).apply {
                text = "stale"
                visibility = View.VISIBLE
            }
            val task: Task? = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = false, dueDate = due)

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("D+$n", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 15.
    @Test
    fun setDueDateLabel_overdueMonthBoundaryAt_showsTotalDays() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val due = today.minusMonths(1)
            val n = ChronoUnit.DAYS.between(due, today)
            val textView = TextView(instrumentation.targetContext).apply {
                text = "stale"
                visibility = View.VISIBLE
            }
            val task: Task? = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = false, dueDate = due)

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("D+$n", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 16.
    @Test
    fun setDueDateLabel_overdueMonthBoundaryAfter_showsTotalDays() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val due = today.minusMonths(1).plusDays(1)
            val n = ChronoUnit.DAYS.between(due, today)
            val textView = TextView(instrumentation.targetContext).apply {
                text = "stale"
                visibility = View.VISIBLE
            }
            val task: Task? = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = false, dueDate = due)

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("D+$n", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 17.
    @Test
    fun setDueDateLabel_recycledLabelToNull_clearsTextAndHides() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "D-1"
                visibility = View.VISIBLE
            }
            val task: Task? = null

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("", textView.text.toString())
            assertEquals(View.GONE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 18.
    @Test
    fun setDueDateLabel_recycledLabelToActiveUndated_clearsTextAndHides() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "D-1"
                visibility = View.VISIBLE
            }
            val task: Task? = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = false, dueDate = null)

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("", textView.text.toString())
            assertEquals(View.GONE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 19.
    @Test
    fun setDueDateLabel_recycledLabelToCompleted_showsClear() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "D-1"
                visibility = View.VISIBLE
            }
            val task: Task? = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = true, dueDate = null)

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("CLEAR", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 20.
    @Test
    fun setDueDateLabel_hiddenToDated_showsDMinus1() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = ""
                visibility = View.GONE
            }
            val task: Task? = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = false, dueDate = today.plusDays(1))

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("D-1", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 21.
    @Test
    fun setDueDateLabel_hiddenToCompletedUndated_showsClear() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = ""
                visibility = View.GONE
            }
            val task: Task? = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = true, dueDate = null)

            // When
            setDueDateLabel(textView, task)

            // Then
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("CLEAR", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    // TasksListBindings.spec.md Tip 22.
    @Test
    fun setDueDateLabel_visibleHiddenVisibleRebinding_replacesTextAndVisibility() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = ""
                visibility = View.GONE
            }
            val futureTask = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = false, dueDate = today.plusDays(1))
            val overdueTask = Task(title = "Read", description = "Chapter 1", id = "task-1",
                isCompleted = false, dueDate = today.minusDays(1))

            // When / Then - Check each state on the same view before rebinding it.
            setDueDateLabel(textView, futureTask)
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("D-1", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)

            setDueDateLabel(textView, null)
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("", textView.text.toString())
            assertEquals(View.GONE, textView.visibility)

            setDueDateLabel(textView, overdueTask)
            assumeTrue("Local date changed during binding", today == LocalDate.now())
            assertEquals("D+1", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }
}
