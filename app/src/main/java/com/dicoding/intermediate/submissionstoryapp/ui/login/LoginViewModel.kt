package com.dicoding.intermediate.submissionstoryapp.ui.login

import android.widget.Toast
import androidx.lifecycle.*
import com.dicoding.intermediate.submissionstoryapp.data.api.ApiConfig
import com.dicoding.intermediate.submissionstoryapp.data.response.LoginResponse
import com.dicoding.intermediate.submissionstoryapp.data.response.LoginResult
import com.dicoding.intermediate.submissionstoryapp.data.response.UserPreferenceDatastore
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreferenceDatastore): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val error = MutableLiveData("")
    val message = MutableLiveData("")
    private val tag = LoginViewModel::class.simpleName

    val loginResult = MutableLiveData<LoginResponse>()

    fun getUser(): LiveData<LoginResult> {
        return pref.getUser().asLiveData()
    }

    fun saveUser(userName: String, userId: String, userToken: String) {
        viewModelScope.launch {
            pref.saveUser(userName, userId, userToken)
        }
    }

    fun signout() {
        viewModelScope.launch {
            pref.signout()
        }
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().doLogin(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        loginResult.postValue(response.body())
                        message.postValue("200")
                    }
                    400 -> error.postValue("400")
                    401 -> error.postValue("401")
                    else -> error.postValue("ERROR ${response.code()} : ${response.message()}")
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = true
                Toast.makeText(null, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}