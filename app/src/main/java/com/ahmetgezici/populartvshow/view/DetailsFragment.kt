package com.ahmetgezici.populartvshow.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ahmetgezici.populartvshow.api.ApiClient
import com.ahmetgezici.populartvshow.databinding.FragmentDetailsBinding
import com.ahmetgezici.populartvshow.utils.datautil.Status
import com.ahmetgezici.populartvshow.viewmodel.DetailsViewModel

class DetailsFragment(val tvId: Int) : Fragment() {

    val TAG = "aaa"

    private lateinit var binding: FragmentDetailsBinding

    private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)

        ////////////////////////////////////////////////////////////////////////////////////////////

        viewModel.loading.observe(viewLifecycleOwner, {
            if (it) {
                binding.loadingProgress.visibility = View.VISIBLE
                binding.detailsLayout.visibility = View.INVISIBLE
            } else {
                binding.loadingProgress.visibility = View.GONE
                binding.detailsLayout.visibility = View.VISIBLE
            }
        })

        ////////////////////////////////////////////

        val apiKey = ApiClient.apiKey
        val language = "tr-TR"

        viewModel.getDetails(tvId, apiKey, language).observe(viewLifecycleOwner, { detailsRes ->

            if (detailsRes.status == Status.LOADING) { ///////////////////////////////////

            } else if (detailsRes.status == Status.SUCCESS) { ////////////////////////////

            } else if (detailsRes.status == Status.ERROR) { //////////////////////////////

            }

        })


        return binding.root
    }

}