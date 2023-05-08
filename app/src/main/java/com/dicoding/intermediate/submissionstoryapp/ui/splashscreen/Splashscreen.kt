package com.dicoding.intermediate.submissionstoryapp.ui.splashscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.intermediate.submissionstoryapp.data.response.UserPreferenceDatastore
import com.dicoding.intermediate.submissionstoryapp.databinding.ActivitySplashscreenBinding
import com.dicoding.intermediate.submissionstoryapp.ui.ViewModelFactory
import com.dicoding.intermediate.submissionstoryapp.ui.login.LoginActivity
import com.dicoding.intermediate.submissionstoryapp.ui.login.LoginViewModel
import com.dicoding.intermediate.submissionstoryapp.ui.main.MainActivity
import com.dicoding.intermediate.submissionstoryapp.ui.main.MainViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "User")

class Splashscreen : AppCompatActivity() {
    private var _binding: ActivitySplashscreenBinding? = null
    private val binding get() = _binding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferenceDatastore.getInstance(dataStore))
        )[MainViewModel::class.java]

        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferenceDatastore.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this){user->
            if (user.userId.isEmpty()){
                Handler().postDelayed({
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }, SPLASH_TIME_OUT)
            }else{
                Handler().postDelayed({
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, SPLASH_TIME_OUT)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val SPLASH_TIME_OUT: Long = 2000L
    }
}