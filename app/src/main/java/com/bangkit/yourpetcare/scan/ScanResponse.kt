package com.bangkit.yourpetcare.scan

data class ScanResponse (
    val confidence : Float,
    val message : String,
    val predicted :String
)