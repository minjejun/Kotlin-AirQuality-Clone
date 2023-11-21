package com.jejunMin.airquality

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jejunMin.airquality.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityMapBinding

    private var mMap : GoogleMap? = null
    var currentLat : Double = 0.0
    var currentLong : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentLat = intent.getDoubleExtra("currentLat", 0.0)
        currentLong = intent.getDoubleExtra("currentLong", 0.0)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this) // OnMapReadyCallback을 클래스에서 선언해줘서 this로 호출 가능.

        setButton()
    }

    private fun setButton() {
        binding.btnCheckHere.setOnClickListener {
            mMap?.let {
                val intent = Intent()
                intent.putExtra("latitude", it.cameraPosition.target.latitude )
                intent.putExtra("longitude", it.cameraPosition.target.longitude )
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        binding.fabCurrentLocation.setOnClickListener {
            val locationProvider = LocationProvider(this@MapActivity)
            val latitude = locationProvider.getLocationLatitude()
            val longitude = locationProvider.getLocationLongitude()

            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude!!, longitude!!), 16f))
            setMarker()
        }
    }

    override fun onMapReady(googleMap : GoogleMap) {
        mMap = googleMap

        mMap?.let  {
            val currentLocation = LatLng(currentLat, currentLong)
            it.setMaxZoomPreference(20.0f) // 줌 최댓값 설정
            it.setMinZoomPreference(12.0f) // 줌 최솟값 설정
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f))
            setMarker()
        }
    }

    private fun setMarker() {
        mMap?.let {// null이 아닌 경우 실행.
            it.clear()
            val markerOption = MarkerOptions()
            markerOption.position(it.cameraPosition.target)
            markerOption.title("Marker 위치")
            val marker = it.addMarker(markerOption) // 반환.

            it.setOnCameraMoveListener {
                marker?.let { marker -> // it이 여러개라 헷갈릴 수 있기에 따로 표시.
                    marker.position = it.cameraPosition.target // it -> 구글 맵
                }
            }
        }
    }
}