package com.darekbx.nightcamera.commands

class RotateRight : Caller() {

    suspend operator fun invoke(): Int {
        return executeCommand("rotate_right")?.status?.value ?: -1
    }
}