package com.darekbx.nightcamera.commands

class RotateLeft : Caller() {

    suspend operator fun invoke(): Int {
        return executeCommand("rotate_left")?.status?.value ?: -1
    }
}