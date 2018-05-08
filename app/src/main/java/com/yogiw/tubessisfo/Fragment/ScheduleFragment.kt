package com.yogiw.tubessisfo.Fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.yogiw.tubessisfo.Adapter.ScheduleAdapter
import com.yogiw.tubessisfo.Data.Data
import com.yogiw.tubessisfo.Data.ScheduleClass
import com.yogiw.tubessisfo.R
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.fragment_schedule.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ScheduleFragment : Fragment() {

    val listSchedule: MutableList<Data> =  ArrayList()
    lateinit var adapterSchedule: ScheduleAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val idkaryawan = arguments?.getString("id_karyawan")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)
        Log.i("halooo", "aaa " + idkaryawan)
        adapterSchedule = ScheduleAdapter(view.context, listSchedule, idkaryawan!!.toInt())
        view.rvSchedule.adapter = adapterSchedule
        view.rvSchedule.layoutManager = LinearLayoutManager(view.context)

        view.pbPending.visibility= View.VISIBLE
        refresh(idkaryawan)
        view.swiperefresh.setOnRefreshListener {
            refresh(idkaryawan)
            swiperefresh.isRefreshing = true
        }


        return view
    }


    fun refresh(idkaryawan: String){
        AndroidNetworking.post("https://fathomless-brushlands-93068.herokuapp.com/jadwal")
                .addBodyParameter("id", idkaryawan)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(ScheduleClass::class.java, object : ParsedRequestListener<ScheduleClass> {
                    override fun onResponse(schedule: ScheduleClass) {
                        if (schedule.message.equals("ok")){
                            listSchedule.clear()
                            listSchedule.addAll(schedule.data)
                            swiperefresh.isRefreshing = false
                            adapterSchedule.notifyDataSetChanged()
                            pbPending.visibility= View.GONE
                        }
                    }

                    override fun onError(anError: ANError) {
                        Log.i("halooo", anError.toString())
                    }
                })

    }

}
