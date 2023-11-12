package com.appmovil.loginfirestore.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.appmovil.loginfirestore.R
import com.appmovil.loginfirestore.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        sharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE)
        isSesion()
        setup()
        registerUser()
        loginUser()
    }

    private fun setup() {
        binding.tvRegister.setOnClickListener {
            registerUser()
        }

        binding.btnLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun registerUser() {
        val email = binding.etEmail.text.toString()
        val pass = binding.etPass.text.toString()
        if (email.isNotEmpty() && pass.isNotEmpty()) {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        goToHome(email)
                    } else {
                        Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun goToHome(email: String){
        val intent = Intent (this, HomeActivity::class.java).apply {
            putExtra("email",email)
        }
        startActivity(intent)
        finish()
    }

    private fun loginUser(){
        val email = binding.etEmail.text.toString()
        val pass = binding.etPass.text.toString()
        if (email.isNotEmpty() && pass.isNotEmpty()) {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        goToHome(email)
                    } else {
                        Toast.makeText(this, "Login incorrecto", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    private fun isSesion(){
        val email = sharedPreferences.getString("email",null)
        if (email != null){
            binding.clContenedor.visibility = View.INVISIBLE
            goToHome(email)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.clContenedor.visibility = View.VISIBLE
    }
}