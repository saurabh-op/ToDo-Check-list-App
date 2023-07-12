package com.example.checklist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.checklist.R
import com.example.checklist.databinding.FragmentSigninBinding
import com.google.firebase.auth.FirebaseAuth

class SigninFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentSigninBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
    }


    private fun init(view: View) {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()

    }

    private fun registerEvents() {
        binding.signinSignupTv.setOnClickListener {
            navControl.navigate(R.id.action_signinFragment_to_signupFragment)
        }
        binding.signinLoginTv.setOnClickListener {
            val email = binding.signinEmailEt.text.toString().trim()
            val pass = binding.signinPassword.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty() ) {

                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Login Successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            navControl.navigate(R.id.action_signinFragment_to_homeFragment)

                        } else {
                            Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                }

            }

        }

    }


}