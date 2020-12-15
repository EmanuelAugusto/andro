package com.example.andro

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.andro.Fragments.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(HomeFragment())

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.search ->{
                    loadFragment(searchFragment())
                    true
                }
                R.id.notification ->{
                    if(hasToken()){
                        loadFragment(notificationFragment())
                    }else{
                        loadFragment(CreateUserClient())
                    }

                    true
                }
                R.id.profile ->{
                    if(hasToken()){
                        loadFragment(profileFragment())
                    }else{
                        loadFragment(CreateUserClient())
                    }

                    true
                }
                else -> false
            }
        }
    }

    public fun hasToken(): Boolean{
        var token: Boolean = false;

        sharedPreferences = getSharedPreferences("andro", MODE_PRIVATE)

        token = sharedPreferences.contains("tokenUser")

        return token
    }


}

fun AppCompatActivity.getToken(): String{


    val editor : AppCompatActivity = this

    val token = editor.getSharedPreferences("andro", Context.MODE_PRIVATE)

    if(token.contains("tokenUser")){
        return token.getString("tokenUser", "").toString()
    }else{
        return ""
    }

}

fun AppCompatActivity.loadFragment(fragment: Fragment)
{
    supportFragmentManager.beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit()
}