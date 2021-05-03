package com.example.musicplayerapp

import java.io.Serializable

class Song(
    var title: String,
    var performer: String,
    var icon: Int,
    var cover: Int,
    var mp3: Int,
    var lyrics: String
) : Serializable {
    var next: Song? = null
    var prev: Song? = null
}