package com.chori.memo

import android.app.Application
import com.chori.memo.repository.MemoRepository

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MemoRepository.init(this)
    }
}