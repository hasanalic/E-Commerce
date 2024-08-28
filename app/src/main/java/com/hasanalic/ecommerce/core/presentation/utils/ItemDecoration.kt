package com.hasanalic.ecommerce.core.presentation.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecoration(private val leftSpacing: Int, private val rightSpacing: Int, private val topSpacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.apply {
            left = leftSpacing
            right = rightSpacing
            top = topSpacing
        }
    }
}