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

    val loading = MutableLiveData<Boolean>()

    val newPageLoading = MutableLiveData<Boolean>()

    ///////////

    // Get Popular TV Show

    fun getPopularTv(
        apiKey: String,
        language: String,
        page: Int
    ): MutableLiveData<Resource<PopularTv>> {

        return repository.getPopularTv(apiKey, language, page)

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Database

    // Add Favorite

    fun addFavoriteTvDB(favoriteTv: FavoriteTv) {
        repository.addFavoriteTvDB(favoriteTv)
    }

    /////////

    // Get Favorites DB

    fun getFavoritesDB(): LiveData<List<FavoriteTv>>? {
        return repository.getFavoritesDB()
    }

    /////////

    // Delete All DB

    fun deleteAllFavoritesDB() {
        repository.deleteAllFavoritesDB()
    }

    /////////

    // Delete Favorite DB

    fun deleteFavoriteDB(id: Int) {
        repository.deleteFavoriteDB(id)
    }

}