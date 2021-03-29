package com.ahmetgezici.populartvshow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ahmetgezici.populartvshow.model.populartv.PopularTV
import com.ahmetgezici.populartvshow.repository.PopularTVRepository
import com.ahmetgezici.populartvshow.utils.datautil.Resource

class PopularTVViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PopularTVRepository()

    ////////////////////////////////////////////////////////////////////////////////////////////////

    fun getPopularTv(
        apiKey: String,
        language: String,
        page: Int
    ): MutableLiveData<Resource<PopularTV>> {

        return repository.getPopularTv(apiKey, language, page)

    }

}