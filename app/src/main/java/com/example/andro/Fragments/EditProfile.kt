package com.example.andro.Fragments

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
import com.example.andro.Model.CreateClient
import com.example.andro.Model.responseAndro
import com.example.andro.Model.responseUser
import com.example.andro.Network.RetrofitBuilder
import com.example.andro.R
import com.example.andro.loadFragment
import com.example.andro.sharedPreferencesUser.SessionManager
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response


class EditProfile : Fragment() {

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

                viewOfLayout.findViewById<LinearLayout>(R.id.ll_main_layout).visibility = View.VISIBLE
                viewOfLayout.findViewById<ProgressBar>(R.id.progressBar).visibility = View.INVISIBLE

                if(res == "user_logged"){

                    responseuserme = response.body()!!

                    if (response.isSuccessful) {


                        viewOfLayout.findViewById<EditText>(R.id.nameclient).setText(responseuserme.dataUser.user.nameclient)
                        viewOfLayout.findViewById<EditText>(R.id.email).setText(responseuserme.dataUser.user.email)
                        viewOfLayout.findViewById<EditText>(R.id.cpf).setText(responseuserme.dataUser.user.cpf)
                        viewOfLayout.findViewById<EditText>(R.id.address).setText(responseuserme.dataUser.user.address)


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

    public fun updateData(){
        val nameClient = viewOfLayout.findViewById<EditText>(R.id.nameclient).text.toString()
        val email = viewOfLayout.findViewById<EditText>(R.id.email).text.toString()
        val password = viewOfLayout.findViewById<EditText>(R.id.password).text.toString()
        val cpf = viewOfLayout.findViewById<EditText>(R.id.cpf).text.toString()
        val address = viewOfLayout.findViewById<EditText>(R.id.address).text.toString()

        val userToCreat = CreateClient(nameClient, "https://www.kindpng.com/picc/m/269-2697881_computer-icons-user-clip-art-transparent-png-icon.png", email, "client", password, cpf, address)

        RetrofitBuilder.androApi.editProfileClient(token = "Bearer ${sessionManager.fetchAuthToken()}", userToCreat).enqueue(object :
            retrofit2.Callback<responseUser> {

            override fun onResponse(
                call: retrofit2.Call<responseUser>,
                response: Response<responseUser>
            ) {

                viewOfLayout.findViewById<LinearLayout>(R.id.ll_main_layout).visibility = View.INVISIBLE
                viewOfLayout.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE

                val builder = context?.let {
                    AlertDialog.Builder(it)
                }

                builder?.setTitle("Aviso")
                builder?.setMessage("Dados alterados com sucesso!")


                val dialog: AlertDialog = builder!!.create()
                dialog.show()

                val context = activity as AppCompatActivity
                val fragment = profileFragment()
                context.loadFragment(fragment)


            }



            override fun onFailure(call: Call<responseUser>, t: Throwable) {


                Log.d("main", "onFailure: ${t.message}")

                val builder = context?.let {
                    AlertDialog.Builder(it)
                }

                println()

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

        viewOfLayout = inflater!!.inflate(R.layout.fragment_edit_profile, container, false)

        viewOfLayout.findViewById<Button>(R.id.updateDate).setOnClickListener {
            updateData()
        }

        return viewOfLayout
    }

}