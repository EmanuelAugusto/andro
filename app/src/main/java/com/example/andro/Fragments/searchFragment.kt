package com.example.andro.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.andro.Adapter.LojaArrayAdapter
import com.example.andro.Model.Loja
import com.example.andro.Model.Owner
import com.example.andro.Network.RetrofitBuilder
import com.example.andro.R
import com.example.andro.loadFragment
import kotlinx.android.synthetic.main.fragment_salesman.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class searchFragment : Fragment() {

    private lateinit var viewOfLayout: View
    private lateinit var foodListResponse: ArrayList<Owner>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    private fun fetchData(nameSalesman: String){

        viewOfLayout.findViewById<TextView>(R.id.notfound).visibility = View.INVISIBLE
        viewOfLayout.findViewById<ProgressBar>(R.id.loadSearch).visibility = View.VISIBLE

        val lojarequest = Owner(0,"", nameSalesman, "", "", "","", "", 0)

        RetrofitBuilder.androApi.getLojaSearch(lojarequest).enqueue(object : Callback<ArrayList<Owner>> {
            override fun onResponse(
                call: Call<ArrayList<Owner>>,
                response: Response<ArrayList<Owner>>
            ) {
                println(response.body())

                if(response.isSuccessful){

                    viewOfLayout.findViewById<ProgressBar>(R.id.loadSearch).visibility = View.INVISIBLE
                    viewOfLayout.findViewById<LinearLayout>(R.id.resultLayout).visibility = View.VISIBLE
                    viewOfLayout.findViewById<TextView>(R.id.notfound).visibility = View.INVISIBLE

                    if(response.body()?.size!! > 0){
                        foodListResponse = response.body()!!

                        val arrayAdapter = context?.let { LojaArrayAdapter(it, foodListResponse) }



                        var mListView = viewOfLayout.findViewById<ListView>(R.id.userlist)

                        arrayAdapter?.notifyDataSetChanged()

                        mListView.adapter = arrayAdapter

                       /* onItemsLoadComplete()*/
                    }else{
                        viewOfLayout.findViewById<ProgressBar>(R.id.loadSearch).visibility = View.INVISIBLE
                        viewOfLayout.findViewById<LinearLayout>(R.id.resultLayout).visibility = View.INVISIBLE
                        viewOfLayout.findViewById<TextView>(R.id.notfound).visibility = View.VISIBLE
                        viewOfLayout.findViewById<TextView>(R.id.notfound).text = "Nenhum restaurante encontrado"
                    }

                }


            }
            override fun onFailure(call: Call<ArrayList<Owner>>, t: Throwable) {

                /*val builder = context?.let {
                    AlertDialog.Builder(it)
                }

                builder?.setTitle("Erro")
                builder?.setMessage("Falha na Conexão com o Servidor")


                val dialog: AlertDialog = builder!!.create()
                dialog.show()*/


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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout = inflater!!.inflate(R.layout.fragment_search, container, false)
        val context = activity as AppCompatActivity

        val search = viewOfLayout.findViewById<SearchView>(R.id.searchView)

        viewOfLayout.findViewById<TextView>(R.id.notfound).visibility = View.VISIBLE
        viewOfLayout.findViewById<TextView>(R.id.notfound).text = "Pesquise por estabelecimentos"
//onActionViewExpanded()
        search.onActionViewExpanded()

            search.setOnQueryTextListener(object :  SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?):Boolean{

                    fetchData(newText.toString())

                    return false
                }
            })




        viewOfLayout.userlist.setOnItemClickListener{ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val args = Bundle()
            val fragment = salesman()
            args.putString("color", foodListResponse?.get(i)._id)
            fragment.arguments = args
            context.loadFragment(fragment)
        }



        /*viewOfLayout.swiperefresh.setOnRefreshListener {
            fetchData();
        }*/


        return  viewOfLayout
    }




}