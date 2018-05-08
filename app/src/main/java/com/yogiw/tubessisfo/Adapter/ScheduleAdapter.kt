package com.yogiw.tubessisfo.Adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.yogiw.tubessisfo.Activity.ScheduleActivity
import com.yogiw.tubessisfo.Data.Account
import com.yogiw.tubessisfo.Data.Data
import com.yogiw.tubessisfo.Data.ScheduleClass
import com.yogiw.tubessisfo.Data.UserClass
import com.yogiw.tubessisfo.R
import java.text.SimpleDateFormat

class ScheduleAdapter(val context: Context, val listSchedule:List<Data>, val idKaryawan:Int): RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_jadwal, parent, false)
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
        holder.ivTukar.setOnClickListener {
            val intent = Intent(context, ScheduleActivity::class.java)
            val extras = Bundle()
            extras.putInt("idkaryawan", idKaryawan)
            extras.putInt("idjadwal", listSchedule[position].id_jadwal)
            intent.putExtras(extras)
            context.startActivity(intent)
        }
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val tvTanggal = item.findViewById<View>(R.id.tvTanggal) as TextView
        val tvShift = item.findViewById<View>(R.id.tvShift) as TextView
        val ivTukar = item.findViewById<View>(R.id.iv_refresh) as ImageView
    }
}
