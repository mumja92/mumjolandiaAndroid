package com.mumjolandia.android.ui.tetris

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.AsyncTask
import android.view.MotionEvent
import android.view.View
import com.mumja.tetris.GameStatus
import com.mumja.tetris.InputData
import com.mumja.tetris.board.*

class TetrisView(context: Context?) : View(context) {

    private var gameStatus = GameStatus(Board(0, 0), Block(BlockType.EMPTY), 0)
    private var touchData = InputData(null, null)
    private var tetrisTask : TetrisEngine = TetrisEngine(this, gameStatus, touchData)
    private var blockSize = 80F
    private val screenSizeX = Resources.getSystem().displayMetrics.widthPixels;
    private val userBlockCalculator = UserBlockCalculator()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (tetrisTask.status != AsyncTask.Status.RUNNING){
            tetrisTask.execute()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        tetrisTask.cancel(true)
    }

    override fun onDraw(canvas: Canvas) {
        try{
            blockSize = screenSizeX/gameStatus.board.sizeY.toFloat()
            drawBoard(canvas)
            drawStatus(canvas)
        }
        catch (e: ArrayIndexOutOfBoundsException){
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val eventAction = event!!.action
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (eventAction) {
            MotionEvent.ACTION_DOWN -> {
                if (touchData.posX == null){
                    touchData.posX = x
                    touchData.posY = y
                }
            }
            MotionEvent.ACTION_UP -> {
            }
            MotionEvent.ACTION_MOVE -> {
            }
        }
        return true
    }

    private fun drawBoard(canvas: Canvas){
        for (x in 0 until gameStatus.board.sizeX) {
            for (y in 0 until gameStatus.board.sizeY) {
                val block = gameStatus.board.getLocation(x, y)
                BlockDrawingScheme.drawRectangle(canvas, block!!.blockColor, y * blockSize, x * blockSize, blockSize + y * blockSize, x * blockSize + blockSize)
            }
        }
    }
    private fun drawStatus(canvas: Canvas){
        val startPositionY = blockSize*gameStatus.board.sizeX + 80
        val startPositionX = 200F
        val smallBlockSize = blockSize/2
        val paint = Paint()
        paint.color = Color.BLACK
        canvas.drawText("SCORE: ${gameStatus.score}", 0F, startPositionY , paint)
        for (point in userBlockCalculator.getBlocks(gameStatus.nextUserBlock)){
            BlockDrawingScheme.drawRectangle(canvas,
                    gameStatus.nextUserBlock.blockColor,
                    startPositionX + point.first*smallBlockSize,
                    startPositionY + point.second*smallBlockSize,
                    startPositionX + point.first*smallBlockSize + smallBlockSize,
                    startPositionY + point.second*smallBlockSize + smallBlockSize)
            //BlockDrawingScheme.drawRectangle(g, gameStatus!!.nextUserBlock.blockColor, xShift + point.second*sizeY, secondShift + point.first * sizeX, sizeY, sizeX)
        }
    }
}