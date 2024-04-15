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

        binding.btnAddProduct.setOnClickListener {
            var intent = Intent(this, AddProductsActivity::class.java)
            startActivity(intent)
        }
        binding.btnUpdateProduct.setOnClickListener {
            var intent = Intent(this, UpdateProductsActivity::class.java)
            startActivity(intent)
        }
        binding.btnDeleteProduct.setOnClickListener {
            var intent = Intent(this, DeleteProductsActivity::class.java)
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