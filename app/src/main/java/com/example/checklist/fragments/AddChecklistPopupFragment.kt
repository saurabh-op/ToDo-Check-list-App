package com.example.checklist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.checklist.databinding.FragmentAddChecklistPopupBinding
import com.example.checklist.util.ToDoData
import com.google.android.material.textfield.TextInputEditText

class AddChecklistPopupFragment : DialogFragment() {
    private lateinit var binding: FragmentAddChecklistPopupBinding
    private var listener: dialogeNextBtnClickListner? = null

    private var toDoData: ToDoData? = null

    fun setListner(listener: dialogeNextBtnClickListner) {
        this.listener = listener
    }

    // Rest of the code...


    companion object {
        const val TAG = "AddToDoPopUpFragment"

        @JvmStatic
        fun newInstance(taskId: String, task: String) = AddChecklistPopupFragment().apply {
            arguments = Bundle().apply {
                putString("taskId", taskId)
                putString("task", task)
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddChecklistPopupBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            toDoData =
                ToDoData(
                    arguments?.getString("taskId").toString(),
                    arguments?.getString("task").toString()
                )
            binding.addTaskString.setText(toDoData?.task)

        }
        registerEvents()
    }

    private fun registerEvents() {
        binding.popUpSave.setOnClickListener {
            val todotask = binding.addTaskString.text.toString()
            if (todotask.isNotEmpty()) {
                if (toDoData == null) {
                    listener?.onSaveTask(todotask, binding.addTaskString)
                } else {
                    toDoData?.task = todotask
                    listener?.onUpdateTask(toDoData!!, binding.addTaskString)

                }

            } else {
                Toast.makeText(context, "Please enter the task", Toast.LENGTH_SHORT).show()
            }
        }

        binding.popUpClose.setOnClickListener {
            dismiss()
        }
    }


    interface dialogeNextBtnClickListner {
        fun onSaveTask(todo: String, todoEt: TextInputEditText)
        fun onUpdateTask(todoData: ToDoData, todoEt: TextInputEditText)
    }

}