package com.yogiw.tubessisfo.Data



data class ScheduleClass(
    val message: String,
    val data: List<Data>
)

data class Data(
        val id_jadwal: Int,
        val id_karyawan: Int,
        val tanggal: String,
        val shift: String
)