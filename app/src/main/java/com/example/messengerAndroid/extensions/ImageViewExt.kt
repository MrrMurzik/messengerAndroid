package com.example.messengerAndroid.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.messengerAndroid.R
import com.example.messengerAndroid.data.contactsRepository.contactModel.User

fun ImageView.addPhoto(user: User) {
    if (user.photo.isNotBlank()) {
        Glide.with(this)
            .load(user.photo)
            .circleCrop()
            .placeholder(R.drawable.ic_baseline_sentiment_very_satisfied_24)
            .error(R.drawable.ic_baseline_sentiment_very_satisfied_24)
            .into(this)
    } else {
        this.setImageResource(R.drawable.ic_baseline_sentiment_very_satisfied_24)
    }
}

fun ImageView.cropPhoto(res: Int) {
    Glide.with(this)
        .load(res)
        .circleCrop()
        .placeholder(R.drawable.ic_baseline_sentiment_very_satisfied_24)
        .error(R.drawable.ic_baseline_sentiment_very_satisfied_24)
        .into(this)
}