package com.example.flcikersample.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.flcikersample.data.models.Photo
import com.example.flcikersample.repo.FlickerSearchRepository
import com.task.utils.EspressoIdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class FlickerSearchViewModel @ExperimentalCoroutinesApi
@ViewModelInject constructor(
    private val flickerSearchRepository: FlickerSearchRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    @Volatile
    private var currentQueryValue: String? = null

    @Volatile
    private var currentSearchResult: Flow<PagingData<Photo>>? = null

    @ExperimentalCoroutinesApi
    fun searchFlicker(queryString: String): Flow<PagingData<Photo>> {
        EspressoIdlingResource.increment()
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<Photo>> = flickerSearchRepository.getSearchResultStream(queryString)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }



}