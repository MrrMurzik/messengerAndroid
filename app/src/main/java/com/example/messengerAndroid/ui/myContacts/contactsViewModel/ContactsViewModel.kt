package com.example.messengerAndroid.ui.myContacts.contactsViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.messengerAndroid.data.contactsRepository.contactModel.User
import com.example.messengerAndroid.ui.myContacts.contactsViewModel.contract.UsersDataSelector
import com.example.messengerAndroid.utils.UniqueIdGenerator.getUniqueId


class ContactsViewModel(usersDataSelector: UsersDataSelector) : ViewModel() {

    private val _contactsLiveData = MutableLiveData<List<User>>()
    val contactsLiveData: LiveData<List<User>> = _contactsLiveData


    init {
        _contactsLiveData.value = usersDataSelector.getUsers()
    }

    fun deleteUser(user: User?) {
        _contactsLiveData.value = _contactsLiveData.value?.toMutableList()?.apply {
            remove(user)
        }
    }

    fun addExistingUser(user: User?, position: Int) {
        _contactsLiveData.value = _contactsLiveData.value?.toMutableList()?.apply {
            if (user != null) {
                add(position, user)
            }
        }
    }

    fun addNewUser(name: String, job: String, photo: String) {
        _contactsLiveData.value = _contactsLiveData.value?.toMutableList()?.apply {
            add(User(getUniqueId(), photo, name, job))
        }
    }

    fun getUserPosition(user: User): Int {
        return contactsLiveData.value?.indexOf(user) ?: -1
    }




}