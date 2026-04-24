package com.devradar.trendradar.ui.feed

import com.devradar.trendradar.core.repository.NewsDataSource
import com.devradar.trendradar.core.repository.NewsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load maps repository items into feed state`() = runTest {
        val source = object : NewsDataSource {
            override suspend fun loadTrendingNews(): List<NewsItem> = listOf(
                NewsItem(
                    id = "a1",
                    title = "Compose update",
                    source = "Android Developers",
                    url = "https://developer.android.com",
                    summary = "New release"
                )
            )
        }

        val viewModel = FeedViewModel(source)
        advanceUntilIdle()

        assertFalse(viewModel.state.value.isLoading)
        assertTrue(viewModel.state.value.error == null)
        assertEquals(1, viewModel.state.value.items.size)
        assertEquals("Compose update", viewModel.state.value.items.first().title)
        assertEquals("Android", viewModel.state.value.items.first().tag)
    }

    @Test
    fun `refresh sets isRefreshing true then false`() = runTest {
        val source = object : NewsDataSource {
            override suspend fun loadTrendingNews(): List<NewsItem> {
                delay(50)
                return listOf(
                    NewsItem(
                        id = "a1",
                        title = "Policy update",
                        source = "Google Play",
                        url = "https://play.google.com",
                        summary = "Details"
                    )
                )
            }
        }

        val viewModel = FeedViewModel(source)
        advanceUntilIdle()

        viewModel.refresh()
        runCurrent()
        assertTrue(viewModel.state.value.isRefreshing)

        advanceUntilIdle()
        assertFalse(viewModel.state.value.isRefreshing)
        assertFalse(viewModel.state.value.isLoading)
        assertTrue(viewModel.state.value.error == null)
    }

    @Test
    fun `load failure exposes error and clears items`() = runTest {
        val source = object : NewsDataSource {
            override suspend fun loadTrendingNews(): List<NewsItem> {
                throw IllegalStateException("backend unavailable")
            }
        }

        val viewModel = FeedViewModel(source)
        advanceUntilIdle()

        assertFalse(viewModel.state.value.isLoading)
        assertTrue(viewModel.state.value.items.isEmpty())
        assertEquals("backend unavailable", viewModel.state.value.error)
    }
}
