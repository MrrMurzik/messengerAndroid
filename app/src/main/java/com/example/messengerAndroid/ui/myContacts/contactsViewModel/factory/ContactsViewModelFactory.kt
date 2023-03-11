package com.example.messengerAndroid.ui.myContacts.contactsViewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.messengerAndroid.ui.myContacts.contactsViewModel.ContactsViewModel
import com.example.messengerAndroid.ui.myContacts.contactsViewModel.contract.UsersDataSelector

@Suppress("UNCHECKED_CAST")
class ContactsViewModelFactory(private val usersDataSelector: UsersDataSelector) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            return ContactsViewModel(usersDataSelector) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}