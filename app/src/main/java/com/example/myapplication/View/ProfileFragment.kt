package com.example.myapplication.View

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.Repository.DataBaseUser
import android.Manifest
class ProfileFragment : Fragment() {
    lateinit var tv_Edit: ImageView
    lateinit var profileImage: ImageView
    lateinit var tv_Favorite : TextView
    lateinit var tv_Order : TextView
    lateinit var tv_Payment : TextView
    lateinit var tv_username: TextView
    lateinit var tv_email: TextView
    lateinit var db: DataBaseUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_Edit = view.findViewById(R.id.btn_edit)
        tv_Favorite= view.findViewById(R.id.tv_favorite)
        tv_Payment = view.findViewById(R.id.tv_payment)
        tv_Order = view.findViewById(R.id.tv_order)
        tv_username = view.findViewById(R.id.username_profile)
        tv_email = view.findViewById(R.id.email_profile)
        profileImage = view.findViewById(R.id.profile_image)
        db = DataBaseUser(requireContext())

        loadUserInfo()
        setClickListeners()

    }

    private fun loadUserInfo() {
        db.getInfoUtilusateur()?.use { cursor ->
            if (cursor.moveToFirst()) {
                val profileImageUriIndex = cursor.getColumnIndex(DataBaseUser.COLUMN_PROFILE_IMAGE_URI)
                val usernameIndex = cursor.getColumnIndex(DataBaseUser.COLUMN_USERNAME)
                val emailIndex = cursor.getColumnIndex(DataBaseUser.COLUMN_EMAIL)

                val profileImageUri = cursor.getString(profileImageUriIndex)
                val username = cursor.getString(usernameIndex)
                val email = cursor.getString(emailIndex)

                // Load profile image if URI exists
                if (!profileImageUri.isNullOrEmpty()) {
                    profileImage.setImageURI(Uri.parse(profileImageUri))
                }
                tv_username.text = username
                tv_email.text = email
            }
        }
    }

    private fun setClickListeners() {
        // Set click listeners for various actions

        tv_Edit.setOnClickListener {
            val intent = Intent(activity, UpdateProfileActivity::class.java)
            startActivity(intent)
        }

        tv_Favorite.setOnClickListener {
            navigateToFragment(FavoriteFragment())
        }

        tv_Payment.setOnClickListener {
            navigateToFragment(PaymentFragment())
        }

        tv_Order.setOnClickListener {
            navigateToFragment(OrderFragment())
        }

        profileImage.setOnClickListener {
            openGalleryForImage()
        }
    }
    private fun setProfileImage(imageUri: Uri?) {
        profileImage.setImageURI(imageUri)
    }

    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    openGalleryForImage()
                } else {
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openGalleryForImage() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }
    // Override onActivityResult to handle image selection result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            imageUri?.let {
                val email = tv_email.text.toString()
                if (email.isNotEmpty()) {
                    db.updateUserProfileImage(email, it.toString())
                    profileImage.setImageURI(it)
                }
            }
        }
    }




    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
        private const val PICK_IMAGE_REQUEST = 1
    }
}