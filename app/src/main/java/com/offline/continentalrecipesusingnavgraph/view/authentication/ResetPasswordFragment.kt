package com.offline.continentalrecipesusingnavgraph.view.authentication

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
import com.offline.continentalrecipesusingnavgraph.R
import com.offline.continentalrecipesusingnavgraph.databinding.FragmentResetPasswordBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ResetPasswordFragment : Fragment() {
    private lateinit var binding: FragmentResetPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResetPasswordBinding.inflate(inflater)
        (activity as AppCompatActivity).supportActionBar?.title = "Reset Password"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.email.addTextChangedListener {
            it?.let{
                binding.submit.isEnabled =
                    android.util.Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()
            }
        }

        binding.submit.setOnClickListener{
            binding.submit.isEnabled = false
            val now = System.currentTimeMillis()
            AlertDialog.Builder(view.context)
                .setMessage(getString(R.string.reset_password_alert))
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
            CoroutineScope(Dispatchers.Main).launch {
                repeat(31) {
                    delay(1000)
                    binding.submit.text = (31 - ((System.currentTimeMillis() - now) / 1000 ).toInt()).toString()
                }
                binding.submit.text = getString(R.string.submit)
                binding.submit.isEnabled = true
            }
        }
    }
}