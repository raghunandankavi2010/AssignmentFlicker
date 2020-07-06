package com.example.flcikersample.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingDataFlow
import com.example.flcikersample.data.models.Photo
import com.example.flcikersample.repo.FlickerSearchRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

class FlickerSearchViewModelTest {

    @get:Rule
    var coroutinesRule = MainCoroutineRule()

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val flickerSearchRepository: FlickerSearchRepository = mock()
    private val savedStateHandle: SavedStateHandle = mock()

    private val query = "Mobile"

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)
        whenever(flickerSearchRepository.getSearchResultStream(query)).thenReturn(getPagingDataFlow())
    }

    @After
    fun tearDown() {
    }

    @ExperimentalCoroutinesApi
    @Test
    fun searchFlicker() = runBlockingTest {
        val viewModel = FlickerSearchViewModel(flickerSearchRepository, savedStateHandle)

        viewModel.searchFlicker(query)

        val observer = flickerSearchRepository.getSearchResultStream(query).test(this)
        observer.assertNotNull()
        observer.finish()

    }


    private fun getPagingDataFlow(): Flow<PagingData<Photo>> {
        return PagingDataFlow(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                FakePagingSource()
            }
        )
    }
}
