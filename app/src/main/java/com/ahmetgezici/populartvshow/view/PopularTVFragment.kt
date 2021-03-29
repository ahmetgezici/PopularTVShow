package com.ahmetgezici.populartvshow.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ahmetgezici.populartvshow.api.ApiClient
import com.ahmetgezici.populartvshow.databinding.FragmentPopularTvBinding
import com.ahmetgezici.populartvshow.utils.datautil.Status
import com.ahmetgezici.populartvshow.viewmodel.PopularTVViewModel

class PopularTVFragment : Fragment() {

    val TAG = "aaa"

    private lateinit var binding: FragmentPopularTvBinding

    private lateinit var viewModel: PopularTVViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPopularTvBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(PopularTVViewModel::class.java)

        ////////////////////////////////////////////////////////////////////////////////////////////

        val apiKey = ApiClient.apiKey
        val language = "tr-TR"
        val page = 1

        viewModel.getPopularTv(apiKey, language, page).observe(viewLifecycleOwner, { popularTVRes ->

            if (popularTVRes.status == Status.LOADING) {


            } else if (popularTVRes.status == Status.SUCCESS) {

                val popularTV = popularTVRes.data
                val result = popularTV?.results



            } else if (popularTVRes.status == Status.ERROR) {

            }

        })

        return binding.root
    }

}