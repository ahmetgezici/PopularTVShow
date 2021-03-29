package com.ahmetgezici.populartvshow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahmetgezici.populartvshow.model.database.FavoriteTv
import com.ahmetgezici.populartvshow.model.populartv.PopularTv
import com.ahmetgezici.populartvshow.repository.PopularTvRepository
import com.ahmetgezici.populartvshow.utils.datautil.Resource

class PopularTvViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PopularTvRepository(application)

    ////////////////////////////////////////////////////////////////////////////////////////////////

    fun getPopularTv(
        apiKey: String,
        language: String,
        page: Int
    ): MutableLiveData<Resource<PopularTv>> {

        return repository.getPopularTv(apiKey, language, page)

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Database

    fun addFavoriteRepoDB(favoriteTv: FavoriteTv) {
        repository.addFavoriteTvDB(favoriteTv)
    }

    /////////

    fun getFavoritesDB(): LiveData<List<FavoriteTv>>? {
        return repository.getFavoritesDB()
    }

    /////////

    fun deleteAllFavoritesDB() {
        repository.deleteAllFavoritesDB()
    }

    /////////

    fun deleteFavoriteDB(id: Int) {
        repository.deleteFavoriteDB(id)
    }

}