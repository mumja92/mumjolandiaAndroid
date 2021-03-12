package com.mumja.tetris.board

import com.mumja.tetris.GameOverException
import com.mumja.tetris.InputCommand

class BoardSupervisor {
    private val boardSizeX = 15
    private val boardSizeY = 10
    private val board = Board(boardSizeX, boardSizeY)
    private var nextUserBlock = Block(getRandomBLockType(), getRandomBlockColor(), -1)
    private var score = 0
    private var userBlock = Block(BlockType.EMPTY)
    private var userBlockPositionX = 0
    private var userBlockPositionY = 0
    private val userBlockCalculator = UserBlockCalculator()

    init {
        generateNewUserBlock()
    }

    fun nextFrame(input: InputCommand, nextTick: Boolean = true){
        unDrawUserBlock()
        removeFullLines()
        moveUserBlock(input, nextTick)
        drawUserBlock()
    }

    fun getBoard(): Board{
        return board
    }

    fun getNextUserBlock(): Block{
        return nextUserBlock
    }

    fun getScore(): Int{
        return score
    }

    private fun moveUserBlock(input: InputCommand, nextTick:Boolean){
        if (nextTick){
            moveUserBlockOneLineDown()
        }
        when (input) {
            InputCommand.LEFT -> {
                if (checkUserBlockCanMove(userBlockPositionX, userBlockPositionY-1)){
                    userBlockPositionY--
                }
            }
            InputCommand.RIGHT -> {
                if (checkUserBlockCanMove(userBlockPositionX, userBlockPositionY+1)){
                    userBlockPositionY++
                }
            }
            InputCommand.SPEED -> {
                moveUserBlockOneLineDown()
            }
            InputCommand.ROTATE -> {
                if (isRotatePossible()){
                    userBlock.rotate()
                }
            }
            InputCommand.EXIT -> {
                throw GameOverException()
            }
            InputCommand.MOVE_DOWN -> {
                moveUserBlockAllWayDown()
            }
            else -> {
            }
        }
    }

    private fun sealUserBlock(){
        if (userBlockPositionX == 0){
            throw GameOverException()
        }
        for (point in userBlockCalculator.getBlocks(userBlock)){
            board.setLocation(userBlockPositionX + point.first, userBlockPositionY + point.second, userBlock)
        }
    }

    private fun drawUserBlock(){
        val points = userBlockCalculator.getBlocks(userBlock)
        for (point in points){
            board.setLocation(userBlockPositionX + point.first, userBlockPositionY + point.second, Block(BlockType.POINT, userBlock.blockColor))
        }
    }

    private fun unDrawUserBlock(){
        for (point in userBlockCalculator.getBlocks(userBlock)){
            board.setLocation(userBlockPositionX + point.first, userBlockPositionY + point.second, Block(BlockType.EMPTY))
        }
    }


    private fun moveUserBlockOneLineDown(){
        if (!checkUserBlockCanMove(userBlockPositionX+1, userBlockPositionY)){
            sealUserBlock()
            generateNewUserBlock()
        }
        else{
            userBlockPositionX++
        }
    }

    private fun checkUserBlockCanMove(targetX: Int, targetY: Int): Boolean{
        for (point in userBlockCalculator.getBlocks(userBlock)){
            val block = board.getLocation(targetX + point.first, targetY + point.second)
            if (block == null){
                return false
            }
            else {
                if (block.blockType != BlockType.EMPTY){
                    return false
                }
            }
        }
        return true
    }

    private fun removeFullLines(){
        for (i in 1 until boardSizeX){
            if (lineIsFull(i)){
                score++
                moveBoardOneLineDown(i)
            }
        }
    }

    private fun lineIsFull(lineIndex: Int): Boolean{
        for (i in 0 until boardSizeY){
            if (board.getLocation(lineIndex, i)!!.blockType == BlockType.EMPTY){
                return false
            }
        }
        return true
    }

    private fun moveBoardOneLineDown(startIndex: Int){
        for (i in startIndex downTo 1){
            for (j in 0 until boardSizeY){
                board.setLocation(i, j, board.getLocation(i-1, j)!!)
            }
        }
    }

    private fun generateNewUserBlock(){
        userBlock = nextUserBlock
        nextUserBlock = Block(getRandomBLockType(), getRandomBlockColor(), -1)
        userBlockPositionX = 0
        userBlockPositionY = boardSizeY/2
    }

    private fun isRotatePossible(): Boolean{
        val rotatedBlock = Block(userBlock.blockType, userBlock.blockColor, userBlock.blockRotation)
        rotatedBlock.rotate()
        for (point in userBlockCalculator.getBlocks(rotatedBlock)){
            val block = board.getLocation(userBlockPositionX + point.first, userBlockPositionY + point.second) ?: return false
            if (block.blockType != BlockType.EMPTY){
                return false
            }
        }
        return true
    }

    private fun getRandomBLockType(): BlockType {
        val value = (0 until BlockType.values().size - 2).random()
        return BlockType.values()[value]
    }

    private fun getRandomBlockColor(): BlockColor {
        val value = (0 until BlockColor.values().size - 1).random()
        return BlockColor.values()[value]
    }

    private fun moveUserBlockAllWayDown(){
        while (checkUserBlockCanMove(userBlockPositionX+1, userBlockPositionY)){
            userBlockPositionX++
        }
    }
}