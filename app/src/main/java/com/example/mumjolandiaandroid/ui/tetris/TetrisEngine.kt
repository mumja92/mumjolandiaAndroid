package com.example.mumjolandiaandroid.ui.tetris

import android.os.AsyncTask
import android.view.View
import com.mumja.tetris.*
import com.mumja.tetris.board.BoardSupervisor

open class TetrisEngine(val context: View, var gameStatusReference: GameStatus, var touchLocation: InputData): AsyncTask<String, Void, String>() {
    private val boardSupervisor = BoardSupervisor()
    private val timer = Timer(1000,50)
    private var nextTick = false
    private val inputParser = InputParser()
    var input = InputCommand.NONE

    override fun doInBackground(vararg p0: String?): String {
        while (true) {
            try {
                input = inputParser.parseInput(touchLocation)
                boardSupervisor.nextFrame(input, nextTick)
                fillGameStatus()
                publishProgress()
                nextTick = timer.handleTime()
            }
            catch (e: GameOverException){
                callGameOver()
                break
            }

        }
        return "abc"
    }

    override fun onProgressUpdate(vararg values: Void?) {
        super.onProgressUpdate(*values)
        context.invalidate()
    }

    private fun callGameOver(){
    }

    private fun fillGameStatus(){
        gameStatusReference.board = boardSupervisor.getBoard()
        gameStatusReference.nextUserBlock = boardSupervisor.getNextUserBlock()
        gameStatusReference.score = boardSupervisor.getScore()
    }
}
