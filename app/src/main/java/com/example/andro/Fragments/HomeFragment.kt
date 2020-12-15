package com.example.andro.Fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.andro.Adapter.FoodArrayAdapter
import com.example.andro.Model.Food
import com.example.andro.Network.RetrofitBuilder
import com.example.andro.R
import com.example.andro.getToken
import com.example.andro.loadFragment
import com.example.andro.sharedPreferencesUser.SessionManager
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class HomeFragment : Fragment() {

    private lateinit var viewOfLayout: View
    private lateinit var foodListResponse: ArrayList<Food>
    private lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val builder = context?.let {
            sessionManager = SessionManager(it)
        }


        fetchData()
    }


    private fun fetchData(){
        //val context = activity as AppCompatActivity


        RetrofitBuilder.androApi.getFood(token = "Bearer ${sessionManager.fetchAuthToken()}").enqueue(object : Callback<ArrayList<Food>> {
            override fun onResponse(
                call: Call<ArrayList<Food>>,
                response: Response<ArrayList<Food>>
            ) {

                if (response.isSuccessful) {

                    if (response.body()?.size!! > 0) {
                        foodListResponse = response.body()!!

                        val arrayAdapter = context?.let { FoodArrayAdapter(it, foodListResponse) }


                        var mListView = viewOfLayout.findViewById<ListView>(R.id.userlist)

                        mListView.adapter = arrayAdapter

                        onItemsLoadComplete()
                    } else {
                        withoutData()
                    }


                }


            }

            override fun onFailure(call: Call<ArrayList<Food>>, t: Throwable) {

                /*val builder = context?.let {
                    AlertDialog.Builder(it)
                }

                builder?.setTitle("Erro")
                builder?.setMessage("Falha na Conexão com o Servidor")


                val dialog: AlertDialog = builder!!.create()
                dialog.show()
                */
                onItemsLoadComplete()

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

    public  fun taskGet(){
        Timer().schedule(200) {
            fetchData()
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout = inflater!!.inflate(R.layout.fragment_home, container, false)
        viewOfLayout.swiperefresh.isRefreshing = true
        val context = activity as AppCompatActivity

        viewOfLayout.userlist.setOnItemClickListener{ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val args = Bundle()
            val fragment = buyFragment()
            args.putString("color", foodListResponse?.get(i)._id)
            fragment.arguments = args
            context.loadFragment(fragment)
        }



        viewOfLayout.swiperefresh.setOnRefreshListener {
            fetchData();
        }



        //val handler = Handler()
        //handler.postDelayed(object : Runnable {
          //  override fun run() {
            //    fetchData()
              //  handler.postDelayed(this, 5000)//1 sec delay
            //}
        //}, 0)

        return  viewOfLayout
    }

    fun onItemsLoadComplete() {
        viewOfLayout.swiperefresh.isRefreshing = false
    }

    fun withoutData(){
        onItemsLoadComplete()
        val context = activity as AppCompatActivity
        val args = Bundle()
        val fragment = HelperFragment()
        args.putString("type", "notFound")
        fragment.arguments = args
        context.loadFragment(fragment)
    }



}
