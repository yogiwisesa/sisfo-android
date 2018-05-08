package com.yogiw.tubessisfo.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.yogiw.tubessisfo.Activity.MainActivity
import com.yogiw.tubessisfo.Activity.ScheduleActivity
import com.yogiw.tubessisfo.Data.Account
import com.yogiw.tubessisfo.Data.Data
import com.yogiw.tubessisfo.Data.ScheduleClass
import com.yogiw.tubessisfo.Data.UserClass
import com.yogiw.tubessisfo.R
import kotlinx.android.synthetic.main.activity_login.*
import java.text.SimpleDateFormat

class ScheduleTukarAdapter(val context: Context, val listSchedule:List<Data>, val listKaryawan:List<Account>, val idJadwal1: Int): RecyclerView.Adapter<ScheduleTukarAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_jadwal_tukar, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listSchedule.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listSchedule[position]

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputFormat = SimpleDateFormat("dd MMMM yyyy")
        val date = outputFormat.format(inputFormat.parse(item.tanggal))

        holder.tvTanggal.text = date
        holder.tvShift.text = item.shift
        holder.tvKaryawan.text = getNamaKaryawan(item.id_karyawan)
        holder.container.setOnClickListener {
            AndroidNetworking.post("https://fathomless-brushlands-93068.herokuapp.com/requesttukar")
                    .addBodyParameter("idjadwal1", idJadwal1.toString())
                    .addBodyParameter("idjadwal2", listSchedule[position].id_jadwal.toString())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(UserClass::class.java, object : ParsedRequestListener<UserClass> {
                        override fun onResponse(user: UserClass) {
                            // do anything with response
                            Log.i("halooo", user.message)
                            if (user.message == "ok"){
                                Toast.makeText(context, "Request tukar jadwal berhasil!", Toast.LENGTH_LONG).show()
                                (context as Activity).finish()
                            }
                        }

                        override fun onError(anError: ANError) {
                            // handle error
                            Log.i("halooo", anError.errorBody)
                        }
                    })
        }
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val tvTanggal = item.findViewById<View>(R.id.tvTanggal) as TextView
        val tvShift = item.findViewById<View>(R.id.tvShift) as TextView
        val tvKaryawan = item.findViewById<View>(R.id.tvKaryawan) as TextView
        val container = item.findViewById<View>(R.id.container) as LinearLayout
    }

    fun getNamaKaryawan(idKaryawan: Int): String {

        for (karyawan in listKaryawan){
            if (idKaryawan == karyawan.id_karyawan){
                return karyawan.nama_karyawan
            }
        }
        return "Not found"
    }
}
