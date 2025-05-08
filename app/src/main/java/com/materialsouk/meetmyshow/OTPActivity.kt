package com.materialsouk.meetmyshow

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.firestore.FirebaseFirestore
import com.materialsouk.meetmyshow.databinding.ActivityOtpactivityBinding
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {

    // Region: Properties
    private lateinit var binding: ActivityOtpactivityBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var loadingDialog: Dialog
    private var verificationId: String? = null
    private val firestore = FirebaseFirestore.getInstance()

    // Region: Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeFirebase()
        setupBinding()
        setupLoadingDialog()
        setupFirebaseAuth()
        setupUI()
        startPhoneNumberVerification()
    }

    // Region: Initialization
    private fun initializeFirebase() {
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otpactivity)
    }

    private fun setupLoadingDialog() {
        loadingDialog = Dialog(this).apply {
            setContentView(R.layout.loading_layout)
            window?.setLayout(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setCancelable(false)
        }
    }

    private fun setupFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance()
    }

    // Region: Setup UI
    private fun setupUI() {
        binding.verifyOTPBtn.setOnClickListener {
            val otpCode = binding.edOtp.text.toString().trim()
            when {
                TextUtils.isEmpty(otpCode) -> showToast("Please enter OTP")
                else -> verifyPhoneNumberWithCode(otpCode)
            }
        }
    }

    // Region: Phone Auth
    private fun startPhoneNumberVerification() {
        val phoneNumber = "+91${intent.getStringExtra("phone_no").orEmpty()}"

        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(verificationCallbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val verificationCallbacks = object : OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            credential.smsCode?.let { code ->
                binding.edOtp.setText(code)
                verifyPhoneNumberWithCode(code)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            showToast(e.message ?: "Phone verification failed")
        }

        override fun onCodeSent(
            verificationId: String,
            token: ForceResendingToken
        ) {
            this@OTPActivity.verificationId = verificationId
        }
    }

    private fun verifyPhoneNumberWithCode(code: String) {
        val credential = verificationId?.let {
            PhoneAuthProvider.getCredential(it, code)
        } ?: run {
            showToast("Verification code is invalid")
            return
        }

        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        loadingDialog.show()

        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    registerNewUser()
                } else {
                    loadingDialog.dismiss()
                    showToast(task.exception?.message ?: "Authentication failed")
                }
            }
    }

    // Region: User Registration
    private fun registerNewUser() {
        val email = intent.getStringExtra("email").orEmpty()
        val password = intent.getStringExtra("password").orEmpty()

        if (email.isEmpty() || password.isEmpty()) {
            loadingDialog.dismiss()
            showToast("Email and password are required")
            return
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveUserToFirestore()
                } else {
                    loadingDialog.dismiss()
                    showToast(task.exception?.message ?: "Registration failed")
                }
            }
    }

    private fun saveUserToFirestore() {
        val userId = mAuth.currentUser?.uid ?: run {
            loadingDialog.dismiss()
            showToast("User ID not available")
            return
        }

        val userData = hashMapOf<String, Any>(
            "id" to userId,
            "username" to intent.getStringExtra("username").orEmpty(),
            "email_id" to intent.getStringExtra("email").orEmpty(),
            "phone_no" to intent.getStringExtra("phone_no").orEmpty()
        )

        firestore.collection("Users")
            .document(userId)
            .set(userData)
            .addOnCompleteListener { task ->
                loadingDialog.dismiss()

                if (task.isSuccessful) {
                    navigateToMainActivity()
                } else {
                    showToast(task.exception?.message ?: "Failed to save user data")
                    // Rollback: Delete user if Firestore save fails
                    mAuth.currentUser?.delete()
                }
            }
    }

    // Region: Navigation
    private fun navigateToMainActivity() {
        Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
        }
        finish()
    }

    // Region: Helpers
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}