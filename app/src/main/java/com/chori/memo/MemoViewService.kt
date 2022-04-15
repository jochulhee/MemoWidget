package com.chori.memo

import android.content.Intent
import android.widget.RemoteViewsService

class MemoViewService: RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return MemoViewFactory(this.applicationContext, intent)
    }
}