package com.chori.memo

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MemoListAdapter:RecyclerView.Adapter<MemoListAdapter.ViewHolder>() {

    private var memoList:List<Memo> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.memo_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = memoList[position]
        holder.apply {
            bind(item) {
                it.context.startActivity(Intent(it.context, MemoEditActivity::class.java).apply {
                    putExtra("index", item.id)
                })
            }
        }
    }

    public fun setData(data: List<Memo>) {
        memoList = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return memoList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val back = itemView.findViewById<View>(R.id.cover_onclick)!!
        private val title = itemView.findViewById<TextView>(R.id.tv_memo_item_title)!!
        private val date = itemView.findViewById<TextView>(R.id.tv_memo_item_date)!!

        fun bind(data : Memo, listener: View.OnClickListener){
            back.setOnClickListener(listener)
            title.text = data.title
            date.text = data.date
        }
    }
}