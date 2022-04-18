package com.example.storydicoding.ui.addstory

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.storydicoding.R
import com.example.storydicoding.createCustomTempFile
import com.example.storydicoding.databinding.FragmentAddStoryBinding
import com.example.storydicoding.reduceFileImage
import com.example.storydicoding.ui.detailstory.DetailStoryFragment
import com.example.storydicoding.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryFragment : DialogFragment() {
    private var _binding: FragmentAddStoryBinding? = null
    private var token: String? = null
    private var getFile: File? = null

    private val binding get() = _binding!!
    private val addStoryViewModel by viewModels<AddStoryViewModel>()

    private lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupArguments()
        setupViewAction()
    }

    private fun setupArguments() {
        if (arguments != null) {
            token = arguments?.getString(TOKEN)
        }
    }

    private fun setupViewAction() {
        binding.apply {
            btnCamera.setOnClickListener {
                openCamera()
            }
            btnGallery.setOnClickListener {
                openGallery()
            }
            btnUpload.setOnClickListener {
                val desc = etDesc.text.toString()
                if (desc.isNotEmpty()) {
                    addNewStory(desc)
                } else {
                    etDesc.error = getString(R.string.error_desc_story)
                }
            }
        }
    }

    private fun openCamera() {
        val mPackageManager = activity?.packageManager
        val mApplication = activity?.application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (mPackageManager != null && mApplication != null) {
            intent.resolveActivity(mPackageManager)
            createCustomTempFile(mApplication).also {
                val photoURI: Uri =
                    FileProvider.getUriForFile(requireContext(), "com.example.storydicoding", it)
                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherIntentCamera.launch(intent)
            }
        }
    }

    private fun openGallery() {
        launcherIntentGallery.launch(Intent.createChooser(Intent().also {
            it.action = ACTION_GET_CONTENT
            it.type = "image/*"
        }, "Choose a Picture?"))
    }

    private fun addNewStory(desc: String) {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val description = desc.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part =
                MultipartBody.Part.createFormData("photo", file.name, requestImageFile)

            addStoryViewModel.addNewStory("Bearer $token", description, imageMultipart)
                .observe(this) {
                    val message: String = if (!it) {
                        getString(R.string.message_success_add_story)
                    } else {
                        getString(R.string.message_fail_add_story)
                    }
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    dialog?.dismiss()
                }
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.warning_image_story),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onStart() {
        super.onStart()
        DetailStoryFragment.dialogLayoutSetting(dialog)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            reduceFileImage(myFile)
            Glide.with(requireView())
                .load(result)
                .apply(RequestOptions())
                .into(binding.ivPhoto)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())

            getFile = myFile
            Glide.with(requireView())
                .load(selectedImg)
                .apply(RequestOptions())
                .into(binding.ivPhoto)
        }
    }

    companion object {
        const val TOKEN = "token"
    }
}