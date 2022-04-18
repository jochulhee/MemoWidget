package com.chori.memo.utils

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import com.chori.memo.MemoViewService
import com.chori.memo.R
import com.chori.memo.views.MemoEditActivity

const val REFRESH_ACTION = "com.chori.memo.REFRESH_ACTION"
const val CLICK_ACTION = "com.chori.memo.CLICK_ACTION"
const val EXTRA_ITEM = "com.chori.memo.EXTRA_ITEM"
//android.appwidget.action.APPWIDGET_UPDATE_OPTIONS
class WidgetProvider: AppWidgetProvider() {
    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?,appWidgetIds: IntArray?) {
        appWidgetIds?.forEach { appWidgetId ->
            GBLog.i("TAG", "widget : $appWidgetId")
            val serviceIntent = Intent(context, MemoViewService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }

            val views = RemoteViews(context?.packageName, R.layout.widget).apply {
                setTextViewText(R.id.tv_widget_title, "메모")
                setRemoteAdapter(R.id.lv_widget_memo_list, serviceIntent)
                setEmptyView(R.id.lv_widget_memo_list, R.id.tv_widget_empty_list)
            }

            val intent = Intent(context, WidgetProvider::class.java).apply {
                action = CLICK_ACTION
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }

            val templatePendingIntent = if (Build.VERSION.SDK_INT >= 31) {
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
            } else
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            views.setPendingIntentTemplate(R.id.lv_widget_memo_list, templatePendingIntent)

            views.setOnClickPendingIntent(R.id.btn_edit, templatePendingIntent)
            appWidgetManager?.updateAppWidget(appWidgetId, views)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val appWidgetId: Int = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        GBLog.v("TAG", "appWidgetId = $appWidgetId")

        GBLog.i("TAG", "action : ${intent.action}")
        if (intent.action == CLICK_ACTION) {
            val id: Long = intent.getLongExtra(EXTRA_ITEM, -1)
            GBLog.v("TAG", "click item = $id")

            context.startActivity(Intent(context, MemoEditActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("index", id)
            })
        } else if (intent.action == REFRESH_ACTION) {
            GBLog.v("TAG", "refresh")
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context,  WidgetProvider::class.java))
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_widget_memo_list)
        }
        super.onReceive(context, intent)
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
    }
}