package org.mytonwallet.app_air.uicomponents.helpers

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CenteringLinearLayoutManager(
    context: Context,
    orientation: Int = RecyclerView.HORIZONTAL,
    reverseLayout: Boolean = false
) : LinearLayoutManager(context, orientation, reverseLayout) {

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)

        if (orientation == HORIZONTAL) {
            centerHorizontally()
        } else {
            centerVertically()
        }
    }

    private fun centerHorizontally() {
        if (childCount == 0) return

        val recyclerWidth = width - paddingLeft - paddingRight
        var contentWidth = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child?.let {
                contentWidth += getDecoratedMeasuredWidth(it)
            }
        }

        if (contentWidth < recyclerWidth) {
            val offset = (recyclerWidth - contentWidth) / 2

            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child?.let {
                    val left = getDecoratedLeft(it) + offset
                    val top = getDecoratedTop(it)
                    val right = left + getDecoratedMeasuredWidth(it)
                    val bottom = top + getDecoratedMeasuredHeight(it)

                    layoutDecorated(it, left, top, right, bottom)
                }
            }
        }
    }

    private fun centerVertically() {
        if (childCount == 0) return

        val recyclerHeight = height - paddingTop - paddingBottom
        var contentHeight = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child?.let {
                contentHeight += getDecoratedMeasuredHeight(it)
            }
        }

        if (contentHeight < recyclerHeight) {
            val offset = (recyclerHeight - contentHeight) / 2

            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child?.let {
                    val left = getDecoratedLeft(it)
                    val top = getDecoratedTop(it) + offset
                    val right = left + getDecoratedMeasuredWidth(it)
                    val bottom = top + getDecoratedMeasuredHeight(it)

                    layoutDecorated(it, left, top, right, bottom)
                }
            }
        }
    }
}
