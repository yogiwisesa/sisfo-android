package com.yogiw.tubessisfo.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.yogiw.tubessisfo.Adapter.ScheduleAdapter
import com.yogiw.tubessisfo.Adapter.ScheduleTukarAdapter
import com.yogiw.tubessisfo.Data.Account
import com.yogiw.tubessisfo.Data.Data
import com.yogiw.tubessisfo.Data.ScheduleClass
import com.yogiw.tubessisfo.Data.UserClass
import com.yogiw.tubessisfo.R
import kotlinx.android.synthetic.main.activity_schedule.*

class ScheduleActivity : AppCompatActivity() {

    val listSchedule: MutableList<Data> =  ArrayList()
    val listKaryawan: MutableList<Account> =  ArrayList()
    lateinit var adapterTukar: ScheduleTukarAdapter
    var idJadwal: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        supportActionBar?.title = "Schedule"
        val intent = getIntent()
        val extras: Bundle = intent.extras
        val idKaryawan =  extras.getInt("idkaryawan")
        idJadwal=  extras.getInt("idjadwal")


        rv.layoutManager = LinearLayoutManager(applicationContext)

        swiperefresh.isRefreshing = true
        getsemuakryawan()
        swiperefresh.setOnRefreshListener {
            swiperefresh.isRefreshing = true
            getsemuakryawan()
        }


    }

    fun getsemuakryawan(){
        AndroidNetworking.get("https://fathomless-brushlands-93068.herokuapp.com/getsemuakaryawan")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(UserClass::class.java, object : ParsedRequestListener<UserClass> {
                    override fun onResponse(user: UserClass) {
                        // do anything with response
                        Log.i("halooo", "size " + user.account.size)
                        if (user.message == "ok"){
                            listKaryawan.addAll(user.account)

                            getJadwal()
                        }
                    }

                    override fun onError(anError: ANError) {
                        // handle error
                        Log.i("halooo", "karyawan " + anError.errorBody)
                    }
                })
    }

    fun getJadwal(){
        AndroidNetworking.get("https://fathomless-brushlands-93068.herokuapp.com/getsemuajadwal")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(ScheduleClass::class.java, object : ParsedRequestListener<ScheduleClass> {
                    override fun onResponse(schedule: ScheduleClass) {
                        if (schedule.message.equals("ok")){
                            listSchedule.addAll(schedule.data)
                            adapterTukar = ScheduleTukarAdapter(this@ScheduleActivity, listSchedule, listKaryawan, idJadwal )

                            rv.adapter = adapterTukar
                            swiperefresh.isRefreshing = false
                        }

                    }

                    override fun onError(anError: ANError) {
                        Log.i("halooo", "jadwal" + anError.toString())
                    }
                })
    }
}
