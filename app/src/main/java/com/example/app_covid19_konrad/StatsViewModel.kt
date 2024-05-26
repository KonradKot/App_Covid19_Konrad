package com.example.app_covid19_konrad

import androidx.lifecycle.ViewModel
import com.example.app_covid19_konrad.CountryStats

class StatsViewModel : ViewModel() {
    private val repository = Covid19Repository()

    suspend fun getCountryStats(): List<CountryStats> {
        // Pobierz dane z API lub innego źródła
        return listOf(
            CountryStats("Poland", 1000, 500, 100),
            CountryStats("Germany", 2000, 1000, 200),
            CountryStats("France", 3000, 1500, 300)
        )
    }
}