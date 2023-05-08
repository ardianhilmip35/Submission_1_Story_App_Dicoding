package com.dicoding.intermediate.submissionstoryapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import androidx.lifecycle.ViewModelProvider
import com.dicoding.intermediate.submissionstoryapp.R
import com.dicoding.intermediate.submissionstoryapp.data.response.UserPreferenceDatastore
import com.dicoding.intermediate.submissionstoryapp.data.response.dataStore
import com.dicoding.intermediate.submissionstoryapp.databinding.ActivityRegisterBinding
import com.dicoding.intermediate.submissionstoryapp.ui.ViewModelFactory
import com.dicoding.intermediate.submissionstoryapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()

        binding?.intLogin?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupAction() {
        binding?.buttonReg?.setOnClickListener {
            val name = binding?.edRegisterName?.text.toString()
            val email = binding?.edRegisterEmail?.text.toString()
            val password = binding?.edRegisterPassword?.text.toString()
            when {
                name.isEmpty() -> {
                    binding?.edRegisterName?.error = getString(R.string.name_empty)
                }
                email.isEmpty() -> {
                    binding?.edRegisterEmail?.error = getString(R.string.email_empty)
                }
                password.isEmpty() -> {
                    binding?.edRegisterPassword?.error = getString(R.string.password_empty)
                }
                password.length < 8 -> {
                    binding?.edRegisterPassword?.error = getString(R.string.password_short)
                }
                else -> {
                    registerViewModel.register(name, email, password)
                }
            }
        }
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(this, ViewModelFactory(UserPreferenceDatastore.getInstance(dataStore))).get(RegisterViewModel::class.java)

        registerViewModel.let { viewModel ->
            viewModel.message.observe(this) { message ->
                if (message == "201") {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.info)
                    builder.setMessage(R.string.register_success)
                    builder.setIcon(R.drawable.ic_baseline_check_24)
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        alertDialog.dismiss()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }, delay)
                }
            }
            viewModel.error.observe(this) { error ->
                if (error == "400") {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.info)
                    builder.setMessage(R.string.register_failed)
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

    private fun playAnimation() {
        val ivLogo = ObjectAnimator.ofFloat(binding?.imLogo, View.ALPHA, 1f).setDuration(duration)
        val txtRegister = ObjectAnimator.ofFloat(binding?.textRegister, View.ALPHA, 1f).setDuration(duration)
        val txtName = ObjectAnimator.ofFloat(binding?.txName, View.ALPHA, 1f).setDuration(duration)
        val edRegisterName = ObjectAnimator.ofFloat(binding?.edRegisterName, View.ALPHA,1f).setDuration(duration)
        val txtEmail = ObjectAnimator.ofFloat(binding?.txEmail, View.ALPHA, 1f).setDuration(duration)
        val edRegisterEmail = ObjectAnimator.ofFloat(binding?.edRegisterEmail, View.ALPHA, 1f).setDuration(duration)
        val txtPassword = ObjectAnimator.ofFloat(binding?.txPassword, View.ALPHA, 1f).setDuration(duration)
        val edRegisterPassword = ObjectAnimator.ofFloat(binding?.edRegisterPassword, View.ALPHA, 1f).setDuration(duration)
        val btnRegister = ObjectAnimator.ofFloat(binding?.buttonReg, View.ALPHA, 1f).setDuration(duration)
        val txtLogin = ObjectAnimator.ofFloat(binding?.intLogin, View.ALPHA, 1f).setDuration(duration)

        AnimatorSet().apply {
            playSequentially(ivLogo, txtRegister, txtName, edRegisterName, txtEmail, edRegisterEmail, txtPassword, edRegisterPassword, btnRegister, txtLogin)
            startDelay = duration
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val delay = 2000L
        const val duration = 500L
    }
}