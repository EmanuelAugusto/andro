package com.example.andro.Fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.andro.Adapter.FoodArrayAdapter
import com.example.andro.Model.Food
import com.example.andro.Model.Owner
import com.example.andro.Network.RetrofitBuilder
import com.example.andro.R
import com.example.andro.loadFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class salesman : Fragment() {

    private lateinit var viewOfLayout: View
    private lateinit var foodListResponse: ArrayList<Food>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = activity as AppCompatActivity

        val color = arguments?.getSerializable("color")
        getDataFromSalesman(color.toString())
        getFoodsFromSalesman(color.toString())


    }

    private fun getDataFromSalesman(idSalesman: String){
        RetrofitBuilder.androApi.getProfileSalesman(idSalesman).enqueue(object : Callback<Owner> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Owner>, response: Response<Owner>) {


                if(response.isSuccessful){

                    viewOfLayout.findViewById<ProgressBar>(R.id.loadSearchProfile).visibility = View.INVISIBLE
                    viewOfLayout.findViewById<RelativeLayout>(R.id.profilesalesman).visibility = View.VISIBLE

                    viewOfLayout.findViewById<TextView>(R.id.item_title).text = response.body()!!.nameSalesman
                    viewOfLayout.findViewById<TextView>(R.id.item_detail).text = response.body()!!.address

                    val img = viewOfLayout.findViewById<ImageView>(R.id.item_image)

                    Picasso.get().load( response.body()!!.image).resize(600, 280).into(img)


                }

            }
            override fun onFailure(call: Call<Owner>, t: Throwable) {

                val builder = context?.let {
                    AlertDialog.Builder(it)
                }

                builder?.setTitle("Erro")
                builder?.setMessage("Falha na Conexão com o Servidor")

                val dialog: AlertDialog = builder!!.create()
                dialog.show()

                Log.d("main", "onFailure: ${t.message}")

            }
        })
    }

    private fun getFoodsFromSalesman(idSalesman: String){
        RetrofitBuilder.androApi.getFoodsBySalesman(idSalesman).enqueue(object : Callback<ArrayList<Food>> {
            override fun onResponse(
                call: Call<ArrayList<Food>>,
                response: Response<ArrayList<Food>>
            ) {

                if(response.isSuccessful){

                    if(response.body()?.size!! > 0){
                        foodListResponse = response.body()!!

                        println("AQUI")
                        println(foodListResponse)

                        val arrayAdapter = context?.let { FoodArrayAdapter(it, foodListResponse) }


                        var mListView = viewOfLayout.findViewById<ListView>(R.id.userlist)

                        mListView.adapter = arrayAdapter

                        viewOfLayout.findViewById<LinearLayout>(R.id.resultLayout).visibility = View.VISIBLE
                        viewOfLayout.findViewById<ProgressBar>(R.id.loadSearch).visibility = View.INVISIBLE
                    }else{
                        withoutData()
                    }


                }


            }
            override fun onFailure(call: Call<ArrayList<Food>>, t: Throwable) {

                val context = activity as AppCompatActivity
                val args = Bundle()
                val fragment = HelperFragment()
                args.putString("type", "error")
                fragment.arguments = args
                context.loadFragment(fragment)

                Log.d("main", "onFailure: ${t.message}")

            }
        })
    }

    fun withoutData(){
        val context = activity as AppCompatActivity
        val args = Bundle()
        val fragment = HelperFragment()
        args.putString("type", "notFound")
        fragment.arguments = args
        context.loadFragment(fragment)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout = inflater!!.inflate(R.layout.fragment_salesman, container, false)
        val context = activity as AppCompatActivity

        viewOfLayout.userlist.setOnItemClickListener{ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val args = Bundle()
            val fragment = buyFragment()
            args.putString("color", foodListResponse?.get(i)._id)
            fragment.arguments = args
            context.loadFragment(fragment)
        }

        return viewOfLayout
    }


}