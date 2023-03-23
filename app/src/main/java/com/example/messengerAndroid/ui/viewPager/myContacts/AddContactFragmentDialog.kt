package com.example.messengerAndroid.ui.viewPager.myContacts

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.messengerAndroid.databinding.DialogAddContactBinding
import com.example.messengerAndroid.extensions.openAppSettings
import com.example.messengerAndroid.ui.viewPager.myContacts.contactsViewModel.ContactsViewModel
import com.example.messengerAndroid.utils.Constants.KEY_SAVE_STATE_JOB
import com.example.messengerAndroid.utils.Constants.KEY_SAVE_STATE_NAME
import com.example.messengerAndroid.utils.Constants.KEY_SAVE_STATE_PHONE_NUMBER
import com.example.messengerAndroid.utils.Constants.KEY_SAVE_STATE_PHOTO_URI
import com.example.messengerAndroid.utils.Constants.KEY_SAVE_STATE_POSTAL_ADDRESS

class AddContactFragmentDialog : DialogFragment() {

    private var uri = Uri.EMPTY

    private var _binding :DialogAddContactBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContactsViewModel by viewModels({requireParentFragment()})

    private val requestGalleryAccessPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        if (it) {
            launchPickPhoto()
        }
    }

    private val pickMediaLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        this.uri = uri?: Uri.EMPTY
        binding.imageViewAvatar.setImageURI(uri)
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddContactBinding.inflate(layoutInflater)
        with(binding) {

            arguments?.let {
                textInputName.editText?.setText(it.getString(KEY_SAVE_STATE_NAME))
                textInputJob.editText?.setText(it.getString(KEY_SAVE_STATE_JOB))
                textInputPhoneNumber.editText?.setText(it.getString(KEY_SAVE_STATE_PHONE_NUMBER))
                textInputPostalAddress.editText?.setText(it.getString(KEY_SAVE_STATE_POSTAL_ADDRESS))
                uri = Uri.parse(it.getString(KEY_SAVE_STATE_PHOTO_URI))
                if (uri != Uri.EMPTY) {
                    binding.imageViewAvatar.setImageURI(uri)
                }
            }

            val dialog = AlertDialog.Builder(requireContext()).setView(root).create()
            setListenerForAddImageViews(this)
            buttonConfirm.setOnClickListener(getAddUserDialogListener())
            buttonCancel.setOnClickListener { dialog.dismiss() }
            return dialog
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        pickMediaLauncher.unregister()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        with(outState) {
            putString(KEY_SAVE_STATE_NAME, binding.textInputName.editText?.text.toString())
            putString(KEY_SAVE_STATE_JOB, binding.textInputJob.editText?.text.toString())
            putString(KEY_SAVE_STATE_PHOTO_URI, uri.toString())
            putString(
                KEY_SAVE_STATE_PHONE_NUMBER,
                binding.textInputPhoneNumber.editText?.text.toString()
            )
            putString(
                KEY_SAVE_STATE_POSTAL_ADDRESS,
                binding.textInputPostalAddress.editText?.text.toString()
            )
        }

    }

    private fun setListenerForAddImageViews(dialogBinding: DialogAddContactBinding) {

        val listener = View.OnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                if (requireContext().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) !=
                    PackageManager.PERMISSION_GRANTED
                ) {

                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)) {
                        openAppSettings()
                    } else {
                        requestGalleryAccessPermission()
                    }


                } else {
                    launchPickPhoto()
                }
            } else {
                if (requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED
                ) {

                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
                        requestGalleryAccessPermission()
                    else
                        openAppSettings()

                } else {
                    launchPickPhoto()
                }
            }
        }
        dialogBinding.textViewAddPhoto.setOnClickListener(listener)
        dialogBinding.imageViewAvatar.setOnClickListener(listener)
    }

    private fun requestGalleryAccessPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestGalleryAccessPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            requestGalleryAccessPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun launchPickPhoto() {
        pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun getAddUserDialogListener(): View.OnClickListener {

        return View.OnClickListener {
            viewModel.addNewUser(
                name = binding.textInputName.editText?.text.toString(),
                job = binding.textInputJob.editText?.text.toString(),
                photo = uri.toString(),
                phone = binding.textInputPhoneNumber.editText?.text.toString(),
                address = binding.textInputPostalAddress.editText?.text.toString()
            )
            dialog?.dismiss()
        }

    }

}