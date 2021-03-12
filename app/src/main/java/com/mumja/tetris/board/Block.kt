package com.mumja.tetris.board

class Block (
    var blockType: BlockType,
    var blockColor: BlockColor = BlockColor.LIGHT_GRAY,
    rotation: Int = 0,
){
    var blockRotation = rotation
        private set

    init {
        if (blockRotation < 0){
            blockRotation = (0..3).random()
        }
        if (blockRotation > 3){
            blockRotation = (0..3).random()
        }
    }
    fun rotate(){
        if (blockRotation < 3){
            blockRotation++
        }
        else{
            blockRotation = 0
        }
    }
}