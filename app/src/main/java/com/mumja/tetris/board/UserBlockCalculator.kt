package com.mumja.tetris.board

class UserBlockCalculator {
    fun getBlocks(block: Block):ArrayList<Pair<Int,Int>>{
        return when(block.blockType){
            BlockType.I ->{
                getBlockI(block.blockRotation)
            }
            BlockType.O ->{
                getBlockO(block.blockRotation)
            }
            BlockType.T ->{
                getBlockT(block.blockRotation)
            }
            BlockType.S ->{
                getBlockS(block.blockRotation)
            }
            BlockType.Z ->{
                getBlockZ(block.blockRotation)
            }
            BlockType.J ->{
                getBlockJ(block.blockRotation)
            }
            BlockType.L ->{
                getBlockL(block.blockRotation)
            }
            else ->{
                getBlockEMPTY()
            }
        }
    }

    private fun getBlockI(rotation: Int): ArrayList<Pair<Int,Int>>{
        val blockList = ArrayList<Pair<Int, Int>>()
        when(rotation){
            0 -> {
                blockList.add(Pair(0,-1))
                blockList.add(Pair(0,0))
                blockList.add(Pair(0,1))
                blockList.add(Pair(0,2))
            }
            1 -> {
                blockList.add(Pair(-1,1))
                blockList.add(Pair(0,1))
                blockList.add(Pair(1,1))
                blockList.add(Pair(2,1))
            }
            2 -> {
                blockList.add(Pair(1,-1))
                blockList.add(Pair(1,0))
                blockList.add(Pair(1,1))
                blockList.add(Pair(1,2))
            }
            3 -> {
                blockList.add(Pair(-1,0))
                blockList.add(Pair(0,0))
                blockList.add(Pair(1,0))
                blockList.add(Pair(2,0))
            }
            else -> {
            }
        }
        return blockList
    }

    private fun getBlockO(rotation: Int): ArrayList<Pair<Int,Int>> {
        val blockList = ArrayList<Pair<Int, Int>>()
        when(rotation) {
            0 -> {
                blockList.add(Pair(0,0))
                blockList.add(Pair(1,0))
                blockList.add(Pair(0,1))
                blockList.add(Pair(1,1))
            }

            1 -> {
                blockList.add(Pair(0,0))
                blockList.add(Pair(1,0))
                blockList.add(Pair(0,1))
                blockList.add(Pair(1,1))
            }
            2 -> {
                blockList.add(Pair(0,0))
                blockList.add(Pair(1,0))
                blockList.add(Pair(0,1))
                blockList.add(Pair(1,1))
            }
            3 -> {
                blockList.add(Pair(0,0))
                blockList.add(Pair(1,0))
                blockList.add(Pair(0,1))
                blockList.add(Pair(1,1))
            }
            else -> {
            }
        }
        return blockList
    }

    private fun getBlockT(rotation: Int): ArrayList<Pair<Int,Int>> {
        val blockList = ArrayList<Pair<Int, Int>>()
        when(rotation) {
            0 -> {
                blockList.add(Pair(-1,0))
                blockList.add(Pair(0,-1))
                blockList.add(Pair(0,0))
                blockList.add(Pair(0,1))
            }

            1 -> {
                blockList.add(Pair(-1,0))
                blockList.add(Pair(0,0))
                blockList.add(Pair(0,1))
                blockList.add(Pair(1,0))
            }
            2 -> {
                blockList.add(Pair(0,-1))
                blockList.add(Pair(0,0))
                blockList.add(Pair(0,1))
                blockList.add(Pair(1,0))
            }
            3 -> {
                blockList.add(Pair(-1,0))
                blockList.add(Pair(0,-1))
                blockList.add(Pair(0,0))
                blockList.add(Pair(1,0))
            }
            else -> {
            }
        }
        return blockList
    }

    private fun getBlockS(rotation: Int): ArrayList<Pair<Int,Int>> {
        val blockList = ArrayList<Pair<Int, Int>>()
        when(rotation) {
            0 -> {
                blockList.add(Pair(-1,0))
                blockList.add(Pair(-1,1))
                blockList.add(Pair(0,-1))
                blockList.add(Pair(0,0))
            }

            1 -> {
                blockList.add(Pair(-1,0))
                blockList.add(Pair(0,0))
                blockList.add(Pair(0,1))
                blockList.add(Pair(1,1))
            }
            2 -> {
                blockList.add(Pair(0,0))
                blockList.add(Pair(0,1))
                blockList.add(Pair(1,-1))
                blockList.add(Pair(1,0))
            }
            3 -> {
                blockList.add(Pair(-1,-1))
                blockList.add(Pair(0,-1))
                blockList.add(Pair(0,0))
                blockList.add(Pair(1,0))
            }
            else -> {
            }
        }
        return blockList
    }

    private fun getBlockZ(rotation: Int): ArrayList<Pair<Int,Int>> {
        val blockList = ArrayList<Pair<Int, Int>>()
        when(rotation) {
            0 -> {
                blockList.add(Pair(-1,-1))
                blockList.add(Pair(-1,0))
                blockList.add(Pair(0,0))
                blockList.add(Pair(0,1))
            }

            1 -> {
                blockList.add(Pair(-1,1))
                blockList.add(Pair(0,0))
                blockList.add(Pair(0,1))
                blockList.add(Pair(1,0))
            }
            2 -> {
                blockList.add(Pair(0,-1))
                blockList.add(Pair(0,0))
                blockList.add(Pair(1,0))
                blockList.add(Pair(1,1))
            }
            3 -> {
                blockList.add(Pair(-1,0))
                blockList.add(Pair(0,-1))
                blockList.add(Pair(0,0))
                blockList.add(Pair(1,-1))
            }
            else -> {
            }
        }
        return blockList
    }

    private fun getBlockJ(rotation: Int): ArrayList<Pair<Int,Int>> {
        val blockList = ArrayList<Pair<Int, Int>>()
        when(rotation) {
            0 -> {
                blockList.add(Pair(-1,-1))
                blockList.add(Pair(0,-1))
                blockList.add(Pair(0,0))
                blockList.add(Pair(0,1))
            }

            1 -> {
                blockList.add(Pair(-1,0))
                blockList.add(Pair(-1,1))
                blockList.add(Pair(0,0))
                blockList.add(Pair(1,0))
            }
            2 -> {
                blockList.add(Pair(0,-1))
                blockList.add(Pair(0,0))
                blockList.add(Pair(0,1))
                blockList.add(Pair(1,1))
            }
            3 -> {
                blockList.add(Pair(-1,0))
                blockList.add(Pair(0,0))
                blockList.add(Pair(1,-1))
                blockList.add(Pair(1,0))
            }
            else -> {
            }
        }
        return blockList
    }

    private fun getBlockL(rotation: Int): ArrayList<Pair<Int,Int>> {
        val blockList = ArrayList<Pair<Int, Int>>()
        when(rotation) {
            0 -> {
                blockList.add(Pair(-1,1))
                blockList.add(Pair(0,-1))
                blockList.add(Pair(0,0))
                blockList.add(Pair(0,1))
            }

            1 -> {
                blockList.add(Pair(-1,0))
                blockList.add(Pair(0,0))
                blockList.add(Pair(1,0))
                blockList.add(Pair(1,1))
            }
            2 -> {
                blockList.add(Pair(0,-1))
                blockList.add(Pair(0,0))
                blockList.add(Pair(0,1))
                blockList.add(Pair(1,-1))
            }
            3 -> {
                blockList.add(Pair(-1,-1))
                blockList.add(Pair(-1,0))
                blockList.add(Pair(0,0))
                blockList.add(Pair(1,0))
            }
            else -> {
            }
        }
        return blockList
    }
    private fun getBlockEMPTY(): ArrayList<Pair<Int,Int>>{
        return ArrayList()
    }
}