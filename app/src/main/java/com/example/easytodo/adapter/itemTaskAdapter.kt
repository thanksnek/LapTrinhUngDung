package com.example.easytodo.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.easytodo.MainActivity
import com.example.easytodo.R
import com.example.easytodo.addTaskActivity
import com.example.easytodo.model.itemTask

class itemTaskAdapter(val context: Context, val dataset: MutableList<itemTask>): RecyclerView.Adapter<itemTaskAdapter.itemViewHolder>() {
    class itemViewHolder(private val view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.itemTextViewTitle);
        val status: TextView = view.findViewById(R.id.itemTextViewTrangThai);
        val titleDate: TextView = view.findViewById(R.id.itemTextViewDate);
        val date: TextView = view.findViewById(R.id.itemEditTextDate);
        val deleteButton: Button = view.findViewById(R.id.buttonDelete)
    }
    private  var newTask:itemTask? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.item_task,parent, false)
        return itemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size;
    }

    override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
        val item = if (position == 0 && newTask != null){
            newTask
        } else {
            dataset[position]
        }
        holder.title.text = item?.title;
        holder.status.text = item?.status.toString();
        holder.date.text = item?.endDate;
        holder.deleteButton.setOnClickListener(){
            val alert = AlertDialog.Builder(holder.itemView.context)
            alert.setTitle("Delete")
            alert.setMessage("Bạn đã hoàn thành công việc này, nhấn yes để xóa")
            alert.setCancelable(true)
            alert.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                item?.let {
                    dataset.remove(it)
                    deleteTask(it)
                }
            })
            alert.show()
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, addTaskActivity::class.java)
            intent.putExtra("task",item)
            holder.itemView.context.startActivity(intent)
        }
    }
    fun getItemTaskList(): List<itemTask>{
        return dataset.toList()
    }
    fun deleteTask (task: itemTask){
        val activity = context as MainActivity
        activity.deleteTask(task)
    }
    fun updateItemTaskList(task: itemTask){
       newTask = task
        if(newTask!= null) {
            dataset.add(0, newTask!!)
            notifyDataSetChanged()
        }
    }
    fun updateTask(updateTask: itemTask){
        val index = dataset.indexOfFirst { it.id == updateTask.id }
        if (index != -1){
            dataset[index].title = updateTask.title
            dataset[index].endDate = updateTask.endDate
            dataset[index].status
            notifyDataSetChanged()
        }
    }
}