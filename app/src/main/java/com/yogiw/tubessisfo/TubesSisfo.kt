package com.yogiw.tubessisfo

import android.app.Application
import com.androidnetworking.AndroidNetworking


class TubesSisfo : Application() {
    override fun onCreate() {
        super.onCreate()

        AndroidNetworking.initialize(applicationContext)

    }
}
