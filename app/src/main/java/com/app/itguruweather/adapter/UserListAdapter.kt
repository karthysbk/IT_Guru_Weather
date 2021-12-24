package com.app.itguruweather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.itguruweather.data.User
import com.app.itguruweather.databinding.ListUserBinding

class UserListAdapter(val onClickItem: OnClickItem) :
    RecyclerView.Adapter<UserListAdapter.UserListViewHolder>() {

    inner class UserListViewHolder(val binding: ListUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    val diffCallBack = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)

    var userData: List<User>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun getItemCount(): Int = userData.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        return UserListViewHolder(
            ListUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val user = userData[position]
        holder.binding.apply {
            itemFirstName.text = user.firstName
            itemLastName.text = user.lastName
            itemEmail.text = user.email
        }

        holder.itemView.setOnClickListener {
            onClickItem.onClick(user)
        }
    }
}

interface OnClickItem {
    fun onClick(i: User)
}