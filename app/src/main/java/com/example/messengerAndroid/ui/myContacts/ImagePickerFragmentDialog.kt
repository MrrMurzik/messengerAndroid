package com.example.messengerAndroid.ui.myContacts

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.messengerAndroid.databinding.DialogAddContactBinding
import com.example.messengerAndroid.databinding.DialogDeniedPermissionBinding
import com.example.messengerAndroid.utils.Constants
import com.example.messengerAndroid.utils.Constants.KEY_SAVE_STATE_JOB
import com.example.messengerAndroid.utils.Constants.KEY_SAVE_STATE_NAME
import com.example.messengerAndroid.utils.Constants.KEY_SAVE_STATE_PHOTO_URI

class ImagePickerFragmentDialog() : DialogFragment() {

    private var uri = Uri.EMPTY
    private lateinit var dialogBinding: DialogAddContactBinding
    private lateinit var listener: DialogListener

    private val requestGalleryAccessPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        if (!it) {
            showPermissionDeniedDialog()
        } else {
            launchPickPhoto()
        }
    }

    private val pickMediaLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        this.uri = uri?: Uri.EMPTY
        dialogBinding.imageViewAvatar.setImageURI(uri)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = DialogAddContactBinding.inflate(layoutInflater)
        with(dialogBinding) {
            if (savedInstanceState != null) {
                textInputName.editText?.setText(savedInstanceState.getString(KEY_SAVE_STATE_NAME))
                textInputJob.editText?.setText(savedInstanceState.getString(KEY_SAVE_STATE_JOB))
                uri = Uri.parse(savedInstanceState.getString(KEY_SAVE_STATE_PHOTO_URI))
            }
            val dialog = AlertDialog.Builder(requireContext()).setView(root).create()
            setListenerForAddImageViews(this)
            buttonConfirm.setOnClickListener(getAddUserDialogListener())
            buttonCancel.setOnClickListener { dialog.dismiss() }
            return dialog
        }


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SAVE_STATE_NAME, dialogBinding.textInputName.editText?.text.toString())
        outState.putString(KEY_SAVE_STATE_JOB, dialogBinding.textInputJob.editText?.text.toString())
        outState.putString(KEY_SAVE_STATE_PHOTO_URI, uri.toString())

    }

    private fun setListenerForAddImageViews(dialogBinding: DialogAddContactBinding) {

        val listener = View.OnClickListener {
            if (requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
                requestGalleryAccessPermission()
            } else {
                launchPickPhoto()
            }
        }
        dialogBinding.textViewAddPhoto.setOnClickListener(listener)
        dialogBinding.imageViewAvatar.setOnClickListener(listener)
    }

    private fun requestGalleryAccessPermission() {
        requestGalleryAccessPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun launchPickPhoto() {
        pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    fun setListener(listener: DialogListener): DialogFragment {
        this.listener = listener
        return this
    }


    private fun getAddUserDialogListener(): View.OnClickListener {

        return View.OnClickListener {
            listener.onDialogPositiveClick(
                dialogBinding.textInputName.editText?.text.toString(),
                dialogBinding.textInputJob.editText?.text.toString(),
                uri.toString()
            )
            dialog?.dismiss()
        }

    }

    private fun showPermissionDeniedDialog() {
        val deniedDialogBinding = DialogDeniedPermissionBinding.inflate(requireActivity().layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()
        dialog.show()
        setPermissionDeniedDialogListeners(deniedDialogBinding, dialog)
    }

    private fun setPermissionDeniedDialogListeners(
        deniedDialogBinding: DialogDeniedPermissionBinding,
        dialog: AlertDialog
    ) {
        deniedDialogBinding.buttonGrantPermission.setOnClickListener {
            if (requireActivity().shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                requestGalleryAccessPermission()
            } else {
                openAppSettings()
            }
            dialog.dismiss()
        }
        deniedDialogBinding.buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts(Constants.SETTINGS_PACKAGE_SCHEME, requireActivity().packageName, null)
        intent.data = uri
        requireActivity().startActivity(intent)
    }



}