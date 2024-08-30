package com.hasanalic.ecommerce.core.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hasanalic.ecommerce.R

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun Fragment.toast(context: Context, msg: String, isLong: Boolean) {
    if (isLong) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    } else {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}

fun ImageView.glide(url: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.ic_broken_image)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

fun placeHolderProgressBar(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}

fun Int.toCent(): String {
    if (this.toString().length == 1) {
        return this.toString().let { "0$it" }
    }
    return this.toString()
}

fun String.maskCreditCard(): String {
    val firstPart = this.substring(0, 4).padEnd(4, '*')
    val lastPart = this.substring(this.length - 2).padStart(2, '*')
    val middlePart = this.substring(4, this.length - 2)
        .chunked(4) { "****" }
        .joinToString(separator = " ")
    return "$firstPart $middlePart $lastPart"
}