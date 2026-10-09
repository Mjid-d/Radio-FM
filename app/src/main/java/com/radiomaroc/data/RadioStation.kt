package com.radiomaroc.data

data class RadioStation(
    val id: String,
    val name: String,
    val city: String,
    val category: String,
    val streamUrl: String,
    val logoUrl: String? = null,
    val description: String = ""
)
