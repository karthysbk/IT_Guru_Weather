package com.app.itguruweather.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.itguruweather.R
import com.app.itguruweather.databinding.LoginScreenBinding
import dagger.hilt.android.AndroidEntryPoint

private const val USER = "testapp@google.com"
private const val PASS = "Test@123456"

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: LoginScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnLogin.setOnClickListener {
            val user = binding.userName.text.toString()
            val pass = binding.password.text.toString()

            if (!TextUtils.isEmpty(user)) {
                if (!TextUtils.isEmpty(pass)) {
                    if (user.equals(USER) && pass.equals(PASS)) {
                        redirectUser()
                    } else {
                        binding.layoutUserName.error = null
                        binding.layoutPassword.error = null
                        Toast.makeText(requireContext(), "Wrong credentials", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }
                } else {
                    binding.layoutUserName.error = null
                    binding.layoutPassword.error = "Enter password"
                }
            } else {
                binding.layoutUserName.error = "Enter username"
                binding.layoutPassword.error = null
            }
        }
    }

    private fun redirectUser() {
        // CHECK INPUT WITH REMOTE OR LOCAL API -> USE VIEW MODEL TO CHECK CREDENTIALS

        findNavController().navigate(R.id.action_loginFragment_to_userListFragment)
    }

}