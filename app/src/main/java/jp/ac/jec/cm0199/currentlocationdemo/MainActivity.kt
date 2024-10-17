package jp.ac.jec.cm0199.currentlocationdemo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }


    // TODO STEP-5 オプトインの処理: 位置情報をの権限リクエスト - START
    /*
    private val fetchCurrentLocation =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            Log.d(TAG, "granted: $granted")
            if (granted) {
                // 位置情報の権限付与した場合、現在地取得を行う
                appLocationService.fetchCurrentLocation()
            } else {
                Snackbar.make(
                    window.decorView,
                    "位置情報取得の許可を行ってください",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
     */
    // END

    // TODO STEP-4 位置情報取得サービスを呼び出す - START
    // private lateinit var appLocationService: AppLocationService
    // END

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // TODO STEP-4 位置情報取得サービスを呼び出す - START
        /*
        appLocationService = AppLocationService(this)
        appLocationService.locationResult.observe(this) {
            Log.d(TAG, "取得した位置情報:::::: $it")
        }
        appLocationService.fetchError.observe(this) {
            Log.d(TAG, it)
        }
         */
        // END

        // TODO STEP-5 オプトインの処理: 位置情報をの権限リクエスト - START
//        findViewById<Button>(R.id.button_current_location).setOnClickListener {
//            fetchCurrentLocation.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
//        }
        // END
    }
}