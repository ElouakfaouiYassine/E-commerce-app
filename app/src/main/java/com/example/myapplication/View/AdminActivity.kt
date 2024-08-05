package com.example.myapplication.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnShowProducts.setOnClickListener {
            var intent = Intent(this, ProductListActivity::class.java)
            startActivity(intent)
        }
        binding.btnShowUser.setOnClickListener {
            val fragment = Info_User_AdminFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame_Admin, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}