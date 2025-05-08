package com.materialsouk.meetmyshow

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.textfield.TextInputLayout
import com.materialsouk.meetmyshow.databinding.ActivityRegisterBinding
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var isValidPassword = false
    private var isValidConPassword = false
    private lateinit var loadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        setupViews()
        setupLoadingDialog()
    }

    private fun setupViews() {
        binding.signUpBtn.setOnClickListener { validateAndProceed() }

        binding.edPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                validatePassword(binding.edPassword, binding.txtPasswordL, "password")
            }
        })

        binding.edConPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                validatePassword(binding.edConPassword, binding.txtConPasswordL, "conPassword")
            }
        })
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

    private fun validateAndProceed() {
        when {
            binding.edUserName.text.toString().trim().isEmpty() -> {
                binding.edUserName.error = "Vui lòng nhập tên"
            }
            binding.edEmailId.text.toString().trim().isEmpty() -> {
                binding.edEmailId.error = "Vui lòng nhập email"
            }
            !isValidEmail(binding.edEmailId.text.toString().trim()) -> {
                binding.edEmailId.error = "Email không hợp lệ"
            }
            binding.edPhoneNum.text.toString().trim().isEmpty() -> {
                binding.edPhoneNum.error = "Vui lòng nhập số điện thoại"
            }
            !isValidVietnamesePhone(binding.edPhoneNum.text.toString().trim()) -> {
                binding.edPhoneNum.error = "Số điện thoại không hợp lệ"
            }
            else -> {
                validatePassword(binding.edPassword, binding.txtPasswordL, "password")
                if (isValidPassword) {
                    validatePassword(binding.edConPassword, binding.txtConPasswordL, "conPassword")
                    if (isValidConPassword) {
                        if (binding.edPassword.text.toString() != binding.edConPassword.text.toString()) {
                            binding.txtConPasswordL.error = "Mật khẩu không khớp"
                        } else {
                            proceedToOTPVerification()
                        }
                    }
                }
            }
        }
    }

    private fun proceedToOTPVerification() {
        val phone = normalizeVietnamesePhone(binding.edPhoneNum.text.toString().trim())

        Intent(this, OTPActivity::class.java).apply {
            putExtra("email", binding.edEmailId.text.toString().trim())
            putExtra("username", binding.edUserName.text.toString().trim())
            putExtra("phone_no", phone)
            startActivity(this)
        }
    }

    private fun normalizeVietnamesePhone(phone: String): String {
        return when {
            phone.startsWith("+84") -> phone
            phone.startsWith("0") -> "+84" + phone.substring(1)
            else -> "+84$phone"
        }
    }

    private fun validatePassword(
        editText: EditText,
        textInputLayout: TextInputLayout,
        state: String
    ) {
        val password = editText.text.toString().trim()
        when {
            password.isEmpty() -> {
                textInputLayout.error = "Vui lòng nhập mật khẩu"
                updatePasswordState(false, state)
            }
            password.length < 8 -> {
                textInputLayout.error = "Mật khẩu phải từ 8 ký tự"
                updatePasswordState(false, state)
            }
            else -> {
                textInputLayout.error = null
                updatePasswordState(true, state)
            }
        }
    }

    private fun updatePasswordState(isValid: Boolean, state: String) {
        when (state) {
            "password" -> isValidPassword = isValid
            "conPassword" -> isValidConPassword = isValid
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidVietnamesePhone(phone: String): Boolean {
        val pattern = Pattern.compile("^(0|\\+84)(3|5|7|8|9)[0-9]{8}$")
        return pattern.matcher(phone).matches()
    }
}