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

    lateinit var dateText: TextView



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

        dateText = findViewById(R.id.date_text_view) // dodac przycisk!

        findViewById<Button>(R.id.get_data_btn_reg).setOnClickListener {
            getStateInfo()// kiedy klikne pobieraj sie informacje o

        }
        findViewById<Button>(R.id.get_data_btn_wor).setOnClickListener {
            getWorldInfo()// kiedy klikne pobieraj sie informacje o
        }

        val showImageButton = findViewById<Button>(R.id.image_button)

        // Set a click listener on the button
        showImageButton.setOnClickListener {
            // Create an intent to start the ImageActivity
            val intent = Intent(this, ImageActivity::class.java)

            // Start the ImageActivity
            startActivity(intent)
        }
        val showQuizButton = findViewById<Button>(R.id.quiz_button)
        showQuizButton.setOnClickListener {
            // Create an intent to start the ImageActivity
            val intent = Intent(this,ChartActivity::class.java)

            // Start the ImageActivity
            startActivity(intent)
        }


    }

    private fun getStateInfo() // tu podaje zrodło API
    {
        val euCountries = listOf(
            "Austria", "Belgium", "Bulgaria", "Croatia", "Cyprus", "Czech Republic", "Denmark", "Estonia",
            "Finland", "France", "Germany", "Greece", "Hungary", "Ireland", "Italy", "Latvia", "Lithuania",
            "Luxembourg", "Malta", "Netherlands", "Poland", "Portugal", "Romania", "Slovakia", "Slovenia",
            "Spain", "Sweden"
        ) // kraje EU
        val url = "https://covid-193.p.rapidapi.com/statistics"
        val queue = Volley.newRequestQueue(this@MainActivity)
        val request = object: JsonObjectRequest(Request.Method.GET, url, null, { response ->
                try { // tu pobieram konkretne dane z API, które chcę wyswietlić
                    val dataObj = response.getJSONArray("response").getJSONObject(203)
                    //val summaryObj = dataObj.getJSONObject("summary")
                    val cases: Int = dataObj.getJSONObject("cases").getLong("total").toInt()
                    val deaths: Int = dataObj.getJSONObject("deaths").getLong("total").toInt()
                    val recovered: Int = dataObj.getJSONObject("cases").getLong("recovered").toInt()
                    val date: String = dataObj.getString("day")

                    countryCasesTV.text = cases.toString()
                    countryRecoveredTV.text = recovered.toString()
                    countryDeathsTV.text = deaths.toString()
                    dateText.text = date.toString()


                    val regionalArray =
                        response.getJSONArray("response") // pobranie danych regionalnie

                    for (i in 0 until 238) {
                        val regionalObj = regionalArray.getJSONObject(i)
                        val stateName = regionalObj.getString("country")
                        val continentName = regionalObj.getString("continent")
                        if (stateName in euCountries && continentName == "Europe") { // Check if the state belongs to the EU
                            val cases: Int = regionalObj.getJSONObject("cases").getLong("total").toInt()
                            val deaths: Int = regionalObj.getJSONObject("deaths").getLong("total").toInt()
                            val recovered: Int = try {
                                regionalObj.getJSONObject("cases").getLong("recovered").toInt()
                            } catch (e: JSONException) {
                                0 // Set recovered to 0 if the value is null
                            }
                            val stateModel = StateModel(stateName, cases, deaths, recovered)
                            stateList = stateList + stateModel
                        }
                    }

                    stateRVAdapter = StateRVAdapter(stateList)
                    stateRV.layoutManager = LinearLayoutManager(this)
                    stateRV.adapter = stateRVAdapter

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, { error ->
                Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["X-RapidAPI-Key"] = "9a9c2ee830msh1d67666a060a413p1b0636jsnc4f6d9b68998"
                    headers["X-RapidAPI-Host"] = "covid-193.p.rapidapi.com"
                    return headers
                }
            }
        Log.w("MainActivity", "onCreate:")
        queue.add(request)
    }

    private fun getWorldInfo() {
        val url = "https://covid-193.p.rapidapi.com/statistics?country=all"
        val requestQueue = Volley.newRequestQueue(this@MainActivity)

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                val data = response.getJSONArray("response")
                val cases= data.getJSONObject(0).getJSONObject("cases").getLong("total").toInt()
                val deaths:Int = data.getJSONObject(0).getJSONObject("deaths").getLong("total").toInt()
                val recovered:Int = data.getJSONObject(0).getJSONObject("cases").getLong("recovered").toInt()


                worlCasesTV.text = cases.toString()
                worldDeathsTV.text = deaths.toString()
                worldRecoveredTV.text = recovered.toString()


                Log.w("MainActivity", "onCreate:")

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, { error ->
            Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["X-RapidAPI-Key"] = "9a9c2ee830msh1d67666a060a413p1b0636jsnc4f6d9b68998"
                headers["X-RapidAPI-Host"] = "covid-193.p.rapidapi.com"
                return headers
            }
        }
        Log.d("JSON_DATA", jsonObjectRequest.toString())
        requestQueue.add(jsonObjectRequest)
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


