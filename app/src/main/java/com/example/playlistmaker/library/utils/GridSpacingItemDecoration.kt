package com.example.playlistmaker.library.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int, // Количество столбцов
    private val spacing: Int,   // Расстояние между элементами
    private val includeEdge: Boolean // Учитывать отступы от краев
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // Позиция элемента
        val column = position % spanCount                  // Колонка элемента

        if (includeEdge) {
            // Отступы от левого и правого края
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount

            // Отступы сверху
            if (position < spanCount) { // Верхний ряд
                outRect.top = 0
            }
            outRect.bottom = spacing/2 // Нижний отступ
        } else {
            // Без отступов от краев
            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) {
                outRect.top = 0 // Отступ сверху для остальных рядов
            }
        }
    }
}
