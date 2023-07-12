package com.example.madcamp_week2.util

import io.socket.client.IO
import java.net.URISyntaxException
import io.socket.client.Socket

class SocketCompanion {
    companion object {
        private lateinit var socket : Socket
        fun get(): Socket {
            try {
                socket = IO.socket("http://34.64.184.111:3030")
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
            return socket
        }
    }
}