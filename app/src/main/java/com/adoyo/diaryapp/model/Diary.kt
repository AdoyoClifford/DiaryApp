package com.adoyo.diaryapp.model

import androidx.room.PrimaryKey
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import org.mongodb.kbson.BsonObjectId

class Diary : RealmObject {
    @PrimaryKey
    var _id = BsonObjectId
    var ownerId: String = ""
    var title: String = ""
    var description: String = ""
    var images: RealmList<String> = realmListOf()
    var date: RealmInstant = RealmInstant.from(System.currentTimeMillis(), 0)
}