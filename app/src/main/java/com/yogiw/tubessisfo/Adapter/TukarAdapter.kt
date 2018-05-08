package com.yogiw.tubessisfo.Adapter

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.yogiw.tubessisfo.Activity.MainActivity
import com.yogiw.tubessisfo.Activity.ScheduleActivity
import com.yogiw.tubessisfo.Data.*
import com.yogiw.tubessisfo.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_schedule.*
import java.text.SimpleDateFormat


class TukarAdapter(val context: Context, val listSchedule:List<Data>, val listKaryawan:List<Account>, val listTukar:List<DataTukar> ): RecyclerView.Adapter<TukarAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_tukar, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listTukar.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listTukar[position]



        val jadwal1 = getJadwal(item.id_jadwal1)
        val jadwal2 = getJadwal(item.id_jadwal2)
        val idKaryawan = getIdKaryawan(item.id_jadwal2)
        val namaKaryawan = getNamaKaryawan(idKaryawan)

        holder.tvStatus.text = listTukar[position].status
        holder.tvRequested.text = jadwal1
        holder.tvJadwalTujuan.text = jadwal2
        holder.tvKaryawanTujuan.text = namaKaryawan
        Log.i("halooo", "" + position)
        holder.ivDelete.setOnClickListener {
            AndroidNetworking.post("https://fathomless-brushlands-93068.herokuapp.com/deletelistukar")
                    .addBodyParameter("id_jadwal",item.id_tukar.toString())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(UserClass::class.java, object : ParsedRequestListener<UserClass> {
                        override fun onResponse(user: UserClass) {
                            // do anything with response
                            Log.i("halooo", user.message)
                            if (user.message.equals("ok")){
                                Toast.makeText(context, "Berhasil dihapus, harap refresh halaman!", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onError(anError: ANError) {
                            // handle error
                            Log.i("halooo", anError.toString())
                        }
                    })
        }

    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val tvRequested = item.findViewById<View>(R.id.tvRequested ) as TextView
        val tvKaryawanTujuan = item.findViewById<View>(R.id.tvKaryawanTujuan) as TextView
        val tvJadwalTujuan = item.findViewById<View>(R.id.tvJadwalTujuan) as TextView
        val tvStatus = item.findViewById<View>(R.id.tvStatus) as TextView
        val ivDelete = item.findViewById<View>(R.id.ivDelete) as ImageView
    }

    fun getNamaKaryawan(idKaryawan: Int): String {
        Log.i("adapterpending", "" + listKaryawan.size)
        for (karyawan in listKaryawan){
            Log.i("adapterpending", karyawan.nama_karyawan)
            if (idKaryawan == karyawan.id_karyawan){
                return karyawan.nama_karyawan
            }
        }
        return "Not found"
    }

    fun getJadwal(idJadwal: Int): String {

        for (schedule in listSchedule){
            if (idJadwal == schedule.id_jadwal){
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val outputFormat = SimpleDateFormat("dd MMMM yyyy")
                val date = outputFormat.format(inputFormat.parse(schedule.tanggal))

                val jadwal = date + " - " + schedule.shift
                return jadwal
            }
        }
        return "Not found"
    }

    fun getIdKaryawan(idJadwal: Int): Int {

        for (schedule in listSchedule){
            if (idJadwal == schedule.id_jadwal){
                return schedule.id_karyawan
            }
        }
        return 0
    }
}