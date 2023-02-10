package com.example.project3_flixster

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONObject

private const val API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed"

class NowPlayingMovieFragment : Fragment(), OnListFragmentInteractionListener {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_now_playing_movies_list, container, false)
        val progressBar = view.findViewById<View>(R.id.progress) as ProgressBar
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val context = view.context
        recyclerView.layoutManager = LinearLayoutManager(context)
        updateAdapter(progressBar, recyclerView)
        return view
    }

    private fun updateAdapter(progressBar: ProgressBar, recyclerView: RecyclerView) {
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val params =RequestParams()
        params["api-key"] = API_KEY

        client[
                "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed",
                params,
                object : JsonHttpResponseHandler() {
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        progressBar.visibility = View.INVISIBLE

                        throwable?.message?.let {
                            Log.e("NowPlayingMovieFragment", response.toString())
                        }
                    }

                    override fun onSuccess(
                        statusCode: Int,
                        headers: Headers?,
                        json: JsonHttpResponseHandler.JSON?
                    ) {
                        progressBar.visibility = View.INVISIBLE

                        val resultsJSON : JSONObject = json?.jsonObject as JSONObject
                        val movieRawJSON :String = resultsJSON.get("results").toString()

                        val gson = Gson()
                        val arrayMovieType = object : TypeToken<List<NowPlayingMovie>>()  {}.type

                        val models : List<NowPlayingMovie> = gson.fromJson(movieRawJSON, arrayMovieType)
                        recyclerView.adapter = NowPlayingMovieRecyclerViewAdapter(models, this@NowPlayingMovieFragment)

                        Log.d("NowPlayingMovieFragment", "response successful")
                    }
                }
        ]

    }

    override fun onItemClick(item: NowPlayingMovie) {
        Toast.makeText(context, "test: " + item.title, Toast.LENGTH_SHORT).show()
    }
}