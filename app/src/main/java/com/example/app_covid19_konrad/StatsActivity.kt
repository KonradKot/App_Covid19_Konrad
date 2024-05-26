package com.example.app_covid19_konrad

import android.os.Bundle
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.view.LineChartView
import lecho.lib.hellocharts.model.PointValue
import lecho.lib.hellocharts.model.LineDataSet
import org.json.JSONException

class StatsActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChartView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        lineChart = findViewById(R.id.lineChart)

        // Pobierz dane o liczbie zachorowań dla każdego kraju UE
        val euCountryStats = getEuCountryStats()
        val lineDataSet = LineDataSet(listOf(PointValue(10f, 20f), PointValue(20f, 30f)))
        // Utwórz listę wartości dla wykresu słupkowego
        val entries = mutableListOf<LineChartData.PointValue>()
        for ((index, countryStats) in euCountryStats.withIndex()) {
            entries.add(LineChartData.PointValue(index.toFloat(), countryStats.cases.toFloat()))
        }

        // Utwórz zestaw danych dla wykresu słupkowego
        val lineDataSet = LineDataSet(entries).apply {
            setColor(Color.BLUE)
            setLineWidth(2f)
        }

        // Utwórz obiekt LineChartData i ustaw zestaw danych
        val lineChartData = LineChartData(listOf(lineDataSet))

        // Set the LineChartData to the LineChartView
        lineChart.lineChartData = lineChartData

        // Refresh the LineChartView
        lineChart.invalidate()
    }

    // Pobierz dane o liczbie zachorowań dla każdego kraju UE
    private fun getEuCountryStats(): List<CountryStats> {
        // Kod pobierania danych z API lub bazy danych
        // ...

            val euCountries = listOf(
                "Austria", "Belgium", "Bulgaria", "Croatia", "Cyprus", "Czech Republic", "Denmark", "Estonia",
                "Finland", "France", "Germany", "Greece", "Hungary", "Ireland", "Italy", "Latvia", "Lithuania",
                "Luxembourg", "Malta", "Netherlands", "Poland", "Portugal", "Romania", "Slovakia", "Slovenia",
                "Spain", "Sweden"
            )

            val url = "https://covid-193.p.rapidapi.com/statistics"

            val queue = Volley.newRequestQueue(this)

            val request = object : JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    try {
                        val regionalArray = response.getJSONArray("response")

                        val countryStats = mutableListOf<CountryStats>()

                        for (i in 0 until 238) {
                            val regionalObj = regionalArray.getJSONObject(i)
                            val stateName = regionalObj.getString("country")
                            val continentName = regionalObj.getString("continent")

                            if (stateName in euCountries && continentName == "Europe") {
                                val cases: Int = regionalObj.getJSONObject("cases").getLong("total").toInt()
                                val deaths: Int = regionalObj.getJSONObject("deaths").getLong("total").toInt()
                                val recovered: Int = try {
                                    regionalObj.getJSONObject("cases").getLong("recovered").toInt()
                                } catch (e: JSONException) {
                                    0
                                }
                                countryStats.add(CountryStats(stateName, cases, deaths, recovered))
                            }
                        }

                        //return countryStats
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        // emptyList()
                    }
                },
                { error ->
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    //return emptyList()
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["X-RapidAPI-Key"] = "9a9c2ee830msh1d67666a060a413p1b0636jsnc4f6d9b68998"
                    headers["X-RapidAPI-Host"] = "covid-193.p.rapidapi.com"
                    return headers
                }
            }

            queue.add(request)

            return emptyList() // Return an empty list if there is an error or no data is available

        // Zwróć listę obiektów CountryStats
        return listOf(
            CountryStats("Austria", 10000, 500, 8000),
            CountryStats("Belgium", 15000, 700, 12000),
            // ...
        )
    }
}
