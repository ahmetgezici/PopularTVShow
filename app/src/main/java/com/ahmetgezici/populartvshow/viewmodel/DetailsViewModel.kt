package com.ahmetgezici.populartvshow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahmetgezici.populartvshow.model.details.Details
import com.ahmetgezici.populartvshow.repository.DetailsRepository
import com.ahmetgezici.populartvshow.utils.datautil.Resource

class DetailsViewModel : ViewModel() {

    private val repository = DetailsRepository()

    ////////////////////////////////////////////////////////////////////////////////////////////////

    val loading = MutableLiveData<Boolean>()

    /////////

    val detailsLiveData = MutableLiveData<Details>()

    ////////

    // Get Details

    fun getDetails(
        id: Int,
        apiKey: String,
        language: String
    ): MutableLiveData<Resource<Details>> {

        return repository.getDetails(id, apiKey, language)

    }

}