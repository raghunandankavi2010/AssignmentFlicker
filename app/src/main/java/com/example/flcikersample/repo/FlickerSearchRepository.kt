package com.example.flcikersample.repo

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingDataFlow
import com.example.flcikersample.api.FlickerService
import com.example.flcikersample.data.models.Photo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class FlickerSearchRepository @Inject constructor (private val service: FlickerService) {


    fun getSearchResultStream(query: String): Flow<PagingData<Photo>> {

        return PagingDataFlow(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                FlickerPagingSource(
                    "flickr.photos.search",
                    "062a6c0c49e4de1d78497d13a7dbb360",
                    service,
                    query,
                    "json",
                    1,
                    NETWORK_PAGE_SIZE
                )
            }
        )
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}
