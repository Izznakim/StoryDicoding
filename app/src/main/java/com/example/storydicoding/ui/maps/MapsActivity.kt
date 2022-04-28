package com.example.storydicoding.ui.maps

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storydicoding.R
import com.example.storydicoding.databinding.ActivityMapsBinding
import com.example.storydicoding.setupView
import com.example.storydicoding.ui.addstory.AddStoryActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
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

        getListStoryMaps()
        setMapStyle()
    }

    private fun getListStoryMaps() {
        mapsViewModel.getListStoryMaps("Bearer $token").observe(this) {

            val firstPos = LatLng(it[0].lat, it[0].lon)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstPos, 5f))

            it.forEach { story ->
                val posLatLng = LatLng(story.lat, story.lon)
                mMap.addMarker(
                    MarkerOptions().position(posLatLng).title(story.name)
                        .snippet(story.description)
                )
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

    private fun setMapStyle() {
        var message = ""
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                message = getString(R.string.fail_msg_parsing_map_style)
            }
        } catch (e: Resources.NotFoundException) {
            message = getString(R.string.exception_msg_parsing_map_style, e)
        }
        Snackbar.make(window.decorView, message, Snackbar.LENGTH_SHORT).show()
    }
}