package com.dicoding.intermediate.submissionstoryapp.ui.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.intermediate.submissionstoryapp.data.response.ListStoryItem
import com.dicoding.intermediate.submissionstoryapp.databinding.ItemStoryBinding
import com.dicoding.intermediate.submissionstoryapp.ui.detailstory.DetailStoryActivity
import com.dicoding.intermediate.submissionstoryapp.utils.withDateFormat

class MainAdapter(private val listReview: List<ListStoryItem>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {
            Glide.with(binding.root.context)
                .load(data.photoUrl)
                .into(binding.imgItemPhoto)
            binding.tvItemName.text = data.name
            binding.tvItemCreated.text = data.createdAt.withDateFormat()
            binding.tvItemDescription.text = data.description
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.NAME, data.name)
                intent.putExtra(DetailStoryActivity.CREATE_AT, data.createdAt)
                intent.putExtra(DetailStoryActivity.DESCRIPTION, data.description)
                intent.putExtra(DetailStoryActivity.PHOTO_URL, data.photoUrl)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        androidx.core.util.Pair(binding.imgItemPhoto, "photo"),
                        androidx.core.util.Pair(binding.tvItemName, "name"),
                        androidx.core.util.Pair(binding.tvItemCreated, "createdate"),
                        androidx.core.util.Pair(binding.tvItemDescription, "description"),
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listReview[position]
        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listReview.size
}
