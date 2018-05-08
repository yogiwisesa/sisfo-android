package com.yogiw.tubessisfo.Data


data class TukarClass(
    val message: String,
    val data: List<DataTukar>
)

data class DataTukar(
    val id_tukar: Int,
    val id_jadwal1: Int,
    val id_jadwal2: Int,
    val status: String
)