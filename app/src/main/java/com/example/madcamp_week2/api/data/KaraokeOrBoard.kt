package com.example.madcamp_week2.api.data

import com.example.madcamp_week2.api.data.board.Board
import com.example.madcamp_week2.api.data.karaoke.Karaoke
import com.example.madcamp_week2.api.data.user.User

data class KaraokeOrBoard(
    val karaoke: Karaoke?,
    val board: Board?
)
