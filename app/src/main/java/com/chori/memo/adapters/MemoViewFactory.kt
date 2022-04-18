package com.chori.memo.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.chori.memo.repository.MemoRepository
import com.chori.memo.R
import com.chori.memo.models.Memo
import com.chori.memo.utils.EXTRA_ITEM
import com.chori.memo.utils.GBLog

class MemoViewFactory(private val context: Context): RemoteViewsService.RemoteViewsFactory {

    private lateinit var data:MutableList<Memo>
    override fun onCreate() {
        GBLog.i("TAG", "onCreate")

//        data.add(Memo(0,"dummy","dummy",""))
//        data.addAll(MemoRepository.getMemoList())
    }

    override fun onDataSetChanged() {
        GBLog.i("TAG", "onDataSetChanged")
        data = ArrayList()
        data.addAll(MemoRepository.getMemoList())
    }

    override fun onDestroy() { }

    override fun getCount(): Int {
        return data.size
    }

    override fun getViewAt(pos: Int): RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_item).apply {
            val memo = data[pos]
            setTextViewText(R.id.tv_title, memo.title)
            setTextViewText(R.id.tv_contents, memo.contents)
            setTextViewText(R.id.tv_date, memo.date)

            val intent = Intent().apply {
                Bundle().also { extras ->
                    extras.putLong(EXTRA_ITEM, memo.id)
                    putExtras(extras)
                }
            }
            setOnClickFillInIntent(R.id.memo_item, intent)
        }
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return false
    }
}