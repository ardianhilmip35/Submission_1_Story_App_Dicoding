package com.dicoding.intermediate.submissionstoryapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.intermediate.submissionstoryapp.data.response.UserPreferenceDatastore
import com.dicoding.intermediate.submissionstoryapp.ui.login.LoginViewModel
import com.dicoding.intermediate.submissionstoryapp.ui.main.MainViewModel
import com.dicoding.intermediate.submissionstoryapp.ui.register.RegisterViewModel

class ViewModelFactory(private val pref: UserPreferenceDatastore) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                    LoginViewModel(pref) as T
                }
                modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                    RegisterViewModel(pref) as T
                }
                modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                    MainViewModel(pref) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
}