package com.example.andro.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.andro.Model.Payments
import com.example.andro.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import kotlinx.android.synthetic.main.list_item_payments.view.*

class RequestsArrayAdapter (context: Context, lojas: List<Payments>): ArrayAdapter<Payments>(context, 0, lojas) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rootView = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_payments, parent, false)
        val currentContact = getItem(position)



        val img = rootView.imageFood
        Picasso.get().load( currentContact!!.idFood[0].imagem).into(img);

        rootView.nameFood.text = currentContact!!.idFood[0].nome
        rootView.price.text = currentContact!!.price.toString()
        rootView.date.text = currentContact!!.dataCreated
        rootView.status.text = currentContact!!.status[0]

        if(currentContact!!.status[0] == "Processando"){
            rootView.status.setTextColor(Color.parseColor("#FF0000"))
        }else if(currentContact!!.status[0] == "Pago"){
            rootView.status.setTextColor(Color.parseColor("#FFF700"))
        }else if(currentContact!!.status[0] == "Atendido"){
            rootView.status.setTextColor(Color.parseColor("#0EC921"))
        }

        return rootView
    }

}

private fun RequestCreator.into(img: Unit) {
    return img
}