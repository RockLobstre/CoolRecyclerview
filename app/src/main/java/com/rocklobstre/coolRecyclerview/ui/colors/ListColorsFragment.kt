package com.rocklobstre.coolRecyclerview.ui.colors

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rocklobstre.coolRecyclerview.BuildConfig
import com.rocklobstre.coolRecyclerview.R
import com.rocklobstre.coolRecyclerview.data.remote.response.Status
import com.rocklobstre.coolRecyclerview.databinding.FragmentListColorsBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * @author Matin Salehi on 31/12/2017.
 */
class ListColorsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ListColorsViewModel

    private lateinit var binding: FragmentListColorsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ListColorsViewModel::class.java)

        observeLoadingStatus()
        observeResponse()

        viewModel.loadData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_colors, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.swipeContainer.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                viewModel.loadData();
            }
        })
        return binding.root
    }

    private fun observeResponse() {
        viewModel.loadingStatus.observe(
                this,
                Observer { isLoading -> binding.swipeContainer.isRefreshing = isLoading ?: false })
    }

    private fun observeLoadingStatus() {
        viewModel.response.observe(
                this,
                Observer { response ->
                    if(response != null && response.status == Status.SUCCESS) {
                        binding.colors = response.data
                        binding.executePendingBindings()
                    } else {
                        if ((response != null && response.status == Status.ERROR) && BuildConfig.DEBUG) {
                            Log.e("get colors error", response.error.toString())
                        }
                        Snackbar.make(binding.root, R.string.load_data_error, Snackbar.LENGTH_LONG).show()
                    }
                }
        )
    }

}
