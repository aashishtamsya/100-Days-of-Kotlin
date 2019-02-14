package com.aashishtamsya.android.sunrise

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val apiKey = "3d8ff86168b78f5b7cab254d29677f09"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    protected fun getSunrise(view: View) {
        val cityName = editText.text.toString()
        val url = "https://api.openweathermap.org/data/2.5/weather?q=${cityName}&apiKey=${apiKey}"
        MyAsyncTask().execute(url)
    }

    inner class MyAsyncTask: AsyncTask<String, String, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }
        override fun doInBackground(vararg p0: String?): String {
            try {
                var url = URL(p0[0])
                val urlConnect = url.openConnection() as HttpURLConnection
                urlConnect.connectTimeout = 7000
                var inString = convertStringToString(urlConnect.inputStream)
                publishProgress(inString)
            } catch (exception: Exception) { }
            return ""
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                val sunrise = JSONObject(values[0]).getJSONObject("sys").getLong("sunrise").toString()
                textView.text = "Sunrise time is " + getDateTime(sunrise)
            } catch (exception: Exception) { }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            result?.let {
                Log.i("ON_POST_EXECUTE", result)
            }
        }
    }

    fun convertStringToString(inputStream: InputStream): String {
        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line:String
        var allString = ""
        try {
            do {
                line = bufferReader.readLine()
                if (line != null) allString += line
            } while (line != null)
        } catch (exception: Exception) {
        }

        return allString
    }

    private fun getDateTime(s: String): String? {
        try {
            val sdf = SimpleDateFormat("hh:mm aa")
            val netDate = Date(s.toLong() * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

}
