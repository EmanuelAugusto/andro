package com.example.andro.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.andro.Model.Food
import com.example.andro.Model.PaymentsResponse
import com.example.andro.Model.userPaymentFood
import com.example.andro.Network.RetrofitBuilder
import com.example.andro.R
import com.example.andro.loadFragment
import com.example.andro.sharedPreferencesUser.SessionManager
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PaymentFoodFragment : Fragment() {

    private lateinit var viewOfLayout : View
    private lateinit var foodListResponse: Call<Food>
    private lateinit var quantityUser: String
    private lateinit var priceUser: String
    private lateinit var idFood: String
    private lateinit var idSalesman: String
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val builder = context?.let {
            sessionManager = SessionManager(it)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout = inflater!!.inflate(R.layout.fragment_payment_food, container, false)

        idFood = arguments?.getSerializable("idFood") as String
        quantityUser = arguments?.getSerializable("quantityUser") as String
        priceUser = arguments?.getSerializable("priceUser") as String
        fetchData(idFood.toString())


        val btBuy = viewOfLayout.findViewById<Button>(R.id.finishPayment)

        btBuy.setOnClickListener {
            checkdata()
        }

        return viewOfLayout
    }

    private fun checkdata(){
        val carNumber = viewOfLayout.findViewById<EditText>(R.id.carNumber).text.toString()
        val expDate = viewOfLayout.findViewById<EditText>(R.id.expDate).text.toString()
        val nameHolder = viewOfLayout.findViewById<EditText>(R.id.nameHolder).text.toString()
        val price  = priceUser
        val quantity = quantityUser
        val idFoodClient = idFood
        val idSalesmanClient = idSalesman

        if(carNumber.isEmpty() || expDate.isEmpty() || nameHolder.isEmpty()){
            makeAlert("Preencha todos os Campos!")
        }else{
            val payment = userPaymentFood(carNumber, nameHolder, expDate, idFoodClient, idSalesmanClient, price.toInt(), quantity.toInt())
            paymentFood(payment)
        }
    }

    private fun paymentFood(payment: userPaymentFood){
        viewOfLayout.findViewById<LinearLayout>(R.id.loadfood).visibility=View.VISIBLE
        viewOfLayout.findViewById<LinearLayout>(R.id.paymentDetail).visibility=View.GONE
        viewOfLayout.findViewById<LinearLayout>(R.id.paymentform).visibility=View.GONE

        RetrofitBuilder.androApi.payFood(token = "Bearer ${sessionManager.fetchAuthToken()}", payment).enqueue(object : Callback<PaymentsResponse>{
            override fun onResponse(call: Call<PaymentsResponse>, response: Response<PaymentsResponse>) {

                if(response.isSuccessful){

                    val builder = context?.let {
                        AlertDialog.Builder(it)
                    }

                    builder?.setTitle("Aviso")
                    if(response.body()!!.code == "400"){
                        builder?.setMessage("Erro ao efetuar o pagamento do pedido! \n confira se seu cartão possui saldo disponível \n ou confira seus dados.")
                        viewOfLayout.findViewById<LinearLayout>(R.id.loadfood).visibility=View.GONE
                        viewOfLayout.findViewById<LinearLayout>(R.id.paymentDetail).visibility=View.VISIBLE
                        viewOfLayout.findViewById<LinearLayout>(R.id.paymentform).visibility=View.VISIBLE
                    }else{
                        builder?.setMessage("Pagamento efetuado com sucesso, aguarde o pedido em sua mesa. \n acompanhe no menu de notifiações o status do pedido.")
                    }


                    val dialog: AlertDialog = builder!!.create()
                    dialog.show()

                    if(response.body()!!.code == "200"){
                        val context = activity as AppCompatActivity
                        val fragment = myPaymentsFragment()
                        context.loadFragment(fragment)
                    }

                }

            }
            override fun onFailure(call: Call<PaymentsResponse>, t: Throwable) {

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

    public  fun makeAlert(alert: String){

        val toast = Toast.makeText(getActivity(),alert,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show()
    }

    private fun fetchData(id: String){
        RetrofitBuilder.androApi.getOneFood(id).enqueue(object : Callback<Food> {
            override fun onResponse(call: Call<Food>, response: Response<Food>) {

                viewOfLayout.findViewById<LinearLayout>(R.id.loadfood).visibility=View.GONE
                viewOfLayout.findViewById<LinearLayout>(R.id.paymentDetail).visibility=View.VISIBLE
                viewOfLayout.findViewById<LinearLayout>(R.id.paymentform).visibility=View.VISIBLE


                if(response.isSuccessful){

                    idSalesman = response.body()!!.owner[0]._id
                    viewOfLayout.findViewById<TextView>(R.id.nameFood).text = response.body()!!.nome
                    viewOfLayout.findViewById<TextView>(R.id.price).text = "R$ " + response.body()!!.preco.toString()
                    viewOfLayout.findViewById<TextView>(R.id.priceFromUser).text = "R$ " + priceUser
                    viewOfLayout.findViewById<TextView>(R.id.quantity).text = "Quantidade: "+ quantityUser
                    val img = viewOfLayout.findViewById<ImageView>(R.id.imageFood)

                    Picasso.get().load( response.body()!!.imagem).into(img);


                }

            }
            override fun onFailure(call: Call<Food>, t: Throwable) {

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



}