package com.mumja.tetris

import android.content.res.Resources

class InputParser {
    private val screenSizeX = Resources.getSystem().displayMetrics.widthPixels;
    private val screenSizeY = Resources.getSystem().displayMetrics.heightPixels;

    fun parseInput(touchLocation: InputData): InputCommand{
        if (touchLocation.posX == null){
            return InputCommand.NONE
        }
        if (touchLocation.posY!! < screenSizeY/2){
            if (touchLocation.posX!! < screenSizeX/2){
                touchLocation.posX = null
                touchLocation.posY = null
                return InputCommand.MOVE_DOWN
            }
            else {
                touchLocation.posX = null
                touchLocation.posY = null
                return InputCommand.ROTATE
            }
        }
        else{
            if (touchLocation.posX!! < screenSizeX/2){
                touchLocation.posX = null
                touchLocation.posY = null
                return InputCommand.LEFT
            }
            else {
                touchLocation.posX = null
                touchLocation.posY = null
                return InputCommand.RIGHT
            }
        }
    }
}