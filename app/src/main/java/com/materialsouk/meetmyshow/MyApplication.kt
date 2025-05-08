package com.materialsouk.meetmyshow

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Timber first for debugging
        setupTimber()

        // Initialize Firebase
        initializeFirebase()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                // Add line number to logs for better debugging
                override fun createStackElementTag(element: StackTraceElement): String {
                    return "(${element.fileName}:${element.lineNumber})#${element.methodName}"
                }
            })
        }
    }

    private fun initializeFirebase() {
        try {
            // Sử dụng Firebase KTX để khởi tạo
            if (FirebaseApp.getApps(this).isEmpty()) {
                Firebase.initialize(this)
                Timber.d("Firebase initialized successfully with KTX")

                // Kiểm tra các SDK đã được khởi tạo
                checkFirebaseServices()
            }
        } catch (e: Exception) {
            Timber.e(e, "Firebase initialization failed")
            // Có thể thêm xử lý fallback tại đây
        }
    }

    private fun checkFirebaseServices() {
        try {
            // Kiểm tra từng service Firebase đã được enable trong Firebase Console
            val firebaseApp = FirebaseApp.getInstance()
            Timber.d("FirebaseApp name: ${firebaseApp.name}")
            Timber.d("FirebaseApp options: ${firebaseApp.options}")

            // Log các services đã khởi tạo
            Timber.d("Firebase Auth available: ${try { Firebase.auth; true } catch (e: Exception) { false }}")
            Timber.d("Firebase Firestore available: ${try { Firebase.firestore; true } catch (e: Exception) { false }}")
        } catch (e: Exception) {
            Timber.e(e, "Firebase services check failed")
        }
    }
}