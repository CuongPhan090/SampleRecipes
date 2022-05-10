package com.offline.continentalrecipesusingnavgraph.view.authentication

import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import androidx.core.os.bundleOf
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
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result?.user?.let { firebaseUser ->
                        val userToken = firebaseUser.getIdToken(false).result?.token
                        requireActivity().supportFragmentManager.setFragmentResult("emailAddress", bundleOf("email" to firebaseUser.email))
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToCategoryFragment(userToken))
                    }
                } else {
                    AlertDialog.Builder(view.context)
                        .setMessage(it.exception?.message)
                        .setPositiveButton("Ok") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
            }
        }

        binding.signUp.setOnClickListener {
            findNavController().navigate(R.id.action_login_fragment_to_registerFragment)
        }

        binding.resetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_login_fragment_to_resetPasswordFragment)
        }

        if (context?.packageManager?.hasSystemFeature(PackageManager.FEATURE_FACE) == false) {
            binding.usernameLayout.isEndIconVisible = false
        } else {
            binding.usernameLayout.setEndIconOnClickListener {
                if (biometricAvailable()) {
                    promptAuthenticatorDialog()
                } else {
                    Toast.makeText(binding.root.context, "The device has not set up FaceId.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        textWatcher = object: TextWatcher{
            var isUsernameValid = false
            var isPasswordValid = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    when (s) {
                        binding.username.editableText -> {
                            isUsernameValid = android.util.Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()
                        }
                        binding.password.editableText -> {
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

    private fun promptAuthenticatorDialog() {
        val biometricPrompt = BiometricPrompt(this,object: BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(binding.root.context, "Success", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(binding.root.context, "FaceId is not recognized", Toast.LENGTH_SHORT).show()
            }
        })

        val biometricInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login")
            .setSubtitle("Scan your face to log in")
            .setNegativeButtonText("Cancel")
            .setConfirmationRequired(false)
            .setAllowedAuthenticators(BIOMETRIC_WEAK)
            .build()

        biometricPrompt.authenticate(biometricInfo)
    }

    private fun biometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(binding.root.context)
        return biometricManager.canAuthenticate(BIOMETRIC_WEAK) ==  BiometricManager.BIOMETRIC_SUCCESS
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