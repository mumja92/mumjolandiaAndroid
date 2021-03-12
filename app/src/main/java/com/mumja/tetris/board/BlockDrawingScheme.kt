package com.mumja.tetris.board

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class BlockDrawingScheme {
    companion object{
        private var paint = Paint()
        fun drawRectangle (canvas: Canvas, color: BlockColor, posX: Float, posY: Float, width: Float, height: Float){
            paint.color = Color.BLACK
            canvas.drawRect(posX, posY, width, height, paint)
            when (color){
                BlockColor.BLUE -> paint.color = Color.BLUE
                BlockColor.CYAN -> paint.color = Color.CYAN
                BlockColor.GREEN -> paint.color = Color.GREEN
                BlockColor.MAGNETA -> paint.color = Color.MAGENTA
                BlockColor.ORANGE -> paint.color = Color.rgb(255,140,0)
                BlockColor.PINK -> paint.color = Color.rgb(255,105,180)
                BlockColor.RED -> paint.color = Color.RED
                BlockColor.YELLOW -> paint.color = Color.YELLOW
                BlockColor.LIGHT_GRAY -> paint.color = Color.rgb(220,220,220)
            }
            canvas.drawRect(posX+1, posY+1, width-1, height-1, paint)
        }
    }
}