package com.example.checklist.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.checklist.databinding.EachTodoBinding
import com.example.checklist.fragments.HomeFragment

class ToDoAdapter(private val list: MutableList<ToDoData>) :
    RecyclerView.Adapter<ToDoAdapter.TodoViewHolder>() {
    private var listener: ToDoAdapterClicksInterface? = null
    fun setListener(listener: HomeFragment) {
        this.listener = listener
    }

    inner class TodoViewHolder(val binding: EachTodoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = EachTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TodoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        with(holder)
        {
            with(list[position]) {
                binding.todoTask.text = this.task
                binding.deleteTask.setOnClickListener()
                {
                    listener?.onDeleteTaskBtnClicked(this)


                }

                binding.editTask.setOnClickListener()
                {
                    listener?.onEditTaskBtnClicked(this)
                }

            }
        }

    }


    interface ToDoAdapterClicksInterface {
        fun onDeleteTaskBtnClicked(toDoData: ToDoData)
        fun onEditTaskBtnClicked(toDoData: ToDoData)
    }
}