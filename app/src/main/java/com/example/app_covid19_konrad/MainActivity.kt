package com.example.app_covid19_konrad


import android.content.Intent

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {


    lateinit var worlCasesTV: TextView
    lateinit var worldRecoveredTV: TextView
    lateinit var worldDeathsTV: TextView

    lateinit var countryCasesTV: TextView
    lateinit var countryRecoveredTV: TextView
    lateinit var countryDeathsTV: TextView

    lateinit var stateRV: RecyclerView
    lateinit var stateRVAdapter: StateRVAdapter
    lateinit var stateList: List<StateModel>




    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resetButton: Button = findViewById(R.id.reset_button) //dodanie przycisku do resetu
        val expandCollapseButton: Button = findViewById(R.id.expand_collapse_button)
        var isListExpanded = false
        //expandCollapseButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN))

        resetButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        expandCollapseButton.setOnClickListener {
            if (isListExpanded) {
                // Collapse the list
                stateRV.visibility = View.GONE
                expandCollapseButton.text = "Expand"
                expandCollapseButton.animate()
                    .setDuration(200) // Duration of the animation in milliseconds
                    .withEndAction {
                        // Change the button's background color to green after the animation ends
                        expandCollapseButton.setBackgroundColor(Color.GREEN)
                    }
                    .start()
            } else {
                // Expand the list
                stateRV.visibility = View.VISIBLE
                expandCollapseButton.text = "Collapse"
                expandCollapseButton.animate()
                    .setDuration(200) // Duration of the animation in milliseconds
                    .withEndAction {
                        // Change the button's background color to green after the animation ends
                        expandCollapseButton.setBackgroundColor(Color.RED)
                    }
                    .start()
            }
            isListExpanded = !isListExpanded
            // Animate the button's background color


        }

        worlCasesTV = findViewById(R.id.idTVWorldCases)
        worldDeathsTV = findViewById(R.id.idTVWorldDeaths)
        worldRecoveredTV = findViewById(R.id.idTVWorldRecovered)

        countryCasesTV = findViewById(R.id.idTVIndianCases)
        countryDeathsTV = findViewById(R.id.idTVIndianDeaths)
        countryRecoveredTV = findViewById(R.id.idTVIndianRecovered)

        stateRV = findViewById(R.id.idRVStates)
        stateList = ArrayList<StateModel>()

        findViewById<Button>(R.id.get_data_btn_reg).setOnClickListener {
            getStateInfo()// kiedy klikne pobieraj sie informacje o
        }
        findViewById<Button>(R.id.get_data_btn_wor).setOnClickListener {
            getWorldInfo()// kiedy klikne pobieraj sie informacje o
        }


    }

    private fun getStateInfo() // tu podaje zrodło API
    {
        //val url = "https://api.covid19india.org/data.json"
        val url = "https://api.rootnet.in/covid19-in/stats/latest"
        val queue = Volley.newRequestQueue(this@MainActivity)
        val request =
            JsonObjectRequest(Request.Method.GET, url, null, { response ->
                try { // tu pobieram konkretne dane z API, które chcę wyswietlić
                    val dataObj = response.getJSONObject("data")
                    val summaryObj = dataObj.getJSONObject("summary")
                    val cases: Int = summaryObj.getInt("total")
                    val deaths: Int = summaryObj.getInt("deaths")
                    val recovered: Int = summaryObj.getInt("discharged")

                    countryCasesTV.text = cases.toString()
                    countryRecoveredTV.text = recovered.toString()
                    countryDeathsTV.text = deaths.toString()

                    val regionalArray =
                        dataObj.getJSONArray("regional") // pobranie danych regionalnie
                    for (i in 0 until regionalArray.length()) {
                        val regionalObj = regionalArray[i] as JSONObject // castowanie do JSONObject]
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

                    Log.w("MainActivity", "onCreate:")

                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            }, { error ->
                {
                    Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT)
                }
            })
        queue.add(request)
    }

    private fun getWorldInfo() // podobnie jak powyżej uzupełnić funkcje (poszukuje odpowiedniego Api)
    {

    }


    override fun onStop() {
        super.onStop()
        // Zapisz dane do bazy danych lub pliku

    }

    override fun onDestroy() {
        super.onDestroy()
        // Zwolnij zasoby

    }



     // usun ta petle
}
