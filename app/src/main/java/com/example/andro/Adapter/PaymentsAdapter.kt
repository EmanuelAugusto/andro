package com.example.andro.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.andro.Model.Owner
import com.example.andro.Model.Payments
import com.example.andro.Model.PaymentsResponse
import com.example.andro.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.list_item_payments.view.*

class PaymentsAdapter (context: Context, lojas: List<Payments>): ArrayAdapter<Payments>(context, 0, lojas) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rootView = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_payments, parent, false)
        val currentContact = getItem(position)



        val img = rootView.imageFood
        Picasso.get().load( currentContact!!.idFood[0].imagem).into(img);

        rootView.nameFood.text = currentContact!!.idFood[0].nome
        rootView.price.text = currentContact!!.price.toString()
        rootView.date.text = currentContact!!.dataCreated

        return rootView
    }

}

private fun RequestCreator.into(img: Unit) {
    return img
}