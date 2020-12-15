package com.example.andro.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.andro.Adapter.PaymentsAdapter
import com.example.andro.Model.Payments
import com.example.andro.Model.PaymentsResponse
import com.example.andro.Network.RetrofitBuilder
import com.example.andro.R
import com.example.andro.loadFragment
import com.example.andro.sharedPreferencesUser.SessionManager
import retrofit2.Call
import retrofit2.Response

class myPaymentsFragment : Fragment() {

    private lateinit var paymentsResponse: ArrayList<Payments>
    private lateinit var sessionManager: SessionManager
    private lateinit var viewOfLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val builder = context?.let {
            sessionManager = SessionManager(it)
        }

        getPayments()

    }

    public fun getPayments(){
        RetrofitBuilder.androApi.getPayments(token = "Bearer ${sessionManager.fetchAuthToken()}").enqueue(object :
            retrofit2.Callback<PaymentsResponse> {

            override fun onResponse(
                call: retrofit2.Call<PaymentsResponse>,
                response: Response<PaymentsResponse>
            ) {
                val res = sessionManager.resultApiPayment(response.body()!!)

                viewOfLayout.findViewById<ListView>(R.id.userlist).visibility = View.VISIBLE
                viewOfLayout.findViewById<ProgressBar>(R.id.progressBar).visibility = View.INVISIBLE

                if(res == "user_logged"){
                    paymentsResponse = response.body()!!.payment

                    if (response.isSuccessful) {


                        val arrayAdapter = context?.let { PaymentsAdapter(it, paymentsResponse) }


                        var mListView = viewOfLayout.findViewById<ListView>(R.id.userlist)

                        mListView.adapter = arrayAdapter


                    }
                }else{
                    val builder = context?.let {
                        AlertDialog.Builder(it)
                    }

                    builder?.setTitle("Erro")
                    builder?.setMessage("Sessão Expirada! Faça Login Novamente.")

                    val dialog: AlertDialog = builder!!.create()
                    dialog.show()

                    sessionManager.logoutProcedures()


                    val context = activity as AppCompatActivity

                    context.loadFragment(LoginFragment())

                }

            }

            override fun onFailure(call: Call<PaymentsResponse>, t: Throwable) {


                Log.d("main", "onFailure: ${t.message}")

                val builder = context?.let {
                    AlertDialog.Builder(it)
                }

                builder?.setTitle("Erro")
                builder?.setMessage(t.localizedMessage)


                val dialog: AlertDialog = builder!!.create()
                dialog.show()

            }


        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout = inflater!!.inflate(R.layout.fragment_my_payments, container, false)

        return  viewOfLayout
    }



}