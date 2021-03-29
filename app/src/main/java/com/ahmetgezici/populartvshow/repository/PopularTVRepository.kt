package com.ahmetgezici.populartvshow.repository

import androidx.lifecycle.MutableLiveData
import com.ahmetgezici.populartvshow.api.ApiClient
import com.ahmetgezici.populartvshow.api.ApiInterface
import com.ahmetgezici.populartvshow.model.populartv.PopularTV
import com.ahmetgezici.populartvshow.utils.datautil.Resource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class PopularTVRepository {

    var apiInterface: ApiInterface = ApiClient.getClient()!!.create(ApiInterface::class.java)

    ////////////////////////////////////////////////////////////////////////////////////////////////

    fun getPopularTv(
        apiKey: String,
        language: String,
        page: Int
    ): MutableLiveData<Resource<PopularTV>> {

        val liveData = MutableLiveData<Resource<PopularTV>>()

        liveData.postValue(Resource.loading())

        apiInterface.getPopularTV(apiKey, language, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ reposList ->
                liveData.postValue(Resource.success(reposList))
            }, { throwable ->
                liveData.postValue(Resource.error(throwable.message!!))
            }).isDisposed

        return liveData
    }

}