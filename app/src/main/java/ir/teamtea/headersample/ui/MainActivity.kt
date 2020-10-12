package ir.teamtea.headersample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ir.teamtea.headerlibrary.HeaderItemDecoration
import ir.teamtea.headersample.data.FakeData
import ir.teamtea.headersample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = ItemsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initAdapter()

        requestData()

    }

    private fun requestData() {
        adapter.submitList(FakeData.createData())
    }

    private fun initAdapter() {
        binding.list.adapter = adapter
        binding.list.addItemDecoration(HeaderItemDecoration(binding.list, false))
    }
}