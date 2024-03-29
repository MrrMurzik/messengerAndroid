package com.example.messengerAndroid.app

object Constants {

    //URL to back-end
    const val BASE_URL = "http://178.63.9.114:7777/api/"

    // constants for Validator
    const val PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$"
    const val EMAIL_DIVIDER_PATTERN = "[-._]"


    // constants for shared preferences

    const val APP_PREFERENCES = "login_preferences"

    const val USER_NAME_KEY = "name"
    const val EMAIL_KEY = "EMAIL_KEY"
    const val PASSWORD_KEY = "PASSWORD_KEY"
    const val PHONE_KEY = "PHONE_KEY"
    const val PHOTO_KEY = "PHOTO_KEY"

    const val TOKEN_KEY = "TOKEN_KEY"
    const val ID_KEY = "ID_KEY"


    // constants for packages

    const val SETTINGS_PACKAGE_SCHEME = "package"

    // constants for savedInstanceState of AddContactDialog

    const val KEY_SAVE_STATE_NAME = "editTextName"
    const val KEY_SAVE_STATE_JOB = "editTextJob"
    const val KEY_SAVE_STATE_PHOTO_URI = "avatarPhotoUri"
    const val KEY_SAVE_STATE_PHONE_NUMBER = "avatarPhotoUri"
    const val KEY_SAVE_STATE_POSTAL_ADDRESS = "avatarPhotoUri"

    // fragment tags

    const val TAG_ADD_CONTACT_DIALOG = "TAG_ADD_CONTACT_DIALOG"

    // number of pages in viewPager

    const val NUM_TABS = 2


}