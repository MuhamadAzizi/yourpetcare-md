package com.bangkit.yourpetcare.scan

data class ScanModel (
    val confidence : Int,
    val message : String,
    val predicted :String
    )