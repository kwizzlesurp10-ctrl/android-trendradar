package com.devradar.trendradar
import com.devradar.trendradar.ui.reviews.ReviewsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
@OptIn(ExperimentalCoroutinesApi::class)
class ReviewsViewModelTest {
  private val dispatcher = StandardTestDispatcher()
  @Before fun setUp() { Dispatchers.setMain(dispatcher) }
  @After  fun tearDown() { Dispatchers.resetMain() }
  @Test fun `initial state is loading`() = runTest {
    val vm = ReviewsViewModel()
    assertTrue(vm.state.value.isLoading)
  }
  @Test fun `reviews load after delay`() = runTest {
    val vm = ReviewsViewModel()
    advanceTimeBy(700)
    assertFalse(vm.state.value.isLoading)
    assertTrue(vm.state.value.reviews.isNotEmpty())
  }
  @Test fun `filter by rating 5 returns only 5-star reviews`() = runTest {
    val vm = ReviewsViewModel()
    advanceTimeBy(700)
    vm.setFilter(5)
    assertTrue(vm.state.value.reviews.all { it.rating == 5 })
  }
  @Test fun `clear filter returns all reviews`() = runTest {
    val vm = ReviewsViewModel()
    advanceTimeBy(700)
    val total = vm.state.value.reviews.size
    vm.setFilter(5)
    vm.setFilter(null)
    assertEquals(total, vm.state.value.reviews.size)
  }
  @Test fun `refresh sets isRefreshing then clears it`() = runTest {
    val vm = ReviewsViewModel()
    advanceTimeBy(700)
    vm.refresh()
    advanceTimeBy(100)
    assertTrue(vm.state.value.isRefreshing)
    advanceTimeBy(900)
    assertFalse(vm.state.value.isRefreshing)
  }
}
