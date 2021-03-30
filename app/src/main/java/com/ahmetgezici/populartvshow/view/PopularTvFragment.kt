package com.ahmetgezici.populartvshow.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmetgezici.populartvshow.R
import com.ahmetgezici.populartvshow.adapter.PopularTvAdapter
import com.ahmetgezici.populartvshow.api.ApiClient
import com.ahmetgezici.populartvshow.databinding.FragmentPopularTvBinding
import com.ahmetgezici.populartvshow.model.database.FavoriteTv
import com.ahmetgezici.populartvshow.model.populartv.PopularTv
import com.ahmetgezici.populartvshow.model.populartv.Results
import com.ahmetgezici.populartvshow.utils.datautil.Resource
import com.ahmetgezici.populartvshow.utils.datautil.Status
import com.ahmetgezici.populartvshow.viewmodel.PopularTvViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList


class PopularTvFragment : Fragment() {

    private lateinit var binding: FragmentPopularTvBinding

    private lateinit var viewModel: PopularTvViewModel

    lateinit var popularTvAdapter: PopularTvAdapter

    companion object {

        var lastPopularTvList: ArrayList<Results>? = null

        const val apiKey = ApiClient.apiKey
        const val language = "tr-TR"
        var page = 1

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPopularTvBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(PopularTvViewModel::class.java)

        ////////////////////////////////////////////////////////////////////////////////////////////

        viewModel.loading.observe(viewLifecycleOwner, {
            if (it) {
                binding.loadingProgress.visibility = View.VISIBLE
                binding.popularTvRecycler.visibility = View.INVISIBLE
            } else {
                binding.loadingProgress.visibility = View.GONE
                binding.popularTvRecycler.visibility = View.VISIBLE
            }
        })

        ////////////////////////////////////////

        viewModel.newPageLoading.observe(viewLifecycleOwner, {

            if (it) {
                binding.newPageProgress.visibility = View.VISIBLE
            } else {
                binding.newPageProgress.visibility = View.GONE
            }

        })

        ////////////////////////////////////////////////////////////////////////////////////////////

        getFirstData()

        ////////////////////////////////////////////////////////////////////////////////////////////

        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {

                    page += 1

                    viewModel.getPopularTv(apiKey, language, page)
                        .observe(viewLifecycleOwner, { popularTVRes ->

                            if (popularTVRes.status == Status.LOADING) { ///////////////////////////

                                viewModel.newPageLoading.postValue(true)

                            } else if (popularTVRes.status == Status.SUCCESS) { ////////////////////

                                val popularTV = popularTVRes.data
                                val results = popularTV?.results

                                if (popularTV != null && results != null) {

                                    popularTvAdapter.addItem(results)

                                }

                                viewModel.newPageLoading.postValue(false)

                            } else if (popularTVRes.status == Status.ERROR) { //////////////////////

                                MaterialAlertDialogBuilder(
                                    requireContext(), R.style.MaterialAlertDialog_Rounded
                                )
                                    .setTitle("Hata")
                                    .setMessage("Bağlanılamadı!")
                                    .setCancelable(false)
                                    .setPositiveButton("Tamam", null)
                                    .show()

                            }

                        })

                }

            }
        }

        binding.popularTvRecycler.addOnScrollListener(scrollListener)

        ////////////////////////////////////////////////////////////////////////////////////////////

        startPeriodicalData()

        ////////////////////////////////////////////////////////////////////////////////////////////

        binding.allDelete.setOnClickListener(View.OnClickListener {

            MaterialAlertDialogBuilder(
                requireContext(), R.style.MaterialAlertDialog_Rounded
            )
                .setTitle("Favoriler Siliniyor")
                .setMessage("Tüm favorileri silmek istediğinizden emin misiniz?")
                .setPositiveButton("Evet") { dialog, which ->

                    viewModel.deleteAllFavoritesDB()

                }.setNegativeButton("Hayır", null)
                .show()

        })

        return binding.root
    }

    ////////////////////////////////////

    fun getFirstData() {

        val liveData = viewModel.getPopularTv(apiKey, language, page)

        val observable = object : Observer<Resource<PopularTv>> {
            override fun onChanged(popularTVRes: Resource<PopularTv>?) {

                if (popularTVRes != null) {

                    if (popularTVRes.status == Status.LOADING) { ///////////////////////////////////

                        viewModel.loading.postValue(true)

                    } else if (popularTVRes.status == Status.SUCCESS) { ////////////////////////////

                        val popularTV = popularTVRes.data
                        val results = popularTV?.results

                        lastPopularTvList = results

                        if (popularTV != null && results != null) {

                            val favoriteList = ArrayList<FavoriteTv>()

                            popularTvAdapter =
                                PopularTvAdapter(
                                    results,
                                    favoriteList,
                                    viewModel,
                                    requireActivity().supportFragmentManager
                                )
                            binding.popularTvRecycler.layoutManager =
                                LinearLayoutManager(requireContext())
                            binding.popularTvRecycler.adapter = popularTvAdapter

                            ////////////////////////////////////////////////////////////////////////

                            viewModel.getFavoritesDB()?.observe(viewLifecycleOwner, Observer {

                                popularTvAdapter.changeDB(it as ArrayList<FavoriteTv>)

                            })

                            ////////////////////////////////////////////////////////////////////////

                            viewModel.loading.postValue(false)

                            liveData.removeObserver(this)

                        }

                    } else if (popularTVRes.status == Status.ERROR) { //////////////////////////////

                        MaterialAlertDialogBuilder(
                            requireContext(), R.style.MaterialAlertDialog_Rounded
                        )
                            .setTitle("Hata")
                            .setMessage("Bağlanılamadı!")
                            .setCancelable(false)
                            .setPositiveButton("Tamam", null)
                            .show()

                    }
                }

            }

        }

        liveData.observe(viewLifecycleOwner, observable)

    }

    ////////////////////////////////////

    private fun startPeriodicalData() {

        val t = Timer()

        t.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {

                    ////////////////////////////////////////////////////////////////////////////////

                    Handler(Looper.getMainLooper()).post(Runnable {

                        val liveData = viewModel.getPopularTv(apiKey, language, page)

                        val observable = object : Observer<Resource<PopularTv>> {
                            override fun onChanged(popularTVRes: Resource<PopularTv>?) {

                                if (popularTVRes != null) {

                                    if (popularTVRes.status == Status.SUCCESS) { ///////////////////////

                                        val popularTV = popularTVRes.data
                                        val results = popularTV?.results

                                        if (results != null) {

                                            if (!(lastPopularTvList?.containsAll(results)!!)) {

                                                binding.refresh.visibility = View.VISIBLE

                                                binding.refresh.setOnClickListener(View.OnClickListener {

                                                    binding.popularTvRecycler.smoothScrollToPosition(
                                                        0
                                                    )

                                                    binding.popularTvRecycler.addOnScrollListener(
                                                        object :
                                                            RecyclerView.OnScrollListener() {

                                                            override fun onScrollStateChanged(
                                                                recyclerView: RecyclerView,
                                                                newState: Int
                                                            ) {
                                                                super.onScrollStateChanged(
                                                                    recyclerView,
                                                                    newState
                                                                )

                                                                if (newState == 0 && binding.popularTvRecycler.computeVerticalScrollOffset() == 0) {

                                                                    page = 1

                                                                    getFirstData()

                                                                    binding.refresh.setOnClickListener(
                                                                        null
                                                                    )

                                                                    binding.refresh.visibility =
                                                                        View.GONE

                                                                    binding.popularTvRecycler.removeOnScrollListener(
                                                                        this
                                                                    )

                                                                }

                                                            }

                                                        })

                                                })

                                            }

                                        }

                                        ////////////////////////////////////////////////////////////////////////

                                        liveData.removeObserver(this)

                                    }
                                }
                            }
                        }

                        liveData.observe(viewLifecycleOwner, observable)

                    })

                    ////////////////////////////////////////////////////////////////////////////////

                }
            }, 60 * 1000, 60 * 1000
        )

    }

}