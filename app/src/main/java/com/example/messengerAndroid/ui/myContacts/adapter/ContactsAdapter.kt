package com.example.messengerAndroid.ui.myContacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerAndroid.databinding.ItemContactBinding
import com.example.messengerAndroid.extensions.addPhoto
import com.example.messengerAndroid.data.contactsRepository.contactModel.User

class ContactsAdapter (private val actionListener: UserActionListener)
    : ListAdapter<User, ContactsAdapter.ContactsViewHolder>(UserItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContactBinding.inflate(inflater, parent, false)
        return ContactsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    inner class ContactsViewHolder (private val binding: ItemContactBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun bindTo(user: User) {
            with (binding) {
                tvName.text = user.name
                tvJob.text = user.job
                binding.ivAvatar.addPhoto(user)
                setListeners(user)
            }
        }

        private fun setListeners(user: User) {
            binding.ibTrash.setOnClickListener {
                actionListener.onUserDelete(user)
            }
        }
    }

}