package com.ahmetgezici.populartvshow.model.populartv

data class PopularTv(
    val page: Int,
    val results: ArrayList<Results>,
    val total_pages: Int,
    val total_results: Int
)
