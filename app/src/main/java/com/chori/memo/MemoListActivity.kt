package com.chori.memo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class MemoListActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_list)
        initView()
    }

    private lateinit var emptyView: TextView
    private lateinit var adapter: MemoListAdapter
    private fun initView() {
        emptyView = findViewById(R.id.tv_memo_empty)
        adapter = MemoListAdapter()
        val rv = findViewById<RecyclerView>(R.id.rv_memo_list)
        rv.adapter = adapter

        findViewById<Button>(R.id.btn_edit).setOnClickListener {
            startActivity(Intent(this@MemoListActivity, MemoEditActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val data = MemoRepository.getMemoList()
        if (data.isNotEmpty()) {
            adapter.setData(data)
            emptyView.visibility = View.GONE
        }
        else {
            emptyView.visibility = View.VISIBLE
        }
    }
}