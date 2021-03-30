package com.ahmetgezici.populartvshow.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ahmetgezici.populartvshow.R
import com.ahmetgezici.populartvshow.api.ApiClient
import com.ahmetgezici.populartvshow.databinding.FragmentDetailsBinding
import com.ahmetgezici.populartvshow.model.database.FavoriteTv
import com.ahmetgezici.populartvshow.utils.Tools
import com.ahmetgezici.populartvshow.utils.datautil.Status
import com.ahmetgezici.populartvshow.viewmodel.DetailsViewModel
import com.ahmetgezici.populartvshow.viewmodel.PopularTvViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.like.LikeButton
import com.like.OnLikeListener
import java.text.DecimalFormat
import kotlin.math.roundToInt

class DetailsFragment(
    private val tvId: Int,
    private val favoriteState: Boolean,
    val popularTvViewModel: PopularTvViewModel
) : Fragment() {

    val TAG = "aaa"

    private lateinit var binding: FragmentDetailsBinding

    private lateinit var viewModel: DetailsViewModel

    @SuppressLint("SetTextI18n")
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

            if (detailsRes.status == Status.LOADING) { /////////////////////////////////////////////

                viewModel.loading.postValue(true)

            } else if (detailsRes.status == Status.SUCCESS) { //////////////////////////////////////

                viewModel.detailsLiveData.postValue(detailsRes.data)

            } else if (detailsRes.status == Status.ERROR) { ////////////////////////////////////////

                MaterialAlertDialogBuilder(
                    requireContext(), R.style.MaterialAlertDialog_Rounded
                )
                    .setTitle("Hata")
                    .setMessage("Bağlanılamadı!")
                    .setCancelable(false)
                    .setPositiveButton("Tamam") { dialog, which ->

                        closeFragment(this@DetailsFragment)

                    }
                    .show()

            }

        })

        ////////////////////////////////////////////

        binding.favorite.isLiked = favoriteState

        ////////////////////////////////////////////

        viewModel.detailsLiveData.observe(viewLifecycleOwner, { details ->

            Glide.with(requireContext())
                .load(ApiClient.ImageUrl_original + details.backdrop_path)
                .placeholder(R.drawable.details_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(binding.image)

            ////////////

            binding.name.text = details.name
            binding.overview.text = details.overview
            binding.firstAirDate.text = Tools.convertDate(details.first_air_date)
            binding.type.text = details.type
            binding.totalSeason.text = details.number_of_seasons.toString()
            binding.totalEpisode.text = details.number_of_episodes.toString()
            binding.voteCount.text = details.vote_count.toString()

            ////////////

            if (details.created_by.isNotEmpty()) {
                binding.director.text = details.created_by[0].name
            }

            ////////////

            if (details.production_companies.isNotEmpty()) {
                binding.company.text = details.production_companies[0].name
            }

            ////////////

            if (details.production_countries.isNotEmpty()) {
                binding.country.text = details.production_countries[0].name
            }

            ////////////

            if (details.spoken_languages.isNotEmpty()) {
                binding.language.text = details.spoken_languages[0].name
            }

            ////////////

            var genres = ""

            for (genre in details.genres) {
                genres += "● ${genre.name} "
            }

            binding.genres.text = genres

            ////////////

            if (details.in_production) {
                binding.inProductionStatus.setCardBackgroundColor(Color.parseColor("#AEEA00"))
                binding.inProduction.text = "Çekimde"
            } else {
                binding.inProductionStatus.setCardBackgroundColor(Color.parseColor("#D50000"))
                binding.inProduction.text = "Çekimde Değil"
            }

            ////////////

            if (details.homepage.isNotEmpty()) {
                binding.goWebSite.setOnClickListener(View.OnClickListener {

                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(details.homepage)
                    startActivity(intent)

                })
            }

            ////////////

            val voteAnimator = ValueAnimator.ofFloat(0F, details.vote_average.toFloat())
            voteAnimator.duration = 1750
            voteAnimator.interpolator = DecelerateInterpolator()
            voteAnimator.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->


                val decimalFormat = DecimalFormat(".#")

                val animateValue: Float = animation.animatedValue as Float
                val intValue: Int = animateValue.roundToInt()

                binding.votePrimary.text = intValue.toString()

                val fractions: String = decimalFormat.format(animateValue)

                binding.voteSecondary.text = "," +
                        fractions.subSequence(fractions.indexOf(",") + 1, fractions.length)


            })
            voteAnimator.start()

            ////////////

            val voteCountAnimator = ValueAnimator.ofInt(0, details.vote_count)
            voteCountAnimator.duration = 1750
            voteCountAnimator.interpolator = DecelerateInterpolator()
            voteCountAnimator.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->


                val animateValue: Int = animation.animatedValue as Int

                binding.voteCount.text = "$animateValue Kişi"


            })
            voteCountAnimator.start()

            ////////////

            val anim = ObjectAnimator.ofFloat(
                binding.voteProgress,
                "percent",
                0f,
                details.vote_average.toFloat() * 10f
            )
            anim.interpolator = AccelerateDecelerateInterpolator()
            anim.duration = 1750
            anim.start()

            /////////////

            val favoriteTv = FavoriteTv()
            favoriteTv.uid = details.id

            binding.favorite.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton) {

                    Handler(Looper.getMainLooper()).postDelayed(Runnable {

                        popularTvViewModel.addFavoriteTvDB(favoriteTv)

                    }, 600)

                }

                override fun unLiked(likeButton: LikeButton) {

                    popularTvViewModel.deleteFavoriteDB(details.id)
                }
            })



            viewModel.loading.postValue(false)

        })

        ////////////////////////////////////////////////////////////////////////////////////////////

        // Back Button

        binding.back.setOnClickListener(View.OnClickListener {

            closeFragment(this)

        })

        // Back Pressed

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    closeFragment(this@DetailsFragment)
                }
            })


        return binding.root
    }


    // Close Fragment

    fun closeFragment(fragment: Fragment) {

        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
            .remove(fragment)
            .commit()

    }

}