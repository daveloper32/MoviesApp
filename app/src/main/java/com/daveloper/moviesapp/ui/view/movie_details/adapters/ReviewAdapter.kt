package com.daveloper.moviesapp.ui.view.movie_details.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daveloper.moviesapp.R
import com.daveloper.moviesapp.core.ResourceProvider
import com.daveloper.moviesapp.data.model.entity.Review
import com.daveloper.moviesapp.databinding.ReviewCardViewBinding

class ReviewAdapter (
    private val reviewList: List<Review>,
    val listener: OnItemClickListener,
    private val context: Context
) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    inner class ViewHolder(
        val binding: ReviewCardViewBinding
    ) : RecyclerView.ViewHolder(binding.root),
            View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val selectedItem: Int = bindingAdapterPosition
            if (selectedItem != RecyclerView.NO_POSITION) {
                if (reviewList[selectedItem] != null) {
                    listener.onItemClicked(selectedItem, reviewList[selectedItem])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ReviewCardViewBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Loading movie review rating
        if (reviewList[position].authorReview.rating == null) {
            holder.binding.tVReviewcVRate.text = ResourceProvider(context).getStringResource(R.string.tV_reviewcV_rating_reviews_not_found)
        } else {
            holder.binding.tVReviewcVRate.text = reviewList[position].authorReview.rating.toString()
        }
        // Loading review author
        holder.binding.tVReviewcVAuthor.text = reviewList[position].author
        // Loading review content
        holder.binding.tVReviewcVContent.text = reviewList[position].content
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    interface OnItemClickListener {
        fun onItemClicked (selectedItem: Int, reviewSelected: Review)
    }
}