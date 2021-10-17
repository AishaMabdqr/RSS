package com.example.rss

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var rvItems : RecyclerView
    lateinit var rvAdapter: RVAdapter

    var topQuestions = mutableListOf<Questions>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         rvItems = findViewById(R.id.rvItems)
//        rvAdapter = RVAdapter(topQuestions)
//        rvItems.adapter = rvAdapter
//        rvItems.layoutManager = LinearLayoutManager(this)

        fetchQuestions().execute()
    }

    private inner class fetchQuestions : AsyncTask<Void, Void, MutableList<Questions>>() {
        val parser = XMLParser()
        override fun doInBackground(vararg params: Void?): MutableList<Questions> {
            val url = URL("https://stackoverflow.com/feeds")
            val urlConnection = url.openConnection() as HttpURLConnection
            topQuestions =
                urlConnection.getInputStream()?.let {
                    parser.parse(it)
                } as MutableList<Questions>

            return topQuestions
            rvAdapter.notifyDataSetChanged()
        }

        override fun onPostExecute(result: MutableList<Questions>?) {
            super.onPostExecute(result)

            rvAdapter = RVAdapter(topQuestions)
            rvItems.adapter = rvAdapter
            rvItems.layoutManager = LinearLayoutManager(this@MainActivity)
            rvAdapter.notifyDataSetChanged()
        }

    }
}