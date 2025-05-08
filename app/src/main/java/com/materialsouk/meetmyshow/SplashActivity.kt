package com.materialsouk.meetmyshow

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class SplashActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private var isNavigationCompleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Khởi tạo Firebase an toàn
        val firebaseApp = initializeFirebase()
        if (firebaseApp == null) {
            Timber.e("Không thể khởi tạo Firebase, chuyển đến màn hình login")
            navigateToLoginAndFinish()
            return
        }

        // Sử dụng FirebaseAuth an toàn
        val auth = FirebaseAuth.getInstance(firebaseApp)

        handler.postDelayed({
            if (!isNavigationCompleted) {
                checkAuthAndNavigate(auth)
            }
        }, 500)
    }

    private fun initializeFirebase(): FirebaseApp? {
        return try {
            // Thử lấy instance hiện có
            FirebaseApp.getInstance()
        } catch (e: IllegalStateException) {
            Timber.w("Firebase chưa khởi tạo, đang thử khởi tạo...")
            try {
                // Khởi tạo và trả về instance mới
                FirebaseApp.initializeApp(this)?.also {
                    Timber.d("Khởi tạo Firebase thành công")
                }
            } catch (ex: Exception) {
                Timber.e(ex, "Không thể khởi tạo Firebase")
                null
            }
        }
    }

    private fun checkAuthAndNavigate(auth: FirebaseAuth) {
        try {
            val currentUser = auth.currentUser
            val destination = if (currentUser != null) {
                Timber.d("Người dùng đã đăng nhập, chuyển đến MainActivity")
                MainActivity::class.java
            } else {
                Timber.d("Chưa đăng nhập, chuyển đến LoginActivity")
                LoginActivity::class.java
            }

            startActivity(Intent(this, destination))
            safeFinish()
        } catch (e: Exception) {
            Timber.e(e, "Lỗi kiểm tra auth, chuyển đến login")
            navigateToLoginAndFinish()
        }
    }

    private fun navigateToLoginAndFinish() {
        startActivity(Intent(this, LoginActivity::class.java))
        safeFinish()
    }

    private fun safeFinish() {
        if (!isFinishing && !isDestroyed) {
            isNavigationCompleted = true
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}