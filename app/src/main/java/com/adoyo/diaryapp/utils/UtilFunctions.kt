package com.adoyo.diaryapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import io.realm.kotlin.types.RealmInstant
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
fun RealmInstant.toInstant(): Instant {
    val sec: Long = this.epochSeconds
    val nano: Int = this.nanosecondsOfSecond

    return if (sec >= 0) {
        Instant.ofEpochSecond(sec, nano.toLong())
    } else {
        Instant.ofEpochSecond(sec - 1, 1_000_000 + nano.toLong())
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.toRealmInstant(): RealmInstant {
    val sec = this.epochSecond
    val nano = this.nano
    return if (sec >= 0) {
        RealmInstant.from(sec, nano)
    } else {
        RealmInstant.from(sec + 1,-1_000_000 + nano)
    }
}