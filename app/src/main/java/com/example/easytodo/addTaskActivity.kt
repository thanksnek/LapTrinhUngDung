package com.example.easytodo

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.easytodo.adapter.itemTaskAdapter
import com.example.easytodo.data.AllData
import com.example.easytodo.databinding.AddTaskBinding
import com.example.easytodo.model.itemTask
import com.example.easytodo.model.statusTask
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

const val RESULT_CODE_UPDATE_TASK = 1

class addTaskActivity : AppCompatActivity() {
   private lateinit var allData: AllData;
    private lateinit var newTask: itemTask;
    private var status = statusTask.Chưa_làm;
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = AddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()
        val task = intent.getSerializableExtra("task") as? itemTask
        if (task != null) {
            showTask(task)
        }
       val buttonDangLam = findViewById<Button>(R.id.buttonDangLam)
        val buttonChuaLam = findViewById<Button>(R.id.buttonChuaLam)
        val buttonHoanThanh = findViewById<Button>(R.id.radioButtonHoanThanh)
        val buttonQuaHan = findViewById<Button>(R.id.radioButtonQuaHan)

        buttonChuaLam.setOnClickListener {
             status = statusTask.Chưa_làm

        }
        buttonDangLam.setOnClickListener {
             status = statusTask.Đang_làm
        }
        buttonHoanThanh.setOnClickListener {
              status = statusTask.Hoàn_thành
        }
        buttonQuaHan.setOnClickListener {
             status = statusTask.Quá_hạn
        }
         val saveButton = binding.buttonSave
        saveButton.setOnClickListener {
            if (task == null) {
                creatNewTask()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("itemTaskList", newTask)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }else {
                val resultIntent = Intent(this,MainActivity::class.java)
                Log.d("AddIntent","Task: $task")
                resultIntent.putExtra("updateTask", task)
                startActivity(resultIntent)
            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun creatNewTask(){
        val currentCalendar = Calendar.getInstance()
        val updateTimeToString:String = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(currentCalendar.time)


        val editTextTitle = findViewById<EditText>(R.id.editTextNhapTieuDe)
        val editTextNoiDung = findViewById<EditText>(R.id.editTextNhapNoiDung)
        val editTextStartDate = findViewById<EditText>(R.id.editTextStartDate)
        val editTextEndDate = findViewById<EditText>(R.id.editTextEndDate)
        val editTextUpdateTime = findViewById<TextView>(R.id.editTextTimeUpdate)

        val title = editTextTitle.text.toString()
        val content = editTextNoiDung.text.toString()
        val startDate = editTextStartDate.text.toString()
        val endDate = editTextEndDate.text.toString()
        editTextUpdateTime.text = updateTimeToString as CharSequence


        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val endDateDate = dateFormat.parse(endDate)

         newTask = itemTask(title,content,status, startDate,endDate)
        allData.listTask.add(0,newTask)
        Log.d("AddTaskActivity","New task : $newTask")
        var adapter = itemTaskAdapter(this,allData.listTask)
        adapter.updateItemTaskList(newTask)
        saveData()
    }

    fun showTask(task: itemTask){
        Log.d("AddActivity","UpdateTask: $task")
        val editTextTitle = findViewById<EditText>(R.id.editTextNhapTieuDe)
        val editTextNoiDung = findViewById<EditText>(R.id.editTextNhapNoiDung)
        val editTextStartDate = findViewById<EditText>(R.id.editTextStartDate)
        val editTextEndDate = findViewById<EditText>(R.id.editTextEndDate)
        val editTextUpdateTime = findViewById<TextView>(R.id.editTextTimeUpdate)
        val radioGroup = findViewById<RadioGroup>(R.id.groupStatus)
        for (i in 0 until radioGroup.childCount){
            val radioButton = radioGroup.getChildAt(i) as RadioButton
            if(radioButton is RadioButton && radioButton.text.toString() == task.status.toString()){
                radioButton.isChecked = true
                break
            }

        }

        editTextTitle.setText(task.title)
        editTextNoiDung.setText(task.description)
        editTextStartDate.setText(task.startDate)
        editTextEndDate.setText(task.endDate)
        editTextUpdateTime.setText(task.updateTimeToString)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val checkedRadioButton : RadioButton = findViewById(checkedId)
            task.status = when(checkedRadioButton.id){
                R.id.buttonChuaLam ->statusTask.Chưa_làm
                R.id.radioButtonHoanThanh ->statusTask.Hoàn_thành
                R.id.radioButtonQuaHan->statusTask.Quá_hạn
                else-> statusTask.Đang_làm
            }
        }
        var adapter = itemTaskAdapter(this,allData.listTask)
        editTextTitle.addTextChangedListener (object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence,start: Int,before: Int,count: Int){
            }
            override fun afterTextChanged(s: Editable?) {
                task.title = s.toString()
                allData.updateTask(task)
                adapter.updateTask(task)
                saveData()
                Log.d("AddTaskActivity","taslTittle: ${task.title}")
                Log.d("AddActivity","UpdateTask: $task")
            }
        })

        editTextNoiDung.addTextChangedListener (object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence,start: Int,before: Int,count: Int){
                task.description = s.toString()
                allData.updateTask(task)
                adapter.updateTask(task)
                saveData()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        editTextStartDate.addTextChangedListener (object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence,start: Int,before: Int,count: Int){
                task.startDate = s.toString()
                allData.updateTask(task)
                saveData()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        editTextEndDate.addTextChangedListener (object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence,start: Int,before: Int,count: Int){
            }
            override fun afterTextChanged(s: Editable?) {
                task.endDate = s.toString()
                allData.updateTask(task)
                saveData()
            }
        })
        allData.updateTask(task)

        saveData()
        Log.d("AddActivity","showTask í called")
        Log.d("AddActivity","UpdateTask: $task")
        Log.d("AddTaskActivity","taslTittle: ${task.title}")
    }
    fun saveData(){
        allData = AllData(allData.listTask)
        var pref = getSharedPreferences("database", MODE_PRIVATE)
         pref.edit().putString("allTask",allData.toJsonString()).commit()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadData(){
        var pref = getSharedPreferences("database", MODE_PRIVATE)
        var jsonData = pref.getString("allTask",null)
        if(jsonData != null){
            allData = Gson().fromJson<AllData>(jsonData,AllData::class.java)
        } else{
            allData = createDefaultData()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createDefaultData(): AllData{
        val defaultTask = mutableListOf<itemTask>(itemTask("đi mua áo","mua áo dài tay, size L",
            statusTask.Hoàn_thành,"30/11/2023","1/12/2023"))
        return AllData(defaultTask)
    }


}