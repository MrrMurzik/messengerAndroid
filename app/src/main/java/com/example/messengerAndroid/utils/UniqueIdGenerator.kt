package com.example.messengerAndroid.utils

import java.util.*

object UniqueIdGenerator {
    fun getUniqueId() = UUID.randomUUID().toString()
}