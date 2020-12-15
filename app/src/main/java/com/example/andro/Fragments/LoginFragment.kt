package com.example.andro.Fragments

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.andro.Model.requestLogin
import com.example.andro.Model.responseUserLogin
import com.example.andro.Network.RetrofitBuilder
import com.example.andro.R
import com.example.andro.loadFragment
import retrofit2.Response


@Suppress("UNREACHABLE_CODE")
class LoginFragment : Fragment() {

    private lateinit var viewOfLayout: View
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    public fun userLogin(){
        val email = viewOfLayout.findViewById<EditText>(R.id.email).text.toString()
        val password = viewOfLayout.findViewById<EditText>(R.id.password).text.toString()

        if( email.isEmpty() || password.isEmpty()){
            makeAlert("Preencha todos os Campos!")
        }else{
            val userToLogin = requestLogin(email, password)
            viewOfLayout.findViewById<LinearLayout>(R.id.ll_main_layout).visibility = View.INVISIBLE
            viewOfLayout.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            requestLogin(userToLogin)
        }
    }

    public fun requestLogin(user: requestLogin){

        RetrofitBuilder.androApi.loginClient(user).enqueue(object : retrofit2.Callback<responseUserLogin>{

            override fun onResponse(
                call: retrofit2.Call<responseUserLogin>,
                response: Response<responseUserLogin>
            ) {

                viewOfLayout.findViewById<LinearLayout>(R.id.ll_main_layout).visibility = View.VISIBLE
                viewOfLayout.findViewById<ProgressBar>(R.id.progressBar).visibility = View.INVISIBLE

                val msg = response.body()?.let { resultApi(it) }

                if(msg != "user_logged"){
                    val builder = context?.let {
                        AlertDialog.Builder(it)
                    }



                    builder?.setTitle("Aviso")
                    builder?.setMessage(msg)


                    val dialog: AlertDialog = builder!!.create()
                    dialog.show()

                }

            }



            override fun onFailure(call: retrofit2.Call<responseUserLogin>, t: Throwable) {


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

    public  fun makeAlert(alert: String){

        val toast = Toast.makeText(getActivity(),alert, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        viewOfLayout = inflater!!.inflate(R.layout.fragment_login, container, false)
        val context = activity as AppCompatActivity

        viewOfLayout.findViewById<Button>(R.id.CreateAccount).setOnClickListener {
            val fragment = CreateUserClient()
            context.loadFragment(fragment)
        }

        viewOfLayout.findViewById<Button>(R.id.LoginUser).setOnClickListener {
            userLogin()
        }

        return  viewOfLayout
    }

    public fun resultApi(res: responseUserLogin): String {
        var msg: String = ""

        if(res.code == "401" || res.code == "404"){
            if(res.msg == "password_do_not_match"){
                msg = "E-mail ou Senha incorreto!";
            }else if(res.msg == "user_not_found"){
                msg = "Usuário não encontrado!";
            }
        }else if(res.code == "200"){
            if(res.msg == "user_logged"){
                msg = res.msg;
                procedureToLoginInApp(res)
            }
        }else if(res.code == "500"){
            msg = "Erro ao tentar fazer login"
        }

        return msg
    }

    fun procedureToLoginInApp(res: responseUserLogin){

        val editor : AppCompatActivity = activity as AppCompatActivity

        val set = editor.getSharedPreferences("andro", MODE_PRIVATE)

        set?.edit()?.putString("tokenUser", res.token)?.commit()
        set?.edit()?.putString("userName", res.dataUser.user.nameclient)?.commit()

        toScreenUser()

    }

    public fun toScreenUser(){
        val context = activity as AppCompatActivity
        val fragment = profileFragment()
        context.loadFragment(fragment)
    }

}