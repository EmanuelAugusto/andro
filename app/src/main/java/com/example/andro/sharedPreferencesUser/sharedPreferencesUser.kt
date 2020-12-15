package com.example.andro.sharedPreferencesUser

import android.content.Context
import android.content.SharedPreferences
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.andro.Model.PaymentsResponse
import com.example.andro.Model.responseUser
import com.example.andro.Model.responseUserLogin
import com.example.andro.R
import retrofit2.Response

class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences("andro", Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "tokenUser"
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, "")
    }

    fun logoutProcedures(): String?{
        var msg: String = ""

        val editor = prefs.edit()

        editor?.remove("tokenUser")?.apply()
        editor?.remove("userName")?.commit()

        return msg
    }

    public fun resultApiPayment(res: PaymentsResponse): String{

        var msg: String = ""

        if(res.code == "401" || res.code == "404"){
            if(res.msg== "invalid_session"){
                msg = "invalid_session";
            }else if(res.msg == "user_not_found"){
                msg = "user_not_found";
            }
        }else if(res.code == "200"){
            if(res.msg == "user_logged"){
                msg = "user_logged";
            }
        }else if(res.code == "500"){
            msg = "error_server"
        }

        return msg

    }


    public fun resultApiPayment(res: responseUser): String{

        var msg: String = ""

        if(res.code == "401" || res.code == "404"){
            if(res.msg== "invalid_session"){
                msg = "invalid_session";
            }else if(res.msg == "token_not_sended"){
                msg = "token_not_sended";
            }
        }else if(res.code == "200"){
            if(res.msg == "user_logged"){
                msg = "user_logged";
            }
        }else if(res.code == "500"){
            msg = "error_server"
        }

        return msg

    }
}