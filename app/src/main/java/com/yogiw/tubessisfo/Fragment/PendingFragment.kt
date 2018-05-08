package com.yogiw.tubessisfo.Fragment


import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.yogiw.tubessisfo.Adapter.ScheduleTukarAdapter
import com.yogiw.tubessisfo.Adapter.TukarAdapter
import com.yogiw.tubessisfo.Data.*

import com.yogiw.tubessisfo.R
import kotlinx.android.synthetic.main.fragment_pending.*
import kotlinx.android.synthetic.main.fragment_pending.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PendingFragment : Fragment() {

    val listSchedule: MutableList<Data> = ArrayList()
    val listKaryawan: MutableList<Account> = ArrayList()
    val listTukar: MutableList<DataTukar> = ArrayList()
    var idkaryawan: String? = ""
    lateinit var adapterTukar: TukarAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        idkaryawan = arguments?.getString("id_karyawan")
        Log.i("pending", "idkaryawan: " + idkaryawan)
        val view = inflater.inflate(R.layout.fragment_pending, container, false)
        view.pbPending.visibility= View.VISIBLE

        getSemuaKaryawan()
        view.swiperefresh.setOnRefreshListener {
            getSemuaKaryawan()
            swiperefresh.isRefreshing = true
        }

        view.rv.layoutManager = LinearLayoutManager(view.context)
        return view
    }

    fun getSemuaKaryawan() {
        AndroidNetworking.get("https://fathomless-brushlands-93068.herokuapp.com/getsemuakaryawan")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(UserClass::class.java, object : ParsedRequestListener<UserClass> {
                    override fun onResponse(user: UserClass) {
                        if (user.message.equals("ok")) {
                            listKaryawan.clear()
                            listTukar.clear()
                            listSchedule.clear()
                            listKaryawan.addAll(user.account)
                            Log.i("pending", " karyawan " + user.account.size)
                            getSemuaJadwal()

                        }

                    }

                    override fun onError(anError: ANError) {
                        Log.i("halooo", "jadwal" + anError.errorBody)
                    }
                })
    }

    fun getSemuaJadwal() {
        AndroidNetworking.get("https://fathomless-brushlands-93068.herokuapp.com/getsemuajadwal")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(ScheduleClass::class.java, object : ParsedRequestListener<ScheduleClass> {
                    override fun onResponse(schedule: ScheduleClass) {
                        if (schedule.message.equals("ok")) {
                            listSchedule.addAll(schedule.data)
                            Log.i("pending", " jadwal " + schedule.data.size)
                            getTukar()
                        }

                    }

                    override fun onError(anError: ANError) {
                        Log.i("halooo", "pending- jadwal" + anError.errorBody)
                    }
                })
    }

    fun getTukar() {
        AndroidNetworking.post("https://fathomless-brushlands-93068.herokuapp.com/getlistukar")
                .addBodyParameter("id_karyawan", idkaryawan)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(TukarClass::class.java, object : ParsedRequestListener<TukarClass> {
                    override fun onResponse(tukar: TukarClass) {
                        if (tukar.message.equals("ok")) {
                            listTukar.addAll(tukar.data)
                            Log.i("pending", " tukar " + listTukar.size)
                            adapterTukar = TukarAdapter(context!!, listSchedule, listKaryawan, listTukar)
                            pbPending.visibility = View.GONE
                            rv.adapter = adapterTukar
                            swiperefresh.isRefreshing = false
                        } else {
                            pbPending.visibility = View.GONE
                            Toast.makeText(context, tukar.message, Toast.LENGTH_LONG).show()
                        }

                    }

                    override fun onError(anError: ANError) {
                        Log.i("pending", "pending - " + anError.errorBody)
                        pbPending.visibility = View.GONE
                    }
                })
    }


}
