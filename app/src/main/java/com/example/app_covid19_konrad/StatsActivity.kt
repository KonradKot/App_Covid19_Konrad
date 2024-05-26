package com.example.app_covid19_konrad

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import com.example.app_covid19_konrad.StatsViewModel
import lecho.lib.hellocharts.view.ColumnChartView

class StatsActivity : AppCompatActivity() {
    private val viewModel: StatsViewModel by viewModels()

    @Composable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        // Initialize the chart
        val chart = findViewById<ColumnChartView>(R.id.chart)

        // Observe the stats data
        lifecycleScope.launch {
            val stats = viewModel.getCountryStats()

            // Update the chart with the stats data
            val data = stats.map {
                Column(it.country, it.cases.toFloat())
            }
            chart.columns = data
        }
    }
}