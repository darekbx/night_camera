package com.darekbx.nightcamera.commands

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

abstract class Caller {
    private val client = HttpClient()

    protected suspend fun executeCommand(command: String): HttpResponse? {
        val url = "$SERVER_ADDRESS$command"
        try {
            val response = client.get(url)
            return response
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return null
        }
    }

    companion object {
        val SERVER_ADDRESS = "http://raspberrypi.local:8081/"
    }
}