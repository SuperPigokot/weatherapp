package com.example.myapplication

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val CITY: String = "Ulyanovsk, ru"
    val API: String ="8e111e527dd99a6d0e69b8a945da397f"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //weatherTask().excute()
    }

    inner class weatherTask(): AsyncTask<String, Void, String>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility= View.VISIBLE
            findViewById<RelativeLayout>(R.id.maimContainer).visibility=View.GONE
            findViewById<TextView>(R.id.error_text).visibility=View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=8e111e527dd99a6d0e69b8a945da397f").readText(Charsets.UTF_8)

            }
            catch (e:Exception){
                response = null

            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObject = JSONObject(result)
                val main = jsonObject.getJSONObject("main")
                val sys = jsonObject.getJSONObject("sys")
                val wind = jsonObject.getJSONObject("wind")
                val weather = jsonObject.getJSONObject("weather").getJSONObject(0.toString())
                val updatedAt:Long = jsonObject.getLong("dt")
                val updatedAtText = "Updated at:"+SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
                val temp = main.getString("temp")+"°C"
                val tempMin = "Min temp: "+main.getString("temp_min")+"°C"
                val tempMax = "Max temp: "+main.getString("temp_max")+"°C"
                val pressure = main.getString("pressure")
                val uf = main.getString("uf")
                val moon = main.getString("moon")
                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val humidity = main.getString("humidity")
                val weatherDescription = weather.getString("description")
                val address = jsonObject.getString("name")+", "+sys.getString("country")

            } catch (e: NumberFormatException) {

            }
        }
    }
}