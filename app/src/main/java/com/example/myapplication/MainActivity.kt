package com.example.myapplication
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.TextUtils.concat
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

//test§
class MainActivity : AppCompatActivity(), LocationListener {
    private val locationPermissionCode = 2
    private var long: Double? = null
    private var lat: Double? = null
    private val API: String = "8e111e527dd99a6d0e69b8a945da397f"
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getLocation()
        getResult()
        getAdditionalResult()
    }
    private fun getLocation() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        val providers = locationManager.getProviders(true)
        for (provider in providers)
        {
            val location = locationManager.getLastKnownLocation(provider)
            if (location != null) {
                this.long = location.longitude
                this.lat = location.latitude
                break
            }  else {
                this.long = 48.3978
                this.lat = 54.3187
                continue
            }
        }
    }
    fun openUvi(view: View) {
        val intentUviActivity = Intent(this, UviActivity::class.java)
        val uvIndexLabel: TextView = findViewById(R.id.uv_index_value)
        intentUviActivity.putExtra("uvi_value", uvIndexLabel.text.toString())
        startActivity(intentUviActivity)
    }

    fun openOther(view: View) {
        var element_id = view.id
        var linearView : LinearLayout = findViewById(element_id)
        var childCount = linearView.childCount
        var otherText = ""
        for (index in 0 .. childCount) {
            var child = linearView.getChildAt(index)
            if (child != null) {
                var childText: TextView? = findViewById(child.id)
                if (childText != null) {
                    otherText = concat(otherText, childText.text.toString()) as String
                    otherText = concat(otherText, " ") as String
                }
            }
        }
        if (!otherText.isEmpty()) {
            val intentOverActivity = Intent(this, OtherActivity::class.java)
            intentOverActivity.putExtra("text", otherText)
            startActivity(intentOverActivity)
        }
    }

    override fun onLocationChanged(location: Location) {
        this.long = location.longitude
        this.long = location.latitude
    }

    private fun getResult() {
        val url = "https://api.openweathermap.org/data/2.5/onecall" +
                "?lat=${this.lat}" +
                "&lon=${this.long}" +
                "&exclude=daily,minutely,hourly,alerts" +
                "&appid=$API" +
                "&lang=ru" +
                "&units=metric"
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, url,
            {
                response-> setResult(response, "main")
            },
            {
                Log.d("MyLog", "VolleyError: $it")
            }
        )
        queue.add(stringRequest)
    }

    private fun getAdditionalResult() {
        val url = "https://api.openweathermap.org/data/2.5/onecall" +
                "?lat=${this.lat}" +
                "&lon=${this.long}" +
                "&exclude=current,minutely,hourly,alerts" +
                "&appid=$API" +
                "&lang=ru" +
                "&units=metric"
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, url,
            {
                    response-> setResult(response, "additional")
            },
            {
                Log.d("MyLog", "VolleyError: $it")
            }
        )
        queue.add(stringRequest)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateTime(s: String): String? {
        return try {
            val sdf = SimpleDateFormat("HH:m")
            val netDate = Date(s.toLong() * 1000)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }

    private fun getCityName(city: String): String {
        val cityArray = city.split("/")
        return cityArray.last()
    }

    @SuppressLint("SetTextI18n")
    private fun setResult(response: String, type: String) {
        if (type == "main") {
            val responseObj = JSONObject(response)
            val currentWeather = responseObj.getJSONObject("current")
            val currentWeatherDescriptionArray = currentWeather.getJSONArray("weather")
            val currentWeatherDescription = currentWeatherDescriptionArray.getJSONObject(0)
            val sunsetLabel: TextView = findViewById(R.id.sunset_value)
            val sunriseLabel: TextView = findViewById(R.id.sunrise_value)
            val weatherLabel: TextView = findViewById(R.id.current_weather)
            val pressureLabel: TextView = findViewById(R.id.pressure_value)
            val windLabel: TextView = findViewById(R.id.wind_value)
            val statusLabel: TextView = findViewById(R.id.status)
            sunsetLabel.text = getDateTime((currentWeather.getInt("sunset") + responseObj.getInt("timezone_offset")).toString())
            sunriseLabel.text = getDateTime((currentWeather.getInt("sunrise") + responseObj.getInt("timezone_offset")).toString())
            pressureLabel.text = currentWeather.getString("pressure")
            windLabel.text = currentWeather.getString("wind_speed")
            weatherLabel.text = "Текущая температура:" + "\n" + currentWeather.getString("temp") + " " + "°C"
            statusLabel.text = currentWeatherDescription.getString("main")

        } else if (type == "additional") {
            val responseObj = JSONObject(response)
            val dailyWeatherArray = responseObj.getJSONArray("daily")
            val dailyWeather = dailyWeatherArray.getJSONObject(0)
            val dailyTemp = dailyWeather.getJSONObject("temp")
            val moonPhaseLabel: TextView = findViewById(R.id.moon_phase_value)
            val tempMinLabel: TextView = findViewById(R.id.temp_min)
            val tempMaxLabel: TextView = findViewById(R.id.temp_max)
            val cityLabel: TextView = findViewById(R.id.address)
            val uvIndexLabel: TextView = findViewById(R.id.uv_index_value)
            cityLabel.text = getCityName(responseObj.getString("timezone")).replace("_", " ")
            tempMinLabel.text = tempMinLabel.text.toString() + "\n" + dailyTemp.getString("min") + "°C"
            tempMaxLabel.text = tempMaxLabel.text.toString() + "\n" + dailyTemp.getString("max") + "°C"
            moonPhaseLabel.text = dailyWeather.getString("moon_phase")
            uvIndexLabel.text = dailyWeather.getString("uvi")
        }
    }
}