package com.ahmetgezici.populartvshow.repository

import androidx.lifecycle.MutableLiveData
import com.ahmetgezici.populartvshow.api.ApiClient
import com.ahmetgezici.populartvshow.api.ApiInterface
import com.ahmetgezici.populartvshow.model.details.Details
import com.ahmetgezici.populartvshow.utils.datautil.Resource
import io.reactivex.rxjava3.schedulers.Schedulers

class DetailsRepository {

    var apiInterface: ApiInterface = ApiClient.getClient()!!.create(ApiInterface::class.java)

    ////////////////////////////////////////////////////////////////////////////////////////////////

    // Get Details

    fun getDetails(
        id: Int,
        apiKey: String,
        language: String
    ): MutableLiveData<Resource<Details>> {

        val liveData = MutableLiveData<Resource<Details>>()

        liveData.postValue(Resource.loading())

        apiInterface.getDetails(id, apiKey, language)
            .subscribeOn(Schedulers.io())
            .subscribe({ detailsRes ->
                liveData.postValue(Resource.success(detailsRes))
            }, { throwable ->
                liveData.postValue(Resource.error(throwable.message!!))
            }).isDisposed

        return liveData
    }

}