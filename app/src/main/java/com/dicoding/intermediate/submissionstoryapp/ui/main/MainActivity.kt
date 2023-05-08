package com.dicoding.intermediate.submissionstoryapp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.intermediate.submissionstoryapp.R
import com.dicoding.intermediate.submissionstoryapp.data.response.ListStoryItem
import com.dicoding.intermediate.submissionstoryapp.data.response.UserPreferenceDatastore
import com.dicoding.intermediate.submissionstoryapp.databinding.ActivityMainBinding
import com.dicoding.intermediate.submissionstoryapp.ui.ViewModelFactory
import com.dicoding.intermediate.submissionstoryapp.ui.addstory.AddStoryActivity
import com.dicoding.intermediate.submissionstoryapp.ui.login.LoginActivity
import com.dicoding.intermediate.submissionstoryapp.ui.login.LoginViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "User")

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = getString(R.string.dashboard)

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferenceDatastore.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.storyList.observe(this){ listStory->
            val adapter = MainAdapter(listStory as ArrayList<ListStoryItem>)
            binding?.rvListStory?.adapter = adapter
        }

        mainViewModel.isLoading.observe(this){isLoading->
            binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferenceDatastore.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this){user->
            if (user.userId.isEmpty()){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                mainViewModel.getListStory(user.token)
            }
        }

        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)

        binding?.apply {
            rvListStory.layoutManager = layoutManager
            rvListStory.addItemDecoration(itemDecoration)
            btnAddStory.setOnClickListener {
                val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_logout -> {
                loginViewModel.signout()
            }
            R.id.settings_language -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}