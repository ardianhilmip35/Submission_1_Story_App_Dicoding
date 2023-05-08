package com.dicoding.intermediate.submissionstoryapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.intermediate.submissionstoryapp.data.api.ApiConfig
import com.dicoding.intermediate.submissionstoryapp.data.response.AddStoryResponse
import com.dicoding.intermediate.submissionstoryapp.data.response.ListStoryItem
import com.dicoding.intermediate.submissionstoryapp.data.response.StoryResponse
import com.dicoding.intermediate.submissionstoryapp.data.response.UserPreferenceDatastore
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MainViewModel(private val pref: UserPreferenceDatastore) : ViewModel() {

    private val _storyList = MutableLiveData<List<ListStoryItem>>()
    val storyList: LiveData<List<ListStoryItem>> = _storyList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getListStory(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListStory(bearer = "Bearer $token")
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _storyList.value = response.body()?.listStory
                } else {
                    Log.e(tag, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(tag, "onFailure: ${t.message}")
            }
        })
    }

    fun postNewStory(token: String, imageFile: File, desc: String) {
        _isLoading.value = true
        val description = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val image = MultipartBody.Part.createFormData("photo", imageFile.name, requestImageFile)

        val client = ApiConfig.getApiService().postNewStory(bearer = "Bearer $token", image, description)
        client.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                _isLoading.value = false
                when (response.code()) {
                    401 -> "${response.code()} : Bad Request"
                    403 -> "${response.code()} : Forbidden"
                    404 -> "${response.code()} : Not Found"
                    else -> "${response.code()} : ${response.message()}"
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(tag, "onFailure: ${t.message}")
            }
        })
    }

    companion object{
        private const val tag = "MainViewModel"
    }
}