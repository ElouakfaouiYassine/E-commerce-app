package com.example.myapplication.View

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
        private lateinit var binding: ActivityMainBinding
        private lateinit var drawerLayout: DrawerLayout
        private lateinit var sharedPreferences: SharedPreferences

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            drawerLayout = binding.drawerLayout
            val toolbar: Toolbar = binding.toolbar
            setSupportActionBar(toolbar)

            sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)


            // Check if the user is logged in
            if (sharedPreferences.getBoolean("isLoggedIn", false)) {
                binding.tvSignup.visibility = View.GONE
                binding.tvLogin.visibility = View.GONE
                binding.tvLogout.visibility = View.VISIBLE

                binding.tvLogout.setOnClickListener {
                    showLogoutConfirmationDialog()
                }

                if (sharedPreferences.getBoolean("isAdmin", false)) {
                    binding.tvAdmin.visibility = View.VISIBLE
                } else {
                    binding.tvAdmin.visibility = View.GONE
                }
                /*val username = sharedPreferences.getString("username", "")
                binding.tvUser.text = "Welcome, $username!"
                binding.tvUser.visibility = View.VISIBLE*/
                /*// Set click listener for "Log out" TextView
                binding.tvLogout.setOnClickListener {
                    showLogoutConfirmationDialog()
                }

                // Retrieve isAdmin flag from SharedPreferences
                val isAdmin = sharedPreferences.getBoolean("isAdmin", false)
                if (isAdmin) {
                    binding.tvAdmin.visibility = View.VISIBLE
                } else {
                    binding.tvAdmin.visibility = View.GONE
                }*/

            } else {
                binding.tvSignup.visibility = View.VISIBLE
                binding.tvLogin.visibility = View.VISIBLE
                binding.tvLogout.visibility = View.GONE
                binding.tvAdmin.visibility = View.GONE
            }



            val navigationView: NavigationView = binding.navView
            navigationView.setNavigationItemSelectedListener(this)
            val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.open, R.string.close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()


            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, HomeFragment())
                        .commit()
                navigationView.setCheckedItem(R.id.home)
            }


            binding.tvLogin.setOnClickListener {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
            binding.tvSignup.setOnClickListener {
                val intent = Intent(this, SignUP::class.java)
                startActivity(intent)
            }
            binding.tvAdmin.setOnClickListener {
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
            }
            binding.bottomNavigationView.setOnItemSelectedListener {menuItem ->
                when(menuItem.itemId){
                    R.id.home ->{
                        replaceFragment(HomeFragment())
                        true
                    }
                    R.id.search ->{
                        replaceFragment(SearchFragment())
                        true
                    }
                    R.id.panier ->{
                        replaceFragment(PanierFragment())
                        true
                    }
                    R.id.profile ->{
                        replaceFragment(ProfileFragment())
                        true
                    }
                    else -> false
                }
            }
        }


    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Logout")
        builder.setMessage("Are you sure you want to log out?")
        builder.setPositiveButton("Logout") { dialogInterface: DialogInterface, i: Int ->
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
            finish()
        }
        builder.setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.search -> replaceFragment(SearchFragment())
                R.id.panier -> replaceFragment(PanierFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
                R.id.setting -> replaceFragment(SettingsFragment())
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            return true
    }
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }

        override fun onBackPressed() {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack() // Remove fragment from back stack
                } else {
                    super.onBackPressed()
                }
            }
        }
}
