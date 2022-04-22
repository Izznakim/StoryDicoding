package com.example.storydicoding.ui.addstory

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.storydicoding.R
import com.example.storydicoding.createCustomTempFile
import com.example.storydicoding.databinding.ActivityAddStoryBinding
import com.example.storydicoding.reduceFileImage
import com.example.storydicoding.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddStoryBinding
    private lateinit var currentPhotoPath: String

    private var token: String? = null
    private var getFile: File? = null
    private val addStoryViewModel by viewModels<AddStoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title=getString(R.string.add_story)
        token=intent.getStringExtra(TOKEN)

        setupViewAction()
        setupProgressBar()
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

    private fun setupProgressBar() {
        addStoryViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri =
                FileProvider.getUriForFile(this, "com.example.storydicoding", it)
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun openGallery() {
        launcherIntentGallery.launch(Intent.createChooser(Intent().also {
            it.action = Intent.ACTION_GET_CONTENT
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
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    finish()
                }
        } else {
            Toast.makeText(
                this,
                getString(R.string.warning_image_story),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            reduceFileImage(myFile)
            Glide.with(this)
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
            val myFile = uriToFile(selectedImg, this)

            getFile = myFile
            Glide.with(this)
                .load(selectedImg)
                .apply(RequestOptions())
                .into(binding.ivPhoto)
        }
    }

    companion object {
        const val TOKEN = "token"
    }
}