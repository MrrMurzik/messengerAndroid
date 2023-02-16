package com.example.messengerAndroid.ui

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerAndroid.R
import com.example.messengerAndroid.databinding.ActivityMyContactsBinding
import com.example.messengerAndroid.databinding.DialogAddContactBinding
import com.example.messengerAndroid.extensions.showAddUserAlertDialog
import com.example.messengerAndroid.extensions.showDeletionAlertDialog
import com.example.messengerAndroid.data.contactsRepository.contactModel.User
import com.example.messengerAndroid.ui.contactsViewModel.ContactsViewModel
import com.example.messengerAndroid.ui.contactsViewModel.SwipeToDeleteCallback
import com.example.messengerAndroid.ui.contactsViewModel.getUserPosition
import com.google.android.material.snackbar.Snackbar


class MyContactsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyContactsBinding

    private val adapterContacts: ContactsAdapter by lazy {
        ContactsAdapter(actionListener = object : UserActionListener {

            override fun onUserDelete(user: User) {
                val listener = getDeletionAlertDialogListener(user)
                showDeletionAlertDialog(listener)
            }
        })
    }

    private val viewModel: ContactsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycler()
        addSwipeToDeleteFeature()
        setListeners()
        setObservers()

    }

    private fun setListeners() {
        binding.textViewAddContacts.setOnClickListener {
            val dialogBinding = DialogAddContactBinding.inflate(layoutInflater)
            val listener = getAddUserDialogListener(dialogBinding)
            showAddUserAlertDialog(listener)
        }
    }


    private fun addSwipeToDeleteFeature() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.absoluteAdapterPosition
                val user = viewModel.contactsLiveData.value?.get(position)!!
                val listener = getDeletionAlertDialogListener(user)
                showDeletionAlertDialog(listener)

            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerContacts)
    }

    private fun initRecycler() {
        binding.recyclerContacts.run {
            adapter = adapterContacts
            layoutManager = LinearLayoutManager(this@MyContactsActivity)
        }
    }

    private fun setObservers() {
       viewModel.contactsLiveData.observe(this){
            adapterContacts.submitList(it.toMutableList())
       }
    }

    private fun getAddUserDialogListener(dialogBinding: DialogAddContactBinding):
            DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    viewModel.addNewUser(
                        dialogBinding.textInputName.editText?.text.toString(),
                        dialogBinding.textInputJob.editText?.text.toString())
                }
                DialogInterface.BUTTON_NEGATIVE ->
                    showToast(R.string.toast_cancelled_deletion.toString())
            }
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
                DialogInterface.BUTTON_NEGATIVE ->
                    showToast(R.string.toast_cancelled_deletion.toString())
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showUndoSnackbar(user: User,
                                 position: Int,
                                 viewModel: ContactsViewModel,
                                 binding: ActivityMyContactsBinding) {
        Snackbar
            .make(
                binding.root,
                R.string.deletion_snackbar_message,
                5000)
            .setAction(R.string.undo_delete_action) {viewModel.addExistingUser(user, position)}
            .setActionTextColor(Color.CYAN)
            .show()
    }

}