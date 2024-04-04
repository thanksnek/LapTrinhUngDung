package com.example.easytodo.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

enum class statusTask {
    Chưa_làm,
    Đang_làm,
    Hoàn_thành,
    Quá_hạn
}

data class itemTask @RequiresApi(Build.VERSION_CODES.O) constructor(
                    var title: String = "",
                    var description: String ="",
                    var status: statusTask ,
                    var startDate: String = "",
                    var endDate: String = ""
    ) : Serializable{
    var id = UUID.randomUUID()
    val currentCalendar = Calendar.getInstance()
    val updateTimeToString:String = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(currentCalendar.time)
}