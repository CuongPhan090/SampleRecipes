package com.offline.continentalrecipesusingnavgraph.view.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.offline.continentalrecipesusingnavgraph.R
import com.offline.continentalrecipesusingnavgraph.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var textWatcher: TextWatcher
    private var auth: FirebaseAuth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        (activity as AppCompatActivity).supportActionBar?.hide()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signIn.setOnClickListener {
            auth.signInWithEmailAndPassword(
                binding.username.text.toString(),
                binding.password.text.toString()
            ).addOnSuccessListener {
                val userToken = it.user?.getIdToken(false)?.result?.token ?: ""
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToCategoryFragment(userToken))
            }
            .addOnFailureListener {
                AlertDialog.Builder(view.context)
                    .setMessage(getString(R.string.sign_in_failed_message))
                    .setPositiveButton("Ok") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }

        textWatcher = object: TextWatcher{
            var isUsernameValid = false
            var isPasswordValid = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    when {
                        s === binding.username.editableText -> {
                            isUsernameValid = android.util.Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()
                        }
                        s === binding.password.editableText -> {
                            isPasswordValid = it.length >= 6
                        }
                    }
                    validateUserInput(isUsernameValid, isPasswordValid)
                }
            }
        }
        binding.username.addTextChangedListener(textWatcher)
        binding.password.addTextChangedListener(textWatcher)
    }

    private fun validateUserInput(isUsernameValid: Boolean, isPasswordValid: Boolean) {
        binding.signIn.isEnabled = isUsernameValid && isPasswordValid
    }


    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.show()

        binding.username.removeTextChangedListener(textWatcher)
        binding.password.removeTextChangedListener(textWatcher)
    }
}