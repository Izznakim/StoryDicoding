package com.example.storydicoding.ui.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.storydicoding.R
import com.example.storydicoding.createCustomTempFile
import com.example.storydicoding.databinding.ActivityAddStoryBinding
import com.example.storydicoding.reduceFileImage
import com.example.storydicoding.ui.main.MainActivity
import com.example.storydicoding.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var currentPhotoPath: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var token: String? = null
    private var getFile: File? = null
    private var lat: Float? = null
    private var lon: Float? = null
    private val addStoryViewModel by viewModels<AddStoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.add_story)
        token = intent.getStringExtra(TOKEN)

        setupViewAction()
        setupProgressBar()
        fusedLocationClient=LocationServices.getFusedLocationProviderClient(this)
        getMyLastLocation()
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

            addStoryViewModel.addNewStory("Bearer $token", description, imageMultipart, lat, lon)
                .observe(this) {
                    val message: String = if (!it) {
                        getString(R.string.message_success_add_story)
                    } else {
                        getString(R.string.message_fail_add_story)
                    }
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    Intent(this, MainActivity::class.java).also { intent ->
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        when {
            permission[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> getMyLastLocation()
            permission[Manifest.permission.ACCESS_COARSE_LOCATION]?:false->getMyLastLocation()
            else->{}
        }
    }

    private fun checkPermission(permission:String):Boolean{
        return ContextCompat.checkSelfPermission(this,permission)==PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation(){
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)&&checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)){
            fusedLocationClient.lastLocation.addOnSuccessListener { location:Location?->
                if (location!=null){
                    val posistion=LatLng(location.latitude,location.longitude)
                    lat=location.latitude.toFloat()
                    lon=location.longitude.toFloat()
                    Log.d(TAG, "getMyLastLocation: $posistion")
                }else{
                    Toast.makeText(
                        this,
                        getString(R.string.location_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }else{
            requestPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    }

    companion object {
        const val TOKEN = "token"
        private const val TAG = "AddStoryActivity"
    }
}