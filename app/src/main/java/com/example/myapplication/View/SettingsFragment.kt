package com.example.myapplication.View

import android.content.res.Configuration
import android.os.Bundle
import com.google.android.material.switchmaterial.SwitchMaterial
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.myapplication.R

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val switchDarkLight = view.findViewById<SwitchMaterial>(R.id.switchDL)
        val backgroundColor = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> ContextCompat.getColor(requireContext(), R.color.grey)
            else -> ContextCompat.getColor(requireContext(), R.color.white)
        }
        view.setBackgroundColor(backgroundColor)

        switchDarkLight.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        return view
    }

}

/*
private fun isDarkMode(): Boolean {
    val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return currentNightMode == Configuration.UI_MODE_NIGHT_YES
}

val view = inflater.inflate(R.layout.fragment_settings, container, false)
val switchDarkMode = view.findViewById<Switch>(R.id.switchDL)

switchDarkMode.isChecked = isDarkMode()

val backgroundColor = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
    Configuration.UI_MODE_NIGHT_YES -> ContextCompat.getColor(requireContext(), R.color.orange)
    else -> ContextCompat.getColor(requireContext(), R.color.grey)
}
view.setBackgroundColor(backgroundColor)
switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
    if (isChecked) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    } else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}*/
