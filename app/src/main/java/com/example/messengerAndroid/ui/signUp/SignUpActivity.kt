package com.example.messengerAndroid.ui.signUp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.messengerAndroid.R
import com.example.messengerAndroid.data.preferences.SharedPreferencesHelper
import com.example.messengerAndroid.databinding.ActivitySignUpBinding
import com.example.messengerAndroid.ui.myProfile.MyProfileActivity
import com.example.messengerAndroid.utils.Constants.EMAIL_DIVIDER_PATTERN
import com.example.messengerAndroid.utils.Constants.USER_NAME_KEY
import com.example.messengerAndroid.utils.Validator


class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SharedPreferencesHelper.init(this)

        if (SharedPreferencesHelper.get().isNotEmpty()) {
            goMyProfileActivity(SharedPreferencesHelper.get())
        }

        setListeners()

    }

    private fun setListeners() {
        binding.buttonRegister.setOnClickListener { registerNewUser() }
    }

    private fun registerNewUser() {
        val email = binding.inputLayoutEmail.editText?.text.toString()
        val password = binding.inputLayoutPassword.editText?.text.toString()

        if (Validator.getValidityEmail(email) && Validator.getValidityPassword(password)) {
            val name = getName(email)
            if (binding.checkBoxRememberMe.isChecked) {
                SharedPreferencesHelper.set(name)
            }
            goMyProfileActivity(name)
        } else {
            showError(email, password)
        }
    }

    private fun showError(email: String, password: String) {
        if (!Validator.getValidityEmail(email)) {
           binding.inputLayoutEmail.error = getString(R.string.invalid_email)
        }
        if (!Validator.getValidityPassword(password)) {
            binding.inputLayoutPassword.error = getString(R.string.invalid_password)
        }
    }

    private fun goMyProfileActivity(name: String) {
        val intent = Intent(this, MyProfileActivity::class.java)
        intent.putExtra(USER_NAME_KEY, name)
        startActivity(intent)
    }


    private fun getName(email: String): String {
        val partOfName = email.substring(0, email.indexOf('@'))
        return partOfName.replace(EMAIL_DIVIDER_PATTERN.toRegex(), " ")
    }


}