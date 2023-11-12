package com.appmovil.loginfirestore.view.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appmovil.loginfirestore.databinding.FragmentHomeBinding
import com.appmovil.loginfirestore.view.HomeActivity
import com.appmovil.loginfirestore.view.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("shared", Context.MODE_PRIVATE)
        dataLogin()
        setup()
    }

    private fun setup() {
        binding.btnLogOut.setOnClickListener {
            logOut()
        }


        binding.btnGuardarArticulo.setOnClickListener {
            guardarProducto()
        }
    }

    private fun guardarProducto() {
        val articulo = binding.etNombreArticulo.text.toString()
        val precio = binding.etPrecio.text.toString()
        if (articulo.isNotEmpty() && precio.isNotEmpty()){
            db.collection("articulo").document(articulo).set(
                hashMapOf("articulo" to articulo,
                    "precio" to precio)
            )
            listarProducto()
        }


    }

    private fun listarProducto(){
        db.collection("articulo").get().addOnSuccessListener {
            var data = ""
            for (document in it.documents) {
                // Aquí puedes personalizar cómo deseas mostrar cada artículo en la lista
                data += "${document.get("articulo")}\n" +
                        "${document.get("precio")}\n\n"
            }
            binding.tvListProducto.text = data

        }

    }



    private fun dataLogin() {
        val bundle = requireActivity().intent.extras
        val email = bundle?.getString("email")
        binding.tvTitleEmail.text = email ?: ""
        sharedPreferences.edit().putString("email",email).apply()
    }

    private fun logOut() {
        sharedPreferences.edit().clear().apply()
        FirebaseAuth.getInstance().signOut()
        (requireActivity() as HomeActivity).apply {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }


    }


}