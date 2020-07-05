/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.flcikersample.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView

import com.example.flcikersample.R
import com.example.flcikersample.databinding.FlickerLoadStateHeaderViewItemBinding

class FlickerImageLoadStateViewHolder(
    private val binding: FlickerLoadStateHeaderViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.also {
            it.setOnClickListener { retry.invoke() }
        }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.visibility = toVisibility(loadState == LoadState.Loading)
        binding.retryButton.visibility = toVisibility(loadState != LoadState.Loading)
        binding.errorMsg.visibility = toVisibility(loadState != LoadState.Loading)
    }

    private fun toVisibility(constraint: Boolean): Int = if (constraint) {
        View.VISIBLE
    } else {
        View.GONE
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): FlickerImageLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.flicker_load_state_header_view_item, parent, false)
            val binding = FlickerLoadStateHeaderViewItemBinding.bind(view)
            return FlickerImageLoadStateViewHolder(binding, retry)
        }
    }
}
