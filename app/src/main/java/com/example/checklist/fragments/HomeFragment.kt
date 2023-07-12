package com.example.checklist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.checklist.databinding.FragmentHomeBinding

import com.example.checklist.util.ToDoAdapter
import com.example.checklist.util.ToDoData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class HomeFragment : Fragment(), AddChecklistPopupFragment.dialogeNextBtnClickListner,
    ToDoAdapter.ToDoAdapterClicksInterface {


    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private var popUpFragment: AddChecklistPopupFragment? = null
    private lateinit var adapter: ToDoAdapter
    private lateinit var mList: MutableList<ToDoData>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getDataFromFirebase()
        registerEvents()

    }

    private fun registerEvents() {
        binding.homeAddBtn.setOnClickListener {
            if (popUpFragment != null)
                childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
            popUpFragment = AddChecklistPopupFragment()
            popUpFragment!!.setListner(this)
            popUpFragment!!.show(
                childFragmentManager, AddChecklistPopupFragment.TAG
            )
        }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child("Tasks")
            .child(auth.currentUser?.uid.toString())
        binding.RecyclerView.setHasFixedSize(true)
        binding.RecyclerView.layoutManager = LinearLayoutManager(context)
        mList = mutableListOf()
        adapter = ToDoAdapter(mList)
        adapter.setListener(this@HomeFragment)
        binding.RecyclerView.adapter = adapter
    }

    private fun getDataFromFirebase() {

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (taskSnapshot in snapshot.children) {
                    val todoTask = taskSnapshot.key?.let {
                        ToDoData(it, taskSnapshot.value.toString())
                    }
                    if (todoTask != null) {
                        mList.add(todoTask)

                    }

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()

            }
        })
    }

    override fun onSaveTask(todo: String, todoEt: TextInputEditText) {
        databaseRef.push().setValue(todo).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Todo saved Successfully !!", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            todoEt.text = null
            popUpFragment!!.dismiss()
        }
    }

    override fun onUpdateTask(todoData: ToDoData, todoEt: TextInputEditText) {
        val map = HashMap<String, Any>() //Firebase functionality works only with maps
        map[todoData.taskId] = todoData.task
        databaseRef.updateChildren(map).addOnCompleteListener() {
            if (it.isSuccessful) {
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            todoEt.text = null
            popUpFragment!!.dismiss()
        }
    }


    override fun onDeleteTaskBtnClicked(todoData: ToDoData) {
        databaseRef.child(todoData.taskId).removeValue().addOnCompleteListener {

            if (it.isSuccessful) {
                Toast.makeText(context, "Deleted Successfully ", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }

        }


    }

    override fun onEditTaskBtnClicked(toDoData: ToDoData) {
        if (popUpFragment != null) {
            childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
        }
popUpFragment=AddChecklistPopupFragment.newInstance(toDoData.taskId,toDoData.task)
        popUpFragment!!.setListner(this)
      popUpFragment!!.show(childFragmentManager,AddChecklistPopupFragment.TAG)
    }



}
