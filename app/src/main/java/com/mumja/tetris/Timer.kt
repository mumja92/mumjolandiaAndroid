package com.mumja.tetris

class Timer(
    var periodMs: Int,
    var refreshMs: Long = 100,
    ) {
    private var startTime: Long = System.currentTimeMillis()
    fun handleTime() : Boolean{
        Thread.sleep(refreshMs)
        val currentTime = System.currentTimeMillis()
        if (currentTime - startTime > periodMs){
            startTime = System.currentTimeMillis()
            return true
        }
        return false
    }
}