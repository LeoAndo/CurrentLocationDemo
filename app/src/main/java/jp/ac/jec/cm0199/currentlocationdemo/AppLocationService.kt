package jp.ac.jec.cm0199.currentlocationdemo

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

// TODO STEP-3 アプリで利用する位置情報取得サービスの追加- START
class AppLocationService(private val context: Context) {
    companion object {
        // logcatで使うTAG
        private const val TAG = "AppLocationService"

        /**
         * シングルトンパターン(アプリで唯一のインスタンスにする設計)を採用したコード.
         * jetpackのroomライブラリ内部実装で採用しているLazy initializationというパターンを採用したサンプルコード
         * https://en.wikipedia.org/wiki/Double-checked_locking#Usage_in_Java
         * https://en.wikipedia.org/wiki/Singleton_pattern
         *
         * contextはメモリ領域を多く確保するため、シングルトンで持つとよくない.
         */
        /*
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: AppLocationService? = null
        fun getInstance(context: Context): AppLocationService {
            /*
            if (INSTANCE != null) {
                return INSTANCE!!
            } else {
                synchronized(AppLocationService::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = AppLocationService(context)
                    }
                    return INSTANCE!!
                }
            }
             */
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppLocationService(context).also { INSTANCE = it }
            }
        }
         */
    }

    // 位置情報サービス
    private val locationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // 取得成功時のLiveData
    private val _locationResult = MutableLiveData<Location>()
    val locationResult: LiveData<Location> get() = _locationResult

    // 取得エラー時のLiveData
    private val _fetchError = MutableLiveData<String>()
    val fetchError: LiveData<String> get() = _fetchError

    /**
     * 現在の位置情報を取得する.
     */
    fun fetchCurrentLocation() {
        if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            _fetchError.value = "アプリの位置情報パーミッションを許可する必要があります"
            return
        }

        locationClient.lastLocation.addOnSuccessListener { location ->
            Log.d(TAG, "location: $location")
            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 500).build()
            locationClient.requestLocationUpdates(
                request, context.mainExecutor, object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        super.onLocationResult(result)

                        val lastLocation = result.lastLocation
                        if (lastLocation == null) {
                            _fetchError.value = "現在地取得に失敗しました"
                            return
                        }

                        Log.d(
                            TAG,
                            "onLocationResult latitude: ${lastLocation.latitude} longitude: ${lastLocation.longitude}"
                        )
                        _locationResult.value = lastLocation
                        // 現在地だけ欲しいので、1回取得したら位置情報取得をやめる
                        locationClient.removeLocationUpdates(this)
                    }

                    override fun onLocationAvailability(p0: LocationAvailability) {
                        super.onLocationAvailability(p0)
                        // 位置情報OFFの時にここに入る
                        Log.d(TAG, "onLocationAvailability")
                        _fetchError.value = "端末の位置情報設定をONにする必要があります"
                    }
                })
        }.addOnFailureListener {
            Log.d(TAG, "error: $it")
            _fetchError.value = "現在地取得に失敗しました\n ${it.localizedMessage}"
        }
    }
}
// END
