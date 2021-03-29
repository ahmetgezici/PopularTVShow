package com.ahmetgezici.populartvshow.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ahmetgezici.populartvshow.databinding.FragmentDetailsBinding
import com.ahmetgezici.populartvshow.viewmodel.DetailsViewModel

class DetailsFragment : Fragment() {

    val TAG = "aaa"

    private lateinit var binding: FragmentDetailsBinding

    private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailsBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)



        return binding.root
    }

}