package com.example.easytodo

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easytodo.adapter.itemTaskAdapter
import com.example.easytodo.data.AllData
import com.example.easytodo.databinding.ActivityMainBinding
import com.example.easytodo.model.itemTask
import com.example.easytodo.model.statusTask
import com.example.easytodo.ui.theme.EasyToDoTheme
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemTaskAdapter: itemTaskAdapter
    lateinit var allData: AllData
    private var data = mutableListOf<itemTask>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadData()
        updateAutomatic(allData)
            recyclerView = binding.listCongViec
            recyclerView.layoutManager = LinearLayoutManager(this)

            itemTaskAdapter = itemTaskAdapter(this, data)
            recyclerView.adapter = itemTaskAdapter

        val updateTask = intent.getSerializableExtra("updateTask") as? itemTask
        Log.d("MainActivity", "UpdateTask: $updateTask")
        if (updateTask != null) {
            Log.d("MainActivity", "UpdateTask: $updateTask")
            updateTask(updateTask)
            itemTaskAdapter.updateTask(updateTask)
            itemTaskAdapter.notifyDataSetChanged()
            saveData()
        }

            val newTask = intent.getStringExtra("itemTaskList") as? itemTask
            if (newTask != null) {
                itemTaskAdapter.updateItemTaskList(newTask)
                upDateItemTaskList(newTask)
            } else {
            }

            binding.buttonAdd.setOnClickListener {
                val context = this
                val intent = Intent(context, addTaskActivity::class.java)
                startActivityForResult(intent, 101)
                saveData()
            }
            loadData()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK) {
                val newTask = data?.getSerializableExtra("itemTaskList") as? itemTask
                if (newTask != null) {
                    itemTaskAdapter.updateItemTaskList(newTask)
                    upDateItemTaskList(newTask)
                    itemTaskAdapter.notifyDataSetChanged()
                }

            }
            Log.d("MainActivity", "UpdatedTasklist: $allData")
            saveData()
        }


        @RequiresApi(Build.VERSION_CODES.O)
        fun loadData() {
            val pref = getSharedPreferences("database", MODE_PRIVATE)
            val jsonData = pref.getString("allTask", null)
            if (jsonData != null) {
                allData = Gson().fromJson(jsonData, AllData::class.java)
                data.clear()
                data.addAll(allData.listTask)
            } else {
                allData = createDefaultData()
            }

        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun createDefaultData(): AllData {
            val defaultTask = mutableListOf<itemTask>(
                itemTask(
                    "đi mua áo", "mua áo dài tay, size L",
                    statusTask.Hoàn_thành, "30/11/2023", "1/12/2023"
                )
            )
            return AllData(defaultTask)
        }

        fun saveData() {
            val pref = getSharedPreferences("database", MODE_PRIVATE)
            pref.edit().putString("allTask", allData.toJsonString()).commit()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onResume() {
            super.onResume()
            loadData()
        }

        override fun onPause() {
            super.onPause()
            saveData()
        }

        fun upDateItemTaskList(task: itemTask) {
            if (task != null) {
                data.add(task)
                allData.listTask.add(0, task)

            } else {
                data.remove(task)
                allData.listTask.remove(task)

            }
            saveData()
            itemTaskAdapter.notifyDataSetChanged()
        }

        fun deleteTask(task: itemTask) {
            allData.listTask.remove(task)
            itemTaskAdapter.dataset.remove(task)
            saveData()
            itemTaskAdapter.notifyDataSetChanged()
        }

        fun updateTask(updateTask: itemTask) {
            val index = data.indexOfFirst { it.id == updateTask.id }
            if (index != -1) {
                data[index] = updateTask
                allData.listTask[index] = updateTask
            }
        }
    @RequiresApi(Build.VERSION_CODES.O)
    fun  updateAutomatic (allData: AllData){
        val isQuaHan : Int = allData.updateStatusOfAllTask()
        Log.d("MainAct","isQuaHan = $isQuaHan")
        if (isQuaHan > 0){
            Toast.makeText(this,"Có $isQuaHan công việc quá hạn", Toast.LENGTH_SHORT).show()
        }
    }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        EasyToDoTheme {
            Greeting("Android")
        }
    }

