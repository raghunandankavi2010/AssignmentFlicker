package com.example.flcikersample.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.flcikersample.data.models.FlickerItem
import com.example.flcikersample.data.models.Photo
import com.example.flcikersample.data.models.Photos
import com.example.flcikersample.repo.FlickerSearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FlickerSearchViewModel @ExperimentalCoroutinesApi
@ViewModelInject constructor(
    private val flickerSearchRepository: FlickerSearchRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private val SEARCH_QUERY = "searchquery"
    }
    private val _searchQuery: MutableLiveData<String> = savedStateHandle.getLiveData(SEARCH_QUERY)

    // Only expose a immutable LiveData
    val searchQuery : LiveData<String> = _searchQuery
    @Volatile
    private var currentQueryValue: String? = null

    @Volatile
    private var currentSearchResult: Flow<PagingData<Photo>>? = null

    @ExperimentalCoroutinesApi
    fun searchFlicker(queryString: String): Flow<PagingData<Photo>> {
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

    fun saveCurrentSearchQuery(query: String){
        savedStateHandle.set(SEARCH_QUERY, query)
    }

    fun getCurrentSearchQuery(): String {
        // Gets the current value of the user id from the saved state handle
        return savedStateHandle.get(SEARCH_QUERY)?: ""
    }
}