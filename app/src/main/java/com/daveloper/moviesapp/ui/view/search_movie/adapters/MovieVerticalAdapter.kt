package com.daveloper.moviesapp.ui.view.search_movie.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daveloper.moviesapp.auxiliar.ext_fun.activity_context.loadImage
import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.databinding.MovieCardViewBinding
import com.daveloper.moviesapp.databinding.MovieVerticalCardViewBinding

class MovieVerticalAdapter (
    private val movieList: List<Movie>,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<MovieVerticalAdapter.ViewHolder>() {

    inner class ViewHolder (
        val binding: MovieVerticalCardViewBinding
    ) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }
        private fun getSelectedMovie(): String {
            return binding.tVMovieVcVId.text.toString()
        }
        override fun onClick(p0: View?) {
            val selectedItem: Int = bindingAdapterPosition
            if (selectedItem != RecyclerView.NO_POSITION) {
                if (getSelectedMovie().isNotEmpty()) {
                    listener.onItemClicked(selectedItem, getSelectedMovie())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = MovieVerticalCardViewBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Loading Poster image on the cardview
        movieList[position].posterImgURL?.let { holder.binding.imgVMovieVcVPoster.loadImage(it, false) }
        // Loading movie rating value
        holder.binding.tVMovieVcVRate.text = movieList[position].rating
        // Loading movie title value
        holder.binding.tVMovieVcVTitle.text = movieList[position].name
        // Loading movie release date value
        holder.binding.tVMovieVcVDate.text = movieList[position].releaseDate
        // Loading movie id value
        holder.binding.tVMovieVcVId.text = movieList[position].id.toString()
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    interface OnItemClickListener {
        fun onItemClicked (selectedItem: Int, movieIDSelected: String)
    }
}