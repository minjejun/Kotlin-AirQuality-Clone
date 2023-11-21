package com.jejunMin.airquality

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LocationProvider(val context: Context) { // gps나 네트워크의 위치를 통하여 위도와 경도 정보를 얻는 것.
    private var location: Location? = null
    private var locationManager: LocationManager? = null

    init {
        getLocation();
    }

    private fun getLocation(): Location? {
        try {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            var gpsLocation: Location? = null
            var networkLocation: Location? = null

            // GPS Provider와 Network Provider가 활성화 되어있는지 확인.
            val isGPSEnabled : Boolean =
                locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled : Boolean =
                locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) { // 둘 다 사용 불가능한 상황 -> null 반환.
                return null
            } else {
                // 만약 위 두개 권한이 없다면 null 반환.
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return null
                }
                // 네트워크를 통한 위치 파악이 가능한 경우에 위치를 가져옴
                if (isNetworkEnabled) {
                    networkLocation =
                        locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }

                // GPS를 통한 위치 파악이 가능한 경우에 위치를 가져옴.
                if (isGPSEnabled) {
                    networkLocation =
                        locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                }

                if (gpsLocation != null && networkLocation != null) {
                    // 만약 두 개의 위치가 있다면 정확도가 높은 것을 선택.
                    if (gpsLocation.accuracy > networkLocation.accuracy) {
                        location = gpsLocation
                        return gpsLocation
                    } else {
                        location = networkLocation
                        return networkLocation
                    }
                } else {
                    if (gpsLocation != null) {
                        location = gpsLocation
                    } else {
                        location = networkLocation
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return location
    }

    // 위도 정보 가져오는 함수
    fun getLocationLatitude(): Double {
        return location?.latitude ?: 0.0 // null일 때는 0.0을 반환한다는 뜻.
    }

    // 경도 정보 가져오는 함수
    fun getLocationLongitude(): Double {
        return location?.longitude ?: 0.0 // null일 때는 0.0을 반환한다는 뜻.
    }
}