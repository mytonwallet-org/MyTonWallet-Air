package org.mytonwallet.app_air.uicomponents.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import kotlin.math.roundToInt

fun View.setOnClickListener(listener: (() -> Unit)?) {
    listener?.let { l ->
        this.setOnClickListener { l.invoke() }
    } ?: run {
        this.setOnClickListener(null)
    }
}

fun View.setPaddingDp(left: Float, top: Float, right: Float, bottom: Float) {
    this.setPadding(
        left.dp.roundToInt(),
        top.dp.roundToInt(),
        right.dp.roundToInt(),
        bottom.dp.roundToInt()
    )
}

fun View.setPaddingDp(left: Int, top: Int, right: Int, bottom: Int) {
    this.setPadding(
        left.dp,
        top.dp,
        right.dp,
        bottom.dp
    )
}

fun View.setPaddingDp(size: Int) {
    setPaddingDp(size, size, size, size)
}

fun View.setPaddingDp(size: Float) {
    setPaddingDp(size, size, size, size)
}

fun View.asImage(): Bitmap? {
    if (width == 0 || height == 0)
        return null
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    draw(canvas)
    return bitmap
}
