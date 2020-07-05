package com.example.flcikersample.data.models

import androidx.paging.PagingSource
import com.example.flcikersample.api.FlickerService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.HttpException
import java.io.IOException
import kotlin.collections.emptyList

private const val FLICKER_STARTING_PAGE_INDEX = 1

@ExperimentalCoroutinesApi
class FlickerPagingSource(
    private val method: String,
    private val apiKey: String,
    private val service: FlickerService,
    private val query: String,
    private val format: String,
    private val nojsonCallback: Int,
    private val perPage: Int
) : PagingSource<Int, Photo>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val currentPage = params.key ?: FLICKER_STARTING_PAGE_INDEX
        val apiQuery = query
        try {
            val apiResponse = service.searchFlickerImage(method, apiKey,query,format,nojsonCallback,perPage,currentPage)
            return if (apiResponse.isSuccessful) {
                val repos = apiResponse.body()?.photos?.photo ?: emptyList()
                LoadResult.Page(
                    data = repos,
                    prevKey = if (currentPage == FLICKER_STARTING_PAGE_INDEX) null else currentPage - 1,
                    // if we don't get any results, we consider that we're at the last page
                    nextKey = if (repos.isEmpty()) null else currentPage + 1
                )
            } else {
                LoadResult.Error(IOException(apiResponse.message()))
            }
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}
