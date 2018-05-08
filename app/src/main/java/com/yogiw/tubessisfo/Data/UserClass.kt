package com.yogiw.tubessisfo.Data


data class UserClass(
    val message: String,
    val account: List<Account>
)

data class Account(
    val id_karyawan: Int,
    val nama_karyawan: String,
    val TTL: String,
    val jenis_kelamin: String,
    val no_telp: String,
    val alamat: String,
    val jabatan: String,
    val username: String,
    val password: String
)