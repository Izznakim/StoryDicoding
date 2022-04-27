package com.example.storydicoding.ui.maps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.storydicoding.R
import com.example.storydicoding.databinding.ActivityMapsBinding
import com.example.storydicoding.setupView
import com.example.storydicoding.ui.addstory.AddStoryActivity
import com.example.storydicoding.ui.detailstory.DetailActivity
import com.example.storydicoding.ui.main.MainActivity

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val mapsViewModel by viewModels<MapsViewModel>()
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        token = intent.getStringExtra(AddStoryActivity.TOKEN)

        setupView(window)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setupViewModel()
    }

    private fun setupViewModel() {
        mapsViewModel.getListStoryMaps("Bearer $token").observe(this) {

            val firstPos=LatLng(it[0].lat,it[0].lon)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstPos, 5f))

            it.forEach { story ->
                val posLatLng = LatLng(story.lat, story.lon)
                mMap.addMarker(
                    MarkerOptions().position(posLatLng).title(story.name)
                        .snippet(story.description)
                )
                mMap.setOnInfoWindowClickListener {
                    startActivity(
                        Intent(this, DetailActivity::class.java).putExtra(
                            DetailActivity.STORY,
                            story
                        )
                    )
                }
            }
        }

        mapsViewModel.error.observe(this) {
            val message = if (!it) {
                getString(R.string.message_success_list_story_map_mode)
            } else {
                getString(R.string.message_fail_list_story_map_mode)
            }
            Snackbar.make(window.decorView, message, Snackbar.LENGTH_SHORT).show()
        }
    }
}