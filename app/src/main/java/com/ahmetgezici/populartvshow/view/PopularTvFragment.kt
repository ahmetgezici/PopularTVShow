package com.ahmetgezici.populartvshow.view

import android.os.Bundle
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
import com.ahmetgezici.populartvshow.utils.datautil.Resource
import com.ahmetgezici.populartvshow.utils.datautil.Status
import com.ahmetgezici.populartvshow.viewmodel.PopularTvViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PopularTvFragment : Fragment() {

    val TAG = "aaa"

    private lateinit var binding: FragmentPopularTvBinding

    private lateinit var viewModel: PopularTvViewModel

    lateinit var popularTvAdapter: PopularTvAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPopularTvBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(PopularTvViewModel::class.java)

        ////////////////////////////////////////////////////////////////////////////////////////////

        val apiKey = ApiClient.apiKey
        val language = "tr-TR"
        var page = 1

        val observable = object : Observer<Resource<PopularTv>> {
            override fun onChanged(popularTVRes: Resource<PopularTv>?) {

                if (popularTVRes != null) {

                    if (popularTVRes.status == Status.LOADING) { ///////////////////////////////////


                    } else if (popularTVRes.status == Status.SUCCESS) { ////////////////////////////

                        val popularTV = popularTVRes.data
                        val results = popularTV?.results

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

                            viewModel.getPopularTv(apiKey, language, page).removeObserver(this)

                        }


                    } else if (popularTVRes.status == Status.ERROR) { //////////////////////////////

                    }
                }

            }

        }

        viewModel.getPopularTv(apiKey, language, page).observe(viewLifecycleOwner, observable)


        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {

                    page += 1

                    viewModel.getPopularTv(apiKey, language, page)
                        .observe(viewLifecycleOwner, { popularTVRes ->

                            if (popularTVRes.status == Status.LOADING) { ///////////////////////////


                            } else if (popularTVRes.status == Status.SUCCESS) { ////////////////////

                                val popularTV = popularTVRes.data
                                val results = popularTV?.results

                                if (popularTV != null && results != null) {

                                    popularTvAdapter.addItem(results)

                                }

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


//        binding.popularTvRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                Log.e(TAG, "onScrolled: $dy")
//
//            }
//        })


        return binding.root
    }

}