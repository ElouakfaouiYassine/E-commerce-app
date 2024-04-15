package com.example.myapplication.View

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.Utilusateur
import com.example.myapplication.R
import com.example.myapplication.Repository.DataBaseUser
import com.google.android.material.snackbar.Snackbar

class AdapterAdmin(var list: List<Utilusateur>, val context: Context): RecyclerView.Adapter<AdapterAdmin.UtilusateurViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UtilusateurViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.info_rv_admin, parent, false)
        return UtilusateurViewHolder(view, context)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: UtilusateurViewHolder, position: Int) {
        val currentUtilusateur = list[position]
        holder.apply {
            user_info_admin.text = currentUtilusateur.username
            email_info_admin.text = currentUtilusateur.email
            image_delete.setOnClickListener {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("Delete Profile")
                alertDialog.setMessage("Do you want to delete this profile?")
                alertDialog.setPositiveButton("Yes") { _, _ ->
                    val db = DataBaseUser(context)
                    val deletedRows = db.deleteUserByName(currentUtilusateur.email)
                    if (deletedRows > 0) {
                        Snackbar.make((context as Activity).findViewById(android.R.id.content), "User deleted successfully", Snackbar.LENGTH_LONG).show()
                        list = list.filter { it.email != currentUtilusateur.email }
                        notifyDataSetChanged()
                    } else {
                        Snackbar.make((context as Activity).findViewById(android.R.id.content), "ailed to delete user", Snackbar.LENGTH_LONG).show()
                    }
                }
                alertDialog.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                alertDialog.show()
            }
            image_edit.setOnClickListener {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("Edit Profile")
                alertDialog.setMessage("Do you want to edit this profile?")
                alertDialog.setPositiveButton("Yes") { _, _ ->
                    val intent = Intent(context, UpdateProfileActivity::class.java)
                    context.startActivity(intent)
                }
                alertDialog.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                alertDialog.show()
            }
        }
    }
    class UtilusateurViewHolder(itemView:View, val context: Context):RecyclerView.ViewHolder(itemView){
        var user_info_admin: TextView = itemView.findViewById(R.id.user_info_admin)
        var email_info_admin: TextView = itemView.findViewById(R.id.email_info_admin)
        var image_delete: ImageView = itemView.findViewById(R.id.delete_info_admin)
        var image_edit: ImageView = itemView.findViewById(R.id.edit_info_admin)
    }
}