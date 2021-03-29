package com.ahmetgezici.populartvshow.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahmetgezici.populartvshow.api.ApiClient
import com.ahmetgezici.populartvshow.api.ApiInterface
import com.ahmetgezici.populartvshow.database.FavoriteTvDao
import com.ahmetgezici.populartvshow.database.FavoriteTvDatabase
import com.ahmetgezici.populartvshow.model.database.FavoriteTv
import com.ahmetgezici.populartvshow.model.populartv.PopularTv
import com.ahmetgezici.populartvshow.utils.datautil.Resource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class PopularTvRepository(application: Application) {

    var apiInterface: ApiInterface = ApiClient.getClient()!!.create(ApiInterface::class.java)

    var favoriteTvDao: FavoriteTvDao? = null

    init {

        val favoriteTvDatabase = FavoriteTvDatabase.getDatabase(application)
        favoriteTvDao = favoriteTvDatabase.favoritesDao()

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    fun getPopularTv(
        apiKey: String,
        language: String,
        page: Int
    ): MutableLiveData<Resource<PopularTv>> {

        val liveData = MutableLiveData<Resource<PopularTv>>()

        liveData.postValue(Resource.loading())

        apiInterface.getPopularTV(apiKey, language, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ popularTvList ->
                liveData.postValue(Resource.success(popularTvList))
            }, { throwable ->
                liveData.postValue(Resource.error(throwable.message!!))
            }).isDisposed

        return liveData
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////


    fun addFavoriteTvDB(favoriteTv: FavoriteTv) {

        FavoriteTvDatabase.databaseExecutor.execute(Runnable {

            favoriteTvDao?.addFavoriteTv(favoriteTv)

        })

    }

    //////////

    fun getFavoritesDB(): LiveData<List<FavoriteTv>>? {

        return favoriteTvDao?.getFavorites()

    }

    //////////

    fun deleteAllFavoritesDB() {

        FavoriteTvDatabase.databaseExecutor.execute(Runnable {

            favoriteTvDao?.deleteAllFavorites()

        })

    }

    //////////

    fun deleteFavoriteDB(id: Int) {

        FavoriteTvDatabase.databaseExecutor.execute(Runnable {

            favoriteTvDao?.deleteFavoriteTv(id)

        })

    }

}