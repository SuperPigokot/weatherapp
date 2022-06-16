package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class UviActivity : AppCompatActivity() {
    private val uviDatabase by lazy { com.example.myapplication.uviDatabase.getDatabase(this).ufIndexDao() }
    private val creamDatabase by lazy { com.example.myapplication.CreamDatabase.getDatabase(this).dataDao() }
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uvi)
        val uviValue = intent.getSerializableExtra("uvi_value") as String
        val uviLabel: TextView = findViewById(R.id.uviValue)
        uviLabel.text = uviLabel.text.toString() + " " + uviValue
        val uviFloat = uviValue.toFloat()
        val uviId = uviDatabase.getUviByDiapason(uviFloat)
        val creamsObject = creamDatabase.getCream(uviId)
        val creamList: MutableList<String> = ArrayList()
        val creamListView: ListView = findViewById(R.id.creamList)
        for (cream in creamsObject) {
            creamList.add(cream.name.toString())
        }
        val adapter: Any? = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_list_item_1, creamList as List<Any?>
        )
        creamListView.setAdapter(adapter as ListAdapter?)
    }
}