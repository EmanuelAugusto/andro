package com.example.andro.Fragments

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
import com.example.andro.Model.CreateClient
import com.example.andro.Model.responseAndro
import com.example.andro.Model.responseUser
import com.example.andro.Network.RetrofitBuilder
import com.example.andro.R
import com.example.andro.loadFragment
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import android.widget.Toast as Toast


@Suppress("UNREACHABLE_CODE")
class CreateUserClient : Fragment() {

    private lateinit var viewOfLayout: View



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    public fun createUserClient(){

        val nameClient = viewOfLayout.findViewById<EditText>(R.id.nameclient).text.toString()
        val email = viewOfLayout.findViewById<EditText>(R.id.email).text.toString()
        val password = viewOfLayout.findViewById<EditText>(R.id.password).text.toString()
        val cpf = viewOfLayout.findViewById<EditText>(R.id.cpf).text.toString()
        val address = viewOfLayout.findViewById<EditText>(R.id.address).text.toString()

        if(nameClient.isEmpty() || email.isEmpty() || password.isEmpty() || cpf.isEmpty() || address.isEmpty()){
            makeAlert("Preencha todos os Campos!")
        }else{
            val userToCreat = CreateClient(nameClient, "https://www.kindpng.com/picc/m/269-2697881_computer-icons-user-clip-art-transparent-png-icon.png", email, "client", password, cpf, address)
            viewOfLayout.findViewById<LinearLayout>(R.id.ll_main_layout).visibility = View.INVISIBLE
            viewOfLayout.findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            requestCreateUserClient(userToCreat)
        }

    }

    public fun requestCreateUserClient(user : CreateClient){
        RetrofitBuilder.androApi.createUserClient(user).enqueue(object :
            retrofit2.Callback<responseAndro> {

            override fun onResponse(
                call: retrofit2.Call<responseAndro>,
                response: Response<responseAndro>
            ) {

                viewOfLayout.findViewById<LinearLayout>(R.id.ll_main_layout).visibility = View.VISIBLE
                viewOfLayout.findViewById<ProgressBar>(R.id.progressBar).visibility = View.INVISIBLE

                val builder = context?.let {
                    AlertDialog.Builder(it)
                }

                val msg = response.body()?.let { resultApi(it) }

                builder?.setTitle("Aviso")
                builder?.setMessage(msg.toString())


                val dialog: AlertDialog = builder!!.create()
                dialog.show()
            }



            override fun onFailure(call: Call<responseAndro>, t: Throwable) {


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

        val toast = Toast.makeText(getActivity(),alert,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout = inflater!!.inflate(R.layout.fragment_create_user_client, container, false)
        val context = activity as AppCompatActivity

        viewOfLayout.findViewById<Button>(R.id.LoginUser).setOnClickListener {
            val fragment = LoginFragment()
            context.loadFragment(fragment)
        }

        viewOfLayout.findViewById<Button>(R.id.CreateAccount).setOnClickListener {
            createUserClient()
        }


        return  viewOfLayout
    }

    public fun resultApi(res: responseAndro): String {
        var msg: String = ""

        if(res.code == "400"){
            if(res.msg == "already_exists_registered_email_address"){
                msg = "Já existe uma conta cadastrada com este endereço de E-mail!";
            }else if(res.msg == "Incomplete_data"){
                msg = "Dados Imcompletos";
            }
        }else if(res.code == "200"){
            if(res.msg == "Account_Created"){
                msg = "Conta criada com sucesso!";
                val context = activity as AppCompatActivity
                val fragment = LoginFragment()
                context.loadFragment(fragment)
            }
        }

        return msg
    }

}