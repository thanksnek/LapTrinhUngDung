package com.example.easytodo.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.easytodo.model.itemTask
import com.example.easytodo.model.statusTask
import com.google.gson.Gson
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

data class AllData(var listTask: MutableList<itemTask>) {
    public fun addTask (itemTask: itemTask){
        listTask.add(itemTask)
    }
    public  fun toJsonString() : String {
        var gson = Gson()
        return gson.toJson(this)
    }
    fun updateTask(updateTask: itemTask){
        val index = listTask.indexOfFirst { it.id == updateTask.id }
        if (index != -1){
            listTask[index].title = updateTask.title
            listTask[index].description = updateTask.description
            listTask[index].startDate = updateTask.startDate
            listTask[index].endDate = updateTask.endDate
            listTask[index].status = updateTask.status

        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateStatusOfAllTask() : Int{
        val currentDate = LocalDate.now()
        var count : Int = 0;
        val dateFormats = arrayOf("d/M/yyyy","d/MM/yyyy","dd/MM/yyyy","dd/M/yyyy")
        for (task in listTask){
            var parsedDate :LocalDate? = null
            for(format in dateFormats){
                try {
                    val dateFormatter = DateTimeFormatter.ofPattern(format)
                    parsedDate = LocalDate.parse(task.endDate,dateFormatter)
                    break
                } catch (e: DateTimeParseException){}
            }
            if(parsedDate != null && currentDate.isAfter(parsedDate)){
                task.status = statusTask.Quá_hạn
                updateTask(task)
                count++
            }
        }
        Log.d("allData:","count: $count")
        return count
    }

}