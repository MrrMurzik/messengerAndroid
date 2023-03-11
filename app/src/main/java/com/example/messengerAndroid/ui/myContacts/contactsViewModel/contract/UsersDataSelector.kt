package com.example.messengerAndroid.ui.myContacts.contactsViewModel.contract

import com.example.messengerAndroid.data.contactsRepository.contactModel.User

interface UsersDataSelector {
    fun getUsers() : List<User>
}