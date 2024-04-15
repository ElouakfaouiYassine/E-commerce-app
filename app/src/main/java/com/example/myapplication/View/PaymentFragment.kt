package com.example.myapplication.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.Repository.DataBasePayment
import com.google.android.material.snackbar.Snackbar


class PaymentFragment : Fragment() {
    lateinit var ed_card_number: EditText
    lateinit var ed_card_name: EditText
    lateinit var ed_expiration: EditText
    lateinit var ed_cvv: EditText
    lateinit var btn_pay: Button
    lateinit var databasePayment: DataBasePayment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view:View = inflater.inflate(R.layout.fragment_payment, container, false)
        ed_card_number = view.findViewById(R.id.card_number)
        ed_card_name = view.findViewById(R.id.name_card)
        ed_expiration = view.findViewById(R.id.experation)
        ed_cvv = view.findViewById(R.id.cvv)
        btn_pay = view.findViewById(R.id.btn_pay)
        databasePayment = DataBasePayment(requireContext())
        btn_pay.setOnClickListener {
            val card_number = ed_card_number.text.toString()
            val card_name = ed_card_name.text.toString()
            val card_expiration = ed_expiration.text.toString()
            val card_cvv = ed_cvv.text.toString()

            if (card_number.isEmpty()){
                ed_card_number.error = "The field is empty"
                ed_card_name.error = "The field is empty"
                ed_expiration.error = "The field is empty"
                ed_cvv.error = "The field is empty"
            }else if(!isCardNumber(card_number)){
                ed_card_number.error = "The field is not valid"
            }else if (card_number.isEmpty() || card_name.isEmpty()){
                ed_card_name.error = "The field is empty"
                ed_expiration.error = "The field is empty"
                ed_cvv.error = "The field is empty"
            }else if (!isCardName(card_name)){
                ed_card_name.error = "The field is not valid"
            }else if (card_number.isEmpty() || card_name.isEmpty() || card_expiration.isEmpty()){
                ed_expiration.error = "The field is empty"
                ed_cvv.error = "The field is empty"
            }else if (!isCardExpiration(card_expiration)){
                ed_expiration.error = "The field is not valid"
            }else if (card_number.isEmpty() || card_name.isEmpty() || card_expiration.isEmpty() || card_cvv.isEmpty()){
                ed_cvv.error = "The field is empty"
            }else if (!isCardCvv(card_cvv)){
                ed_cvv.error = "The field is not valid"
            }else{
                if (true){
                    paymentDataBase(card_number, card_name, card_expiration, card_cvv)
                }else{
                    Snackbar.make(requireView(), "Error", Snackbar.LENGTH_LONG).show()
                }
            }
        }
        return view
    }

    fun paymentDataBase(card_number: String, card_name: String, card_expiration:String, card_cvv:String){
        val insertRowId = databasePayment.insertInfoCard(card_number.toString(),card_name.toString(), card_expiration.toString(), card_cvv.toString())
        if (insertRowId != -1L){
            Snackbar.make(requireView(), "Info Card added successfully", Snackbar.LENGTH_LONG).show()
            ed_card_number.text.clear()
            ed_card_name.text.clear()
            ed_expiration.text.clear()
            ed_cvv.text.clear()
        }
        else{
            Snackbar.make(requireView(), "Failed to added Info Card", Snackbar.LENGTH_LONG).show()
        }
    }
    fun isCardNumber(numberCard: String?): Boolean {
        val numberCardRegex = "^[0-9]{16}$"
        val pattern = Regex(numberCardRegex)
        return numberCard != null && pattern.matches(numberCard)
    }

    fun isCardName(nameCard: String?): Boolean {
        val nameRegex = "^[A-Z]*$"
        val pattern = Regex(nameRegex)
        return nameCard != null && pattern.matches(nameCard)
    }

    fun isCardExpiration(expiration: String?): Boolean {
        val expirationRegex = """^(0[1-9]|1[0-2])\/\d{2}$"""
        val pattern = Regex(expirationRegex)
        return expiration != null && pattern.matches(expiration)
    }

    fun isCardCvv(cvv: String?): Boolean {
        val cvvRegex = """^\d{3,4}$"""
        val pattern = Regex(cvvRegex)
        return cvv != null && pattern.matches(cvv)
    }

}