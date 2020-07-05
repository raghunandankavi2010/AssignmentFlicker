package com.example.flcikersample.ui

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadType
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.flcikersample.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var  flickerSearchViewModel: FlickerSearchViewModel

    private lateinit var binding: ActivityMainBinding
    private val adapter = FlickerImageAdapter()
    private var searchJob: Job? = null
    private  var query: String = DEFAULT_QUERY


    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        flickerSearchViewModel = ViewModelProvider(this).get(FlickerSearchViewModel::class.java)

        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)

        initAdapter()


        flickerSearchViewModel.searchQuery
            .observe(this, Observer<String> {
                it?.let {
                    query = it
                    search(query)
                }
            })

        initSearch(query)


        binding.retryButton.setOnClickListener { adapter.retry() }
    }

    private fun initAdapter() {
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = FlickerImageLoadStateAdapter { adapter.retry() },
            footer = FlickerImageLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadType, loadState ->
            Log.d("MainActivity", "adapter load: type = $loadType state = $loadState")
            if (loadType == LoadType.REFRESH) {
                binding.list.visibility = View.GONE
                binding.progressBar.visibility = toVisibility(loadState == LoadState.Loading)
                binding.retryButton.visibility = toVisibility(loadState is LoadState.Error)
            } else {
                binding.list.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.retryButton.visibility = View.GONE
            }
            if (loadState is LoadState.Error) {
                Toast.makeText(
                    this,
                    "\uD83D\uDE28 Wooops $loadState.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun initSearch(query: String) {
        flickerSearchViewModel.saveCurrentSearchQuery(query)
        binding.searchImage.setText(query)

        binding.searchImage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
        binding.searchImage.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updateRepoListFromInput() {
        binding.searchImage.text.trim().let {
            if (it.isNotEmpty()) {
                binding.list.scrollToPosition(0)
                search(it.toString())
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            flickerSearchViewModel.searchFlicker(query).collect{
                Log.d("MainActivity", "query: $query, collecting $it")
                adapter.presentData(it)
            }
        }
    }

    private fun toVisibility(constraint: Boolean): Int = if (constraint) {
        View.VISIBLE
    } else {
        View.GONE
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = ""
    }
}