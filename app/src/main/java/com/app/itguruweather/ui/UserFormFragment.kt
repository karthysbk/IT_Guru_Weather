package com.app.itguruweather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.itguruweather.R
import com.app.itguruweather.data.User
import com.app.itguruweather.databinding.LoginScreenBinding
import com.app.itguruweather.databinding.UserFormScreenBinding
import com.app.itguruweather.viewmodel.UserFormViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class UserFormFragment : Fragment() {

    private lateinit var binding: UserFormScreenBinding
    private lateinit var viewModel: UserFormViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(UserFormViewModel::class.java)
        binding = UserFormScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_userFormFragment_to_userListFragment2)
        }

        binding.btnSave.setOnClickListener {
            if (
                binding.firstName.text!!.isNotEmpty() &&
                binding.lastName.text!!.isNotEmpty() &&
                binding.email.text!!.isNotEmpty()
            ) {
                val user = User(
                    binding.firstName.text.toString(),
                    binding.lastName.text.toString(),
                    binding.email.text.toString()
                )
                callSaveUser(user)
            } else {
                Toast.makeText(requireContext(), "All fields are required to save user", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    private fun callSaveUser(user: User) {
        viewModel.saveUser(user)
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                when (it) {
                    is UserFormViewModel.ResultState.Success -> {
                        binding.pgUserForm.isVisible = false
                        Toast.makeText(requireContext(), "" + it.message, Toast.LENGTH_SHORT)
                            .show()
                        if(it.result == true)
                            navigateToUserList()
                    }
                    is UserFormViewModel.ResultState.Failure -> {
                        binding.pgUserForm.isVisible = false
                        Toast.makeText(requireContext(), "" + it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    is UserFormViewModel.ResultState.Loading -> binding.pgUserForm.isVisible =
                        true
                    else -> Unit
                }
            }
        }
    }

    fun navigateToUserList() {
        findNavController().navigate(R.id.action_userFormFragment_to_userListFragment2)
    }

}