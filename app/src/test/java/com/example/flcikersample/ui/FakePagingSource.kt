package com.example.flcikersample.ui

import androidx.paging.PagingSource
import com.example.flcikersample.data.models.FlickerItem
import com.example.flcikersample.data.models.Photo
import com.example.flcikersample.data.models.Photos

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

private const val FLICKER_STARTING_PAGE_INDEX = 1

class FakePagingSource: PagingSource<Int, Photo>()  {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val currentPage = params.key ?: FLICKER_STARTING_PAGE_INDEX
        try {
            val photoList = (0 until 20).map {
                Photo(
                    66, "50073917203", 0, 0, 1,
                    "170228053@N04", "d5795efcf6",
                    "65535",
                    "Copy of Million Money & forsage"
                )
            }


            val photos = Photos(1,1,20,photoList,"20")
            val flickerItem = FlickerItem(photos,"stat")
            val apiResponse  = Response.success(flickerItem)
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