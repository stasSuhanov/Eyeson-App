package com.example.eyesonapp.utils

import java.text.DateFormat
import java.util.*

fun formatData(date: Date): String {
    return DateFormat.getDateInstance(DateFormat.SHORT).format(date)
}