package com.example.app_covid19_konrad

import android.app.DownloadManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity // dodałem stworzoną klasę, któą zimportowałem w dependencies
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    lateinit var worlCasesTV: TextView
    lateinit var worldRecoveredTV: TextView
    lateinit var worldDeathsTV: TextView

    lateinit var countryCasesTV: TextView
    lateinit var countryRecoveredTV: TextView
    lateinit var countryDeathsTV: TextView
    lateinit var countryNameTV: TextView

    lateinit var stateRV: RecyclerView
    lateinit var stateRVAdapter: StateRVAdapter
    lateinit var stateList: List<StateModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        worlCasesTV = findViewById(R.id.idTVWorldCases)
        worldDeathsTV = findViewById(R.id.idTVWorldDeaths)
        worldRecoveredTV = findViewById(R.id.idTVWorldRecovered)

        countryCasesTV = findViewById(R.id.idTVIndianCases)
        countryDeathsTV = findViewById(R.id.idTVIndianDeaths)
        countryRecoveredTV = findViewById(R.id.idTVIndianRecovered)

        stateRV = findViewById(R.id.idRVStates)
        stateList = ArrayList<StateModel>()
    }

    private fun getStateInfo() // tu podaje zrodło API
    {
        //val url = "https://api.covid19india.org/data.json"
        val url = "https://api.rootnet.in/covid19-in/stats/latest"
        val queue = Volley.newRequestQueue(this@MainActivity)
        val request =
            JsonObjectRequest(Request.Method.GET,url,null,{ response ->
                                                          try { // tu pobieram konkretne dane z API, które chcę wyswietlić
                                                              val dataObj = response.getJSONObject("data")
                                                              val summaryObj = dataObj.getJSONObject("summary")
                                                              val cases: Int = summaryObj.getInt("total")
                                                              val deaths: Int = summaryObj.getInt("deaths")
                                                              val recovered: Int = summaryObj.getInt("discharged")

                                                              countryCasesTV.text = cases.toString()
                                                              countryRecoveredTV.text = recovered.toString()
                                                              countryDeathsTV.text = deaths.toString()

                                                              val regionalArray = dataObj.getJSONObject("regional") // pobranie danych regionalnie
                                                              for (i in 0 until regionalArray.length()) {
                                                                  val regionalObj = regionalArray.getJSONObject(i.toString())
                                                                  val stateModel = StateModel(
                                                                      regionalObj.getString("loc"),
                                                                      regionalObj.getInt("discharged"),
                                                                      regionalObj.getInt("deaths"),
                                                                      regionalObj.getInt("totalConfirmed")
                                                                  )
                                                                  stateList += stateModel
                                                              }
                                                              stateRVAdapter = StateRVAdapter(stateList)
                                                              stateRV.layoutManager = LinearLayoutManager(this)
                                                              stateRV.adapter = stateRVAdapter


                                                          }catch (e: JSONException)
                                                          {
                                                              e.printStackTrace()

                                                          }
            }, { error ->
                {
                    Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT)
                }
            })
        queue.add(request)
    }
    private fun getWorldInfo()
    {

    }
}
