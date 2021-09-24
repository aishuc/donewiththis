package com.example.myexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myexample.databinding.ActivityMainBinding
import com.example.myexample.databinding.ItemTodoBinding
import retrofit2.HttpException
import java.io.IOException

const val  TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityMainBinding

    private lateinit var todoAdapter: TodoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerview()

        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible = true
            val response = try {
                RetrofitInstance.api.getTodos()
            }catch (e: IOException){
                Log.e(TAG, "IOException,you might not have Internet Connection ")
                binding.progressBar.isVisible = false
            }catch (e: HttpException){
                Log.e(TAG, "HttpException,unexpected Response" )
                return@launchWhenCreated

            }
            if (response.isSucessful && response.body() !=null){
                todoAdapter.todos = response.body()!!

            }
            else{
                Log.e(TAG, "Response not successful" )
            }
            binding.progressBar.isVisible = false
        }
    }
    private fun setupRecyclerview() = binding.rvTodos.apply {
        todoAdapter = TodoAdapter()
        adapter = todoAdapter
       layoutManager = LinearLayoutManager(this@MainActivity)
    }
}