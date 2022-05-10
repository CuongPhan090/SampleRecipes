package com.offline.continentalrecipesusingnavgraph.view.authentication

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.offline.continentalrecipesusingnavgraph.R
import com.offline.continentalrecipesusingnavgraph.databinding.FragmentRegisterBinding
import com.offline.continentalrecipesusingnavgraph.uishared.ProgressAlertDialog

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var textWatcher: TextWatcher
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.register)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validateUserInputs()
        val progressAlertDialog = ProgressAlertDialog(requireActivity())

        binding.signIn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.signUp.setOnClickListener {
            progressAlertDialog.startLoadingDialog()
            auth.createUserWithEmailAndPassword(binding.email.text.toString(), binding.password.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        requireActivity().getSharedPreferences("credential", Context.MODE_PRIVATE).edit().apply{
                            putString("email",  binding.email.text.toString())
                            putString("password", binding.password.text.toString())
                            putBoolean("isLogIn", true)
                        }.apply()
                        it.result?.user?.let { firebaseUser ->
                            val userToken = firebaseUser.getIdToken(false).result?.token
                            requireActivity().supportFragmentManager.setFragmentResult("emailAddress", bundleOf("email" to firebaseUser.email))
                            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToCategoryFragment(userToken))
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
                    progressAlertDialog.dismissLoadingDialog()
                }
        }
    }

    private fun validateUserInputs() {
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    when (it) {
                        binding.email.editableText -> {
                            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()) {
                                binding.email.error = "Invalid Email"
                            } else {
                                binding.email.error = null
                            }
                        }

                        binding.password.editableText -> {
                            if (it.length < 6) {
                                binding.password.error =
                                    "Password length must be at least 6 characters"
                            } else {
                                binding.password.error = null
                            }
                        }

                        binding.confirmedPassword.editableText -> {
                            if (it.toString() != binding.password.text.toString()) {
                                binding.confirmedPassword.error = "Password not matched"
                            } else {
                                binding.confirmedPassword.error = null
                            }
                        }

                        binding.fullName.editableText -> {
                            if (it.isEmpty()) {
                                binding.fullName.error = "Invalid name"
                            } else {
                                binding.fullName.error = null
                            }
                        }

                        binding.phoneNumber.editableText -> {
                            if (!android.util.Patterns.PHONE.matcher(it.toString()).matches()) {
                                binding.phoneNumber.error = "Invalid phone number"
                            } else {
                                binding.phoneNumber.error = null
                            }
                        }
                    }
                }

                binding.signUp.isEnabled =
                    binding.email.error == null && binding.email.text.isNotEmpty()
                            && binding.password.error == null && binding.password.text.isNotEmpty()
                            && binding.confirmedPassword.error == null && binding.fullName.text.isNotEmpty()
                            && binding.fullName.error == null && binding.fullName.text.isNotEmpty()
                            && binding.phoneNumber.error == null && binding.phoneNumber.text.isNotEmpty()
            }
        }

        binding.email.addTextChangedListener(textWatcher)
        binding.password.addTextChangedListener(textWatcher)
        binding.confirmedPassword.addTextChangedListener(textWatcher)
        binding.fullName.addTextChangedListener(textWatcher)
        binding.phoneNumber.addTextChangedListener(textWatcher)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.email.removeTextChangedListener(textWatcher)
        binding.password.removeTextChangedListener(textWatcher)
        binding.confirmedPassword.removeTextChangedListener(textWatcher)
        binding.fullName.removeTextChangedListener(textWatcher)
        binding.phoneNumber.removeTextChangedListener(textWatcher)
    }
}
