package com.ahmetgezici.populartvshow.adapter

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmetgezici.populartvshow.R
import com.ahmetgezici.populartvshow.api.ApiClient
import com.ahmetgezici.populartvshow.databinding.ItemPopularTvBinding
import com.ahmetgezici.populartvshow.model.database.FavoriteTv
import com.ahmetgezici.populartvshow.model.populartv.Results
import com.ahmetgezici.populartvshow.viewmodel.PopularTvViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.like.LikeButton
import com.like.OnLikeListener


class PopularTvAdapter(
    private val tvShowList: ArrayList<Results>,
    private var favoriteList: ArrayList<FavoriteTv>,
    private val viewModel: PopularTvViewModel
) : RecyclerView.Adapter<PopularTvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPopularTvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val tvShow = tvShowList[position]

        holder.binding.name.text = tvShow.name
        holder.binding.overview.text = tvShow.overview

        Glide.with(holder.itemView.context)
            .load(ApiClient.ImageUrl_w500 + tvShow.poster_path)
            .placeholder(R.drawable.placeholder)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .into(holder.binding.image)

        val favoriteTv = FavoriteTv()
        favoriteTv.uid = tvShow.id

        holder.binding.star.isLiked = favoriteList.contains(favoriteTv)

        holder.binding.star.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {

                Handler(Looper.getMainLooper()).postDelayed(Runnable {

                    viewModel.addFavoriteRepoDB(favoriteTv)

                }, 600)

            }

            override fun unLiked(likeButton: LikeButton) {

                    viewModel.deleteFavoriteDB(tvShow.id)
            }
        })

    }

    override fun getItemCount(): Int {
        return tvShowList.size
    }


    class ViewHolder(val binding: ItemPopularTvBinding) : RecyclerView.ViewHolder(binding.root)


    fun addItem(newTvShowList: ArrayList<Results>) {

        tvShowList.addAll(newTvShowList)
        notifyDataSetChanged()

    }

    fun changeDB(newFavoriteList: ArrayList<FavoriteTv>) {

        favoriteList = newFavoriteList
        notifyDataSetChanged()

    }

}