package com.example.messengerAndroid.ui.myContacts

import android.content.Context
import android.provider.ContactsContract
import com.example.messengerAndroid.data.contactsRepository.contactModel.User
import com.example.messengerAndroid.utils.UniqueIdGenerator.getUniqueId

class ContactFetcher {

    fun fetchContacts(context: Context): List<User> {
        val contacts = mutableListOf<User>()
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val name =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val photo: String? =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI))
                val job: String? =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Organization.COMPANY))
                val contact = User(getUniqueId(), photo?: "", name, job?: "")
                contacts.add(contact)
            }
        }
        return contacts.toList()
    }
}