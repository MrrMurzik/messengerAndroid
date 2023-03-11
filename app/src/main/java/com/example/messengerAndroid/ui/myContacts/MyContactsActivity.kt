package com.example.messengerAndroid.ui.myContacts

import android.Manifest.permission.READ_CONTACTS
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerAndroid.R
import com.example.messengerAndroid.data.contactsRepository.UsersListGenerator
import com.example.messengerAndroid.databinding.ActivityMyContactsBinding
import com.example.messengerAndroid.data.contactsRepository.contactModel.User
import com.example.messengerAndroid.databinding.DialogDeniedPermissionBinding
import com.example.messengerAndroid.ui.myContacts.adapter.ContactsAdapter
import com.example.messengerAndroid.ui.myContacts.adapter.UserActionListener
import com.example.messengerAndroid.ui.myContacts.contactsViewModel.ContactsViewModel
import com.example.messengerAndroid.ui.myContacts.contactsViewModel.contract.UsersDataSelector
import com.example.messengerAndroid.ui.myContacts.contactsViewModel.factory.ContactsViewModelFactory
import com.example.messengerAndroid.utils.Constants.IS_FETCHING_REQUIRED_KEY
import com.example.messengerAndroid.utils.Constants.SETTINGS_PACKAGE_SCHEME
import com.example.messengerAndroid.utils.Constants.TAG_ADD_CONTACT_DIALOG
import com.google.android.material.snackbar.Snackbar


class MyContactsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyContactsBinding
    private var isCheckedFetching = false



    private val viewModel: ContactsViewModel by viewModels {
        ContactsViewModelFactory(usersDataSelector = object : UsersDataSelector {

            override fun getUsers(): List<User> {
                return if (isCheckedFetching &&
                    checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                ) {
                    ContactFetcher().fetchContacts(this@MyContactsActivity)
                } else {
                    UsersListGenerator().getUsers()
                }
            }

        })
    }

    private val adapterContacts: ContactsAdapter by lazy {
        ContactsAdapter(actionListener = object : UserActionListener {

            override fun onUserDelete(user: User) {
                val listener = getDeletionAlertDialogListener(user)
                showDeletionAlertDialog(listener)
            }
        })
    }

    private val requestReadContactsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            setupRecyclerView()
        } else {
            showPermissionDeniedDialog()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isCheckedFetching = intent.getBooleanExtra(IS_FETCHING_REQUIRED_KEY, false)

        if (isCheckedFetching && checkSelfPermission(READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestReadContactsPermission()
        } else {
            setupRecyclerView()
        }
    }

    override fun onRestart() {
        super.onRestart()
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            setupRecyclerView()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        requestReadContactsPermissionLauncher.unregister()
    }

    private fun setupRecyclerView() {
        initRecycler()
        addSwipeToDeleteFeature()
        setListeners()
        setObservers()
    }


    private fun initRecycler() {
        binding.recyclerContacts.run {
            adapter = adapterContacts
            layoutManager = LinearLayoutManager(this@MyContactsActivity)
        }
    }

    private fun addSwipeToDeleteFeature() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.absoluteAdapterPosition
                val user = viewModel.contactsLiveData.value?.get(position)
                viewModel.deleteUser(user)
                showUndoSnackbar(user, position, viewModel, binding)

            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerContacts)
    }

    private fun setListeners() {
        binding.textViewAddContacts.setOnClickListener {
            ImagePickerFragmentDialog().show(supportFragmentManager, TAG_ADD_CONTACT_DIALOG)

        }

        binding.imageButtonArrowBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }



    private fun setObservers() {
        viewModel.contactsLiveData.observe(this) {
            adapterContacts.submitList(it.toMutableList())
        }
    }


    private fun getDeletionAlertDialogListener(user: User): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    val userPosition = viewModel.getUserPosition(user)
                    viewModel.deleteUser(user)
                    showUndoSnackbar(user, userPosition, viewModel, binding)
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    showToast(getString(R.string.toast_cancelled_deletion))
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showUndoSnackbar(
        user: User?, position: Int, viewModel: ContactsViewModel, binding: ActivityMyContactsBinding
    ) {
        if (position == -1) {
            return
        }
        Snackbar.make(
            binding.root, R.string.deletion_snackbar_message, Snackbar.LENGTH_LONG
        ).setAction(R.string.undo_delete_action) { viewModel.addExistingUser(user, position) }
            .setActionTextColor(Color.CYAN).show()
    }

    private fun showDeletionAlertDialog(listener: DialogInterface.OnClickListener) {

        AlertDialog.Builder(this).setCancelable(true)
            .setTitle(R.string.default_deletion_dialog_title)
            .setMessage(R.string.default_deletion_dialog_message)
            .setTitle(R.string.default_deletion_dialog_title)
            .setMessage(R.string.default_deletion_dialog_message)
            .setPositiveButton(R.string.action_confirmed, listener)
            .setNegativeButton(R.string.action_cancelled, listener).create().show()
    }


    private fun requestReadContactsPermission() {
        requestReadContactsPermissionLauncher.launch(READ_CONTACTS)
    }

    private fun showPermissionDeniedDialog() {
        val dialogBinding = DialogDeniedPermissionBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()
        dialog.show()
        setPermissionDeniedDialogListeners(dialogBinding, dialog)
    }

    private fun setPermissionDeniedDialogListeners(
        dialogBinding: DialogDeniedPermissionBinding,
        dialog: AlertDialog
    ) {
        dialogBinding.buttonGrantPermission.setOnClickListener {
            if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
                requestReadContactsPermission()
            } else {
                openAppSettings()
            }
            dialog.dismiss()
        }
        dialogBinding.buttonCancel.setOnClickListener {
            this@MyContactsActivity.finish()
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts(SETTINGS_PACKAGE_SCHEME, packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}