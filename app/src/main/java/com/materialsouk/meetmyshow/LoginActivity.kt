package com.materialsouk.meetmyshow

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.materialsouk.meetmyshow.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loadingDialog: Dialog
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Khởi tạo Firebase
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }
        auth = FirebaseAuth.getInstance()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setupLoadingDialog()
        setupListeners()
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

    private fun setupListeners() {
        binding.apply {
            signInBtn.setOnClickListener { v ->
                validation()
                hideKeyboard(v)
            }

            createAccBtn.setOnClickListener { v ->
                hideKeyboard(v)
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }

            edPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    validatePasswordField()
                }
            })
        }
    }

    private fun validatePasswordField() {
        binding.apply {
            when {
                edPassword.text.toString().trim().isEmpty() -> {
                    txtPasswordL.error = "Required"
                }
                edPassword.text.toString().trim().length < 8 -> {
                    txtPasswordL.error = "Password must be 8 characters!"
                }
                else -> {
                    txtPasswordL.error = null
                }
            }
        }
    }

    private fun validation() {
        binding.apply {
            val email = edEmailId.text.toString().trim()
            val password = edPassword.text.toString().trim()

            when {
                email.isEmpty() -> {
                    edEmailId.error = "Required"
                }
                !isValidEmail(email) -> {
                    edEmailId.error = "Enter valid email!"
                }
                password.isEmpty() -> {
                    txtPasswordL.error = "Required"
                }
                password.length < 8 -> {
                    txtPasswordL.error = "Password must be 8 characters!"
                }
                else -> {
                    txtPasswordL.error = null
                    signIn(email, password)
                }
            }
        }
    }

    private fun signIn(email: String, password: String) {
        loadingDialog.show()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                loadingDialog.dismiss()

                if (task.isSuccessful) {
                    navigateToMainActivity()
                } else {
                    showErrorMessage(task.exception?.message ?: "Authentication failed")
                }
            }
    }

    private fun navigateToMainActivity() {
        startActivity(
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        )
        finish()
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun hideKeyboard(view: View) {
        try {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            // Log error if needed
        }
    }
}