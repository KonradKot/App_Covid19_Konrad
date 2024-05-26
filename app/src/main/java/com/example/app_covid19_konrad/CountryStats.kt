package com.example.app_covid19_konrad

data class CountryStats(
    val country: String,
    val cases: Int,
    val recovered: Int,
    val deaths: Int
)