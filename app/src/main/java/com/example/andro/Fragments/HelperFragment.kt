package com.example.andro.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.andro.R
import com.example.andro.loadFragment
import org.w3c.dom.Text

class HelperFragment : Fragment() {

    private lateinit var viewOfLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewOfLayout = inflater!!.inflate(R.layout.fragment_helper, container, false)

        val type = arguments?.getSerializable("type")

        if(type == "error"){
            viewOfLayout.findViewById<TextView>(R.id.message).text = "Erro ao tentar se Conectar ao Servidor"
        }else{
            viewOfLayout.findViewById<TextView>(R.id.message).text = "Nenhum item Encontrado"
        }

        val context = activity as AppCompatActivity


        viewOfLayout.findViewById<Button>(R.id.goback).setOnClickListener {
            val fragment = HomeFragment()
            context.loadFragment(fragment)
        }

        return viewOfLayout
    }

}