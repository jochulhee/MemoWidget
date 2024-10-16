package com.chori.memo.repository

import android.content.Context
import android.content.Intent
import com.chori.memo.models.Memo
import com.chori.memo.utils.GBLog
import com.chori.memo.utils.REFRESH_ACTION
import com.chori.memo.utils.WidgetProvider
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import io.realm.kotlin.where

object MemoRepository {
    fun init(context: Context) {
        Realm.init(context)
        val config: RealmConfiguration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }

    fun getMemo(id: Long): Memo? {
        val realm = Realm.getDefaultInstance()
        val memo = realm.where<Memo>().equalTo("id", id).findFirst()
        val item = Memo()
        if (memo?.isLoaded == true) {
            item.copy(memo)
        }
        realm.close()
        return if (memo == null)
                    null
                else item
    }

    fun getMemoList():List<Memo> {
        val data:MutableList<Memo> = ArrayList()
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val memoList = it.where(Memo::class.java)
                .findAll()
                .sort("id")

            if (memoList.isLoaded) {
                for (memo in memoList) {
                    val item = Memo()
                    item.copy(memo)
                    data.add(item)
                }
            }
            GBLog.v("TAG", "data size : ${data.size}")
        }
        realm.close()
        return data
    }

    fun saveMemo(context: Context, memo: Memo) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            var newMemo = it.where<Memo>().equalTo("id",memo.id).findFirst()
            if (newMemo == null) {
                val maxId = it.where<Memo>().max("id")?.toLong()  ?: run { 0 }
                newMemo = it.createObject(maxId + 1)
            }

            newMemo.date = memo.date
            newMemo.title = memo.title
            newMemo.contents = memo.contents

            context.sendBroadcast(Intent(context, WidgetProvider::class.java).apply {
                action = REFRESH_ACTION
            })
        }
        realm.close()
    }

    fun deleteMemo(context: Context, id: Long) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            it.where<Memo>().equalTo("id", id).findFirst()?.deleteFromRealm()

            context.sendBroadcast(Intent(context, WidgetProvider::class.java).apply {
                action = REFRESH_ACTION
            })
        }
        realm.close()
    }
}
