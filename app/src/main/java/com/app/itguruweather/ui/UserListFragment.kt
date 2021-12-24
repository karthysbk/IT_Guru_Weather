package com.app.itguruweather.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.itguruweather.R
import com.app.itguruweather.adapter.OnClickItem
import com.app.itguruweather.adapter.UserListAdapter
import com.app.itguruweather.data.User
import com.app.itguruweather.databinding.LoginScreenBinding
import com.app.itguruweather.databinding.UserFormScreenBinding
import com.app.itguruweather.databinding.UserListScreenBinding
import com.app.itguruweather.viewmodel.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class UserListFragment : Fragment() {

    private lateinit var binding: UserListScreenBinding
    private lateinit var viewModel: UserListViewModel
    private lateinit var adapterList: UserListAdapter
    private lateinit var user: List<User>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(UserListViewModel::class.java)
        binding = UserListScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpAdapter()

        callUserListApi()
    }

    private fun callUserListApi() {
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is UserListViewModel.ListUiState.Success -> {
                        if (uiState.data.size > 0) {
                            adapterList?.userData = uiState.data
                            user = uiState.data
                            binding.tvNoUserFound.visibility = View.INVISIBLE
                            binding.pbUserList.visibility = View.GONE
                            binding.userList.visibility = View.VISIBLE
                        } else {
                            binding.userList.visibility = View.GONE
                            binding.pbUserList.visibility = View.GONE
                            binding.tvNoUserFound.visibility = View.VISIBLE
                        }
                    }
                    is UserListViewModel.ListUiState.SuccessDelete -> {
                        if (uiState.data != -1)
                            Toast.makeText(
                                requireContext(),
                                "" + uiState.message,
                                Toast.LENGTH_SHORT
                            ).show()
                    }
                    is UserListViewModel.ListUiState.Failure -> {
                        binding.tvNoUserFound.text = uiState.message
                        binding.pbUserList.visibility = View.GONE
                    }
                    is UserListViewModel.ListUiState.Loading ->
                        binding.pbUserList.visibility = View.VISIBLE
                    else -> Unit
                }
            }
        }
    }

    fun setUpAdapter() {
        val itemTouchHelper = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.deleteUser(user[position])
                viewModel.callUsersList()
            }

        }
        val itemTouchHelper2 = ItemTouchHelper(itemTouchHelper)
        binding.userList.apply {
            adapterList = UserListAdapter(object : OnClickItem {
                override fun onClick(i: User) {
                    deleteUser(i)
                }
            })
            adapter = adapterList
            layoutManager = LinearLayoutManager(requireContext())
            itemTouchHelper2.attachToRecyclerView(this)

        }

        //  itemTouchHelper2.attachToRecyclerView(binding.userList)

    }

    private fun deleteUser(user: User) {
        // TODO: DELETE USER

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.add_user_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController()
        if (item.itemId == R.id.add_user) {
            navController.navigate(R.id.action_userListFragment_to_userFormFragment)
        }
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}