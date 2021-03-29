package com.ahmetgezici.populartvshow.model.populartv

data class PopularTV(
    val page: Int,
    val results: List<Results>,
    val total_pages: Int,
    val total_results: Int
)
