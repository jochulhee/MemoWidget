package com.chori.memo

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import java.text.SimpleDateFormat
import java.util.*

class MemoEditActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_edit)

        initView()
    }
    private var memo: Memo? = null
    private var edited = false
    lateinit var title:EditText
    lateinit var contents:EditText

    private fun initView() {
        title = findViewById(R.id.et_memo_edit_title)
        contents = findViewById(R.id.et_memo_edit_contents)

        val id = intent.getLongExtra("index", -1)
        GBLog.i("TAG", "index : $id")

        if (id > 0)
            memo = MemoRepository.getMemo(id)

        if (memo == null)
            memo = Memo()

        title.setText(memo!!.title)
        contents.setText(memo!!.contents)

        title.addTextChangedListener {
            edited = true
            GBLog.d("TAG", "title changed")
        }
        contents.addTextChangedListener {
            edited = true
            GBLog.d("TAG", "contents changed")
        }
    }

    override fun finish() {
        if (edited) {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
            memo!!.title = title.text.toString()
            memo!!.contents = contents.text.toString()
            memo!!.date = format.format(System.currentTimeMillis())
            MemoRepository.saveMemo(this, memo!!)
        }
        super.finish()
    }
}