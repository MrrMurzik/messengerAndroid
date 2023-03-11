package com.example.messengerAndroid.ui.myProfile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.example.messengerAndroid.R
import com.example.messengerAndroid.data.preferences.SharedPreferencesHelper
import com.example.messengerAndroid.databinding.ActivityMyProfileBinding
import com.example.messengerAndroid.extensions.cropPhoto
import com.example.messengerAndroid.ui.signUp.SignUpActivity
import com.example.messengerAndroid.ui.myContacts.MyContactsActivity
import com.example.messengerAndroid.utils.Constants.IS_FETCHING_REQUIRED_KEY
import com.example.messengerAndroid.utils.Constants.USER_NAME_KEY

class MyProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        binding.imageViewPicture.cropPhoto(R.drawable.babyyoda)
        binding.textViewName.text = intent.getStringExtra(USER_NAME_KEY)
        setContentView(binding.root)

        setListeners()
        updateBackButtonBehavior()
    }

    private fun updateBackButtonBehavior() {
        val onBackPressedCallback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                signOutAndDeleteCache()
            }
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun setListeners() {
        binding.buttonContacts.setOnClickListener {
            val intent = Intent(this, MyContactsActivity::class.java)
            val isFetchedContact = binding.checkBoxFetchContacts?.isChecked
            intent.putExtra(IS_FETCHING_REQUIRED_KEY, isFetchedContact)
            startActivity(intent)
        }

        binding.buttonSignOut.setOnClickListener {
            signOutAndDeleteCache()
        }

    }

    private fun signOutAndDeleteCache() {
        SharedPreferencesHelper.clearPrefs()
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

}
