package com.example.myapplication.View

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.Model.Utilusateur
import com.example.myapplication.R
import com.example.myapplication.Repository.DataBaseUser


class Info_User_AdminFragment : Fragment() {
    lateinit var recyclerView:RecyclerView
    lateinit var dataBaseUser: DataBaseUser
    lateinit var newList: ArrayList<Utilusateur>
    lateinit var btnAddProfil:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info__user__admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rv_info_admin)
        btnAddProfil = view.findViewById(R.id.btn_add_profile)
        dataBaseUser = DataBaseUser(requireContext())
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
        displayUtilusateur()
        btnAddProfil.setOnClickListener {
            val intent = Intent(requireContext(), SignUP::class.java)
            startActivity(intent)
        }
    }

    fun displayUtilusateur(){
        var newcursor: Cursor? = dataBaseUser.getInfoUtilusateur()
        newList = ArrayList()
        newcursor?.let {
            val usernameIndex = it.getColumnIndex(DataBaseUser.COLUMN_USERNAME)
            var emailIndex = it.getColumnIndex(DataBaseUser.COLUMN_EMAIL)
            while (it.moveToNext()){
                val name = if (usernameIndex != -1) it.getString(usernameIndex) else ""
                val  email = if (emailIndex != -1) it.getString(emailIndex) else ""
                val utilusateur = Utilusateur(name, email)
                newList.add(utilusateur)
            }
            it.close()
        }
        val adapter = AdapterAdmin(newList, requireContext())
        recyclerView.adapter = adapter
    }


}