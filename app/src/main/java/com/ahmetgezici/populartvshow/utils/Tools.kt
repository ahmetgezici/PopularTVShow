package com.ahmetgezici.populartvshow.utils

import java.text.SimpleDateFormat
import java.util.*

class Tools {

    companion object{

        fun convertDate(date: String): String {

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("tr", "TR"))

            val newDate: Date? = simpleDateFormat.parse(date)
            val newDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale("tr", "TR"))

            return newDateFormat.format(newDate)
        }

    }

}