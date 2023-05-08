package com.dicoding.intermediate.submissionstoryapp.ui.detailstory

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.intermediate.submissionstoryapp.R
import com.dicoding.intermediate.submissionstoryapp.databinding.ActivityDetailStoryBinding
import com.dicoding.intermediate.submissionstoryapp.utils.withDateFormat

class DetailStoryActivity : AppCompatActivity() {
    private var _binding: ActivityDetailStoryBinding? = null
    private val binding get() = _binding

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = getString(R.string.detail_story)

        val photoUrl = intent.getStringExtra(PHOTO_URL)
        val name = intent.getStringExtra(NAME)
        val createAt = intent.getStringExtra(CREATE_AT)
        val description = intent.getStringExtra(DESCRIPTION)

        Glide.with(binding?.root?.context!!)
            .load(photoUrl)
            .into(binding?.ivDetailPhoto!!)

        binding?.apply {
            tvDetailName.text = name
            tvDetailCreatedTime.text = createAt?.withDateFormat()
            tvDetailDescription.text = description
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val NAME = "name"
        const val CREATE_AT = "create_at"
        const val DESCRIPTION = "description"
        const val PHOTO_URL = "photoUrl"
    }
}