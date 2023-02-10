package com.example.project3_flixster

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.util.*
import java.util.logging.Handler
import kotlin.concurrent.schedule

const val startUrl = "https://image.tmdb.org/t/p/w500"

class NowPlayingMovieRecyclerViewAdapter(
    private val movies : List<NowPlayingMovie>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<NowPlayingMovieRecyclerViewAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val mView: View, val context : Context) :RecyclerView.ViewHolder(mView) {
        var mItem : NowPlayingMovie? = null
        val mMovieTitle : TextView = mView.findViewById<View>(R.id.movie_title) as TextView
        val mMovieDes : TextView = mView.findViewById<View>(R.id.movie_description) as TextView
        val mMoviePoster : ImageView = mView.findViewById<View>(R.id.movie_poster) as ImageView

        override fun toString(): String {
            return mMovieTitle.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_now_playing_movie, parent, false)
        return MovieViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        holder.mItem = movie
        holder.mMovieTitle.text = movie.title
        holder.mMovieDes.text = movie.overview
        val orientation = holder.context.resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            holder.mMoviePoster.layoutParams.width = (100*Resources.getSystem().displayMetrics.density).toInt()
            Glide.with(holder.mView)
                .load(startUrl+movie.posterpath)
                .centerInside()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.mMoviePoster)
        }
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {

            holder.mMoviePoster.layoutParams.width = (300*Resources.getSystem().displayMetrics.density).toInt()
            Glide.with(holder.mView)
                .load(startUrl+movie.backdropPath)
                .centerInside()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.mMoviePoster)
        }

        holder.mView.setOnClickListener {
            holder.mItem?.let { movie ->
                mListener?.onItemClick(movie)
            }
        }

    }

    override fun getItemCount(): Int {
        return movies.size
    }
}