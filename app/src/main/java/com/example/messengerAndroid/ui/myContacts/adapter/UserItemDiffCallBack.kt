package com.example.messengerAndroid.ui.myContacts.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.messengerAndroid.data.contactsRepository.contactModel.User

class UserItemDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem == newItem
}