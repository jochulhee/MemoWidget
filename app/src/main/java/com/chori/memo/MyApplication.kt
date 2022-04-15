package com.chori.memo

import android.app.Application

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MemoRepository.init(this)
    }
}