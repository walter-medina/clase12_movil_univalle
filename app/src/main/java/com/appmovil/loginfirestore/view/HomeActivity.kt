package com.appmovil.loginfirestore.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.appmovil.loginfirestore.R
import com.appmovil.loginfirestore.databinding.ActivityHomeBinding
import com.appmovil.loginfirestore.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
        sharedPreferences = getSharedPreferences("shared",Context.MODE_PRIVATE)
        dataLogin()
        setup()
    }

    private fun setup() {
        binding.btnLogOut.setOnClickListener {
            logOut()
        }
    }

    private fun dataLogin() {
        val bundle = intent.extras
        val email = bundle?.getString("email")
        binding.tvTitleEmail.text = email ?: ""
        sharedPreferences.edit().putString("email",email).apply()
    }

    private fun logOut() {
        sharedPreferences.edit().clear().apply()
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this,LoginActivity::class.java))

    }

}