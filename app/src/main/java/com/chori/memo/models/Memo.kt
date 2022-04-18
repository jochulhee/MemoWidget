package com.chori.memo.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Memo(
    @PrimaryKey var id:Long = 0,
    var title:String = "",
    var contents: String = "",
    var date: String = ""
): RealmObject() {
    fun copy(memo: Memo) {
        this.id = memo.id
        this.title= memo.title
        this.contents= memo.contents
        this.date= memo.date
    }
}
