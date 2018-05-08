package com.yogiw.tubessisfo.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.interfaces.ParsedRequestListener
import kotlinx.android.synthetic.main.activity_login.*
import com.androidnetworking.error.ANError
import com.yogiw.tubessisfo.R
import com.yogiw.tubessisfo.Data.UserClass


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.title = "Klik Ali yah?"

        btnLogin.setOnClickListener {
            pbLogin.visibility = View.VISIBLE
            Log.i("halooo", "button keteken")
            AndroidNetworking.post("https://fathomless-brushlands-93068.herokuapp.com/login")
                    .addBodyParameter("username", etUsername.text.toString())
                    .addBodyParameter("password", etPassword.text.toString())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(UserClass::class.java, object : ParsedRequestListener<UserClass> {
                        override fun onResponse(user: UserClass) {
                            // do anything with response
                            Log.i("halooo", user.message)
                            if (user.message.equals("ok")){
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                intent.putExtra("id", user.account[0].id_karyawan)
                                startActivity(intent)
                                finish()
                            }
                            pbLogin.visibility = View.GONE
                        }

                        override fun onError(anError: ANError) {
                            // handle error
                            Log.i("halooo", anError.toString())
                            pbLogin.visibility = View.GONE
                        }
                    })
        }
    }
}
