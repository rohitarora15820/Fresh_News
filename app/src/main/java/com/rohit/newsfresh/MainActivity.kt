package com.rohit.newsfresh

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NewsListAdapter.NewItemClicked {


    private lateinit var adapter:NewsListAdapter


    override fun onItemClicked(item: News) {

        val builder= CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
        val customTabsIntent = builder.build()

        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager=LinearLayoutManager(this)
        fetchData()
        adapter = NewsListAdapter(this)
        recyclerView.adapter=adapter

    }
    private fun fetchData() {
        val url = "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=8ffe99850c6a4e9cadce1809ae13a0cc"
        //making a request
        val jsonObjectRequest = object: JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }

                adapter.updateNews(newsArray)
            },
            Response.ErrorListener {
            }

        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    }



