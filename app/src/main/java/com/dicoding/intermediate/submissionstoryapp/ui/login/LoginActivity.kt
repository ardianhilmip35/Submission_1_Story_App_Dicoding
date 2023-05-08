package com.dicoding.intermediate.submissionstoryapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.intermediate.submissionstoryapp.R
import com.dicoding.intermediate.submissionstoryapp.data.response.UserPreferenceDatastore
import com.dicoding.intermediate.submissionstoryapp.databinding.ActivityLoginBinding
import com.dicoding.intermediate.submissionstoryapp.ui.ViewModelFactory
import com.dicoding.intermediate.submissionstoryapp.ui.main.MainActivity
import com.dicoding.intermediate.submissionstoryapp.ui.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "User")

class LoginActivity : AppCompatActivity() {
    private var _binding : ActivityLoginBinding? = null
    private val binding get() = _binding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()

        binding?.intRegister?.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun playAnimation() {
        val ivLogo = ObjectAnimator.ofFloat(binding?.imLogo, View.ALPHA, 1f).setDuration(duration)
        val txtLogin = ObjectAnimator.ofFloat(binding?.textLogin, View.ALPHA, 1f).setDuration(duration)
        val txtEmail = ObjectAnimator.ofFloat(binding?.txEmail, View.ALPHA, 1f).setDuration(duration)
        val edLoginEmail = ObjectAnimator.ofFloat(binding?.edLoginEmail, View.ALPHA, 1f).setDuration(duration)
        val txtPassword = ObjectAnimator.ofFloat(binding?.txPassword, View.ALPHA, 1f).setDuration(duration)
        val edLoginPassword = ObjectAnimator.ofFloat(binding?.edLoginPassword, View.ALPHA, 1f).setDuration(duration)
        val btnLogin = ObjectAnimator.ofFloat(binding?.buttonLog, View.ALPHA, 1f).setDuration(duration)
        val txtRegister = ObjectAnimator.ofFloat(binding?.intRegister, View.ALPHA, 1f).setDuration(duration)

        AnimatorSet().apply {
            playSequentially(ivLogo, txtLogin, txtEmail, edLoginEmail, txtPassword, edLoginPassword, btnLogin, txtRegister)
            startDelay = duration
        }.start()
    }

    private fun setupAction() {
        binding?.buttonLog?.setOnClickListener {
            val email = binding?.edLoginEmail?.text.toString()
            val password = binding?.edLoginPassword?.text.toString()
            when {
                email.isEmpty() -> {
                    binding?.edLoginEmail?.error = getString(R.string.email_empty)
                }
                password.isEmpty() -> {
                    binding?.edLoginPassword?.error = getString(R.string.password_empty)
                }
                password.length < 8 -> {
                    binding?.edLoginPassword?.error = getString(R.string.password_short)
                }
                else -> {
                    loginViewModel.login(email, password)
                }
            }
        }
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferenceDatastore.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.let { viewModel ->
            viewModel.loginResult.observe(this) { login ->
                viewModel.saveUser(
                    login.loginResult.name,
                    login.loginResult.userId,
                    login.loginResult.token
                )
            }

            viewModel.message.observe(this) { message ->
                if (message == "200") {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(getString(R.string.info))
                    builder.setMessage(getString(R.string.login_success))
                    builder.setIcon(R.drawable.ic_baseline_check_24)
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        alertDialog.dismiss()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }, delay)
                }
            }

            viewModel.error.observe(this) { error ->
                if (error == "400") {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(getString(R.string.info))
                    builder.setMessage(getString(R.string.label_invalid_email))
                    builder.setIcon(R.drawable.ic_baseline_close_24)
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        alertDialog.dismiss()
                    }, delay)
                }
                if (error == "401") {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(getString(R.string.info))
                    builder.setMessage(getString(R.string.user_not_found))
                    builder.setIcon(R.drawable.ic_baseline_close_24)
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        alertDialog.dismiss()
                    }, delay)
                }
            }

            viewModel.isLoading.observe(this) { isLoading ->
                binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val delay = 2000L
        const val duration = 500L
    }
}