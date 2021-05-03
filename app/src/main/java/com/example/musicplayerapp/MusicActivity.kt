package com.example.musicplayerapp

import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_lyrics.*
import kotlinx.android.synthetic.main.activity_music.*
import java.text.SimpleDateFormat
import java.util.*


class MusicActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val song = intent.getSerializableExtra("song") as Song
        val isAutoPlay = intent.getBooleanExtra("isAutoPlay", false)
        mediaPlayer = MediaPlayer.create(this, song.mp3)
        if (isAutoPlay) {
            mediaPlayer.start()
            btn_play.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
        }
        iv_activity_cover.setImageResource(song.cover)
        tv_activity_title.text = song.title
        tv_activity_performer.text = song.performer
        tv_duration.text = "${mediaPlayer.duration/1000/60}:${mediaPlayer.duration/1000%60}"
        seekBar.progress = 0
        seekBar.max = mediaPlayer.duration

        btn_play.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                btn_play.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
            } else {
                mediaPlayer.pause()
                btn_play.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)
            }
        }

        btn_next.setOnClickListener {
            if (song.next != null) {
                if (mediaPlayer.isPlaying) mediaPlayer.stop()
                val intent = Intent(this, MusicActivity::class.java).apply {
                    putExtra("song", song.next)
                }
                startActivity(intent)
                finish()
            }
        }

        btn_prev.setOnClickListener {
            if (song.prev != null) {
                if (mediaPlayer.isPlaying) mediaPlayer.stop()
                val intent = Intent(this, MusicActivity::class.java).apply {
                    putExtra("song", song.prev)
                }
                startActivity(intent)
                finish()
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, changed: Boolean) {
                if (changed) {
                    mediaPlayer.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        mediaPlayer.setOnCompletionListener {
            if (song.next != null) {
                if (mediaPlayer.isPlaying) mediaPlayer.stop()
                val intent = Intent(this, MusicActivity::class.java).apply {
                    putExtra("song", song.next)
                    putExtra("isAutoPlay", true)
                }
                startActivity(intent)
                finish()
            }
        }

        btn_lyrics.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.activity_lyrics)
            dialog.tv_lyrics_title.text = song.title
            dialog.tv_lyrics_text.text = application.assets.open(song.lyrics).bufferedReader().use{ it.readText() }
            dialog.show()
        }

        Thread(Runnable {
            while (mediaPlayer != null) {
                if (mediaPlayer.isPlaying) {
                    try {
                        runOnUiThread {
                            val min = mediaPlayer.currentPosition/1000/60
                            val sec = mediaPlayer.currentPosition/1000%60
                            var current = "$min:"
                            if (sec < 10) current += "0"
                            current += sec
                            tv_current_playtime.text = current
                            seekBar.progress = mediaPlayer.currentPosition
                        }
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {}
                }
            }
        }).start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (mediaPlayer.isPlaying) mediaPlayer.stop()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


}