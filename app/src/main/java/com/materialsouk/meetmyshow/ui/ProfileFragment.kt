package com.materialsouk.meetmyshow.ui

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.materialsouk.meetmyshow.R
import com.materialsouk.meetmyshow.databinding.FragmentProfileBinding
import timber.log.Timber

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var loadingDialog: Dialog
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        setupLoadingDialog()
        loadUserData()
        return binding.root
    }

    private fun setupLoadingDialog() {
        loadingDialog = Dialog(requireContext()).apply {
            setContentView(R.layout.loading_layout)
            window?.setLayout(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setCancelable(false)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        loadingDialog.show()
    }

    private fun loadUserData() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            handleUserNotLoggedIn()
            return
        }

        firestore.collection("Users")
            .document(currentUser.uid)
            .get()
            .addOnSuccessListener { document ->
                loadingDialog.dismiss()

                if (!document.exists()) {
                    Timber.e("User document not found in Firestore")
                    showToast("User data not found")
                    return@addOnSuccessListener
                }

                try {
                    val userData = document.data ?: throw Exception("Empty user data")

                    binding.apply {
                        edUserName.setText(userData["username"]?.toString().orEmpty())
                        edEmailId.setText(userData["email_id"]?.toString().orEmpty())
                        edPhoneNum.setText(userData["phone_no"]?.toString().orEmpty())
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Error parsing user data")
                    showToast("Error loading profile")
                }
            }
            .addOnFailureListener { exception ->
                loadingDialog.dismiss()
                Timber.e(exception, "Failed to load user data")
                showToast("Failed to load profile: ${exception.localizedMessage}")
            }
    }

    private fun handleUserNotLoggedIn() {
        loadingDialog.dismiss()
        Timber.w("No authenticated user found")
        showToast("Please login first")
        (activity as? Activity)?.finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}