package com.example.mobileproject.activities

import SelecaoObrasAdapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobileproject.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SelecaoObras2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecao_obras2)

        val items = List(5) { "Item $it" } // Replace with your actual data

        val recyclerView: RecyclerView = findViewById(R.id.RecyclerViewSelecaoObras)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SelecaoObrasAdapter(items)


    }
}