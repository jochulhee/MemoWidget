package com.chori.memo.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.chori.memo.repository.MemoRepository
import com.chori.memo.databinding.ActivityMemoEditBinding
import com.chori.memo.models.Memo
import com.chori.memo.utils.GBLog
import java.text.SimpleDateFormat
import java.util.*

class MemoEditActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMemoEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }
    private var memo: Memo? = null
    private var edited = false
    private fun initView() {
        val id = intent.getLongExtra("index", -1)
        GBLog.i("TAG", "index : $id")

        if (id > 0) {
            memo = MemoRepository.getMemo(id)

            // can delete memo
            binding.btnMemoDelete.visibility = View. VISIBLE
            binding.btnMemoDelete.setOnClickListener {
                // delete memo
                MemoRepository.deleteMemo(this, id)
                edited = false
                finish()
            }
        }

        if (memo == null)
            memo = Memo()

        binding.etMemoEditTitle.setText(memo!!.title)
        binding.etMemoEditContents.setText(memo!!.contents)

        binding.etMemoEditTitle.addTextChangedListener {
            edited = true
            GBLog.d("TAG", "title changed")
        }
        binding.etMemoEditContents.addTextChangedListener {
            edited = true
            GBLog.d("TAG", "contents changed")
        }
    }

    override fun finish() {
        if (edited) {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
            memo!!.title = binding.etMemoEditTitle.text.toString()
            memo!!.contents = binding.etMemoEditContents.text.toString()
            memo!!.date = format.format(System.currentTimeMillis())
            MemoRepository.saveMemo(this, memo!!)
        }
        super.finish()
    }
}