package com.example.andro.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.andro.Fragments.HomeFragment
import com.example.andro.Model.Food
import com.example.andro.Model.Loja
import com.example.andro.Model.Owner
import com.example.andro.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import kotlinx.android.synthetic.main.list_item.view.*

class LojaArrayAdapter(context: Context, lojas: List<Owner>): ArrayAdapter<Owner>(context, 0, lojas) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View{
        val rootView = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        val currentContact = getItem(position)



        val img = rootView.contactImage
        Picasso.get().load( currentContact!!.image).into(img);
        rootView.contactName.text = currentContact!!.nameSalesman
        rootView.contactDescription.text = currentContact!!.address

        return rootView
    }

}

private fun RequestCreator.into(img: Unit) {
    return img
}
