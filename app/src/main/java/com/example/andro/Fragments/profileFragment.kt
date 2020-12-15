package com.example.andro.Fragments

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.andro.Adapter.PaymentsAdapter
import com.example.andro.Model.PaymentsResponse
import com.example.andro.Model.responseUser
import com.example.andro.Network.RetrofitBuilder
import com.example.andro.R
import com.example.andro.loadFragment
import com.example.andro.sharedPreferencesUser.SessionManager
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response


class profileFragment : Fragment() {

    private lateinit var viewOfLayout: View
    private lateinit var sessionManager: SessionManager
    private lateinit var responseuserme: responseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val builder = context?.let {
            sessionManager = SessionManager(it)
        }

        getMe()

    }

    public fun getMe(){
        RetrofitBuilder.androApi.getMe(token = "Bearer ${sessionManager.fetchAuthToken()}").enqueue(object :
            retrofit2.Callback<responseUser> {

            override fun onResponse(
                call: retrofit2.Call<responseUser>,
                response: Response<responseUser>
            ) {
                val res = sessionManager.resultApiPayment(response.body()!!)

                viewOfLayout.findViewById<CardView>(R.id.cardProfile).visibility = View.VISIBLE
                viewOfLayout.findViewById<ProgressBar>(R.id.progressBar).visibility = View.INVISIBLE

                if(res == "user_logged"){

                    responseuserme = response.body()!!

                    if (response.isSuccessful) {

                        viewOfLayout.findViewById<TextView>(R.id.userName).text = responseuserme.dataUser.user.nameclient

                        val img = viewOfLayout.findViewById<ImageView>(R.id.profilePhoto)

                        Picasso.get().load( responseuserme.dataUser.user.image).into(img);


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

            override fun onFailure(call: Call<responseUser>, t: Throwable) {


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

    public fun logout(){
        sessionManager.logoutProcedures()
        toScreenLogin()
    }

    public fun toScreenLogin(){
        val context = activity as AppCompatActivity
        val fragment = LoginFragment()
        context.loadFragment(fragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout = inflater!!.inflate(R.layout.fragment_profile, container, false)

        val context = activity as AppCompatActivity

        viewOfLayout.findViewById<Button>(R.id.myPayments).setOnClickListener {
            val fragment = myPaymentsFragment()
            context.loadFragment(fragment)
        }

        viewOfLayout.findViewById<Button>(R.id.dataprofile).setOnClickListener {
            val fragment = EditProfile()
            context.loadFragment(fragment)
        }


        viewOfLayout.findViewById<Button>(R.id.logout).setOnClickListener {
            logout()
        }

        return  viewOfLayout
    }


}