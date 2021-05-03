package com.example.musicplayerapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val CHANNEL_ID = "channel_1"
    private val notificationId = 101
    private var songs = mutableListOf<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        listsInit()

        rv_musicList.layoutManager = LinearLayoutManager(this)
        rv_musicList.adapter = RecyclerAdapter(songs)

        btn_notification.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val time = SimpleDateFormat("HH:mm").format(calendar.time)
                Toast.makeText(this, "Picked time: $time", Toast.LENGTH_LONG).show()
                val pickedTime = calendar.timeInMillis
                val currentTime = System.currentTimeMillis()
                val timeDiff = pickedTime - currentTime
                timer((timeDiff/1000).toInt())
            }
            TimePickerDialog(this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(
                Calendar.MINUTE), true).show()
        }
    }

    private fun listsInit() {
        val believer = Song("Believer", "Imagine Dragons", R.drawable.imaginedragonsicon, R.drawable.believercover, R.raw.believer, "believer.txt")
        val countingStars = Song("Counting Stars", "OneRepublic", R.drawable.onerepublicicon, R.drawable.countingstarscover, R.raw.countingstars,"countingStars.txt")
        val stayinAlive = Song("Stayin' Alive", "Bee Gees", R.drawable.beegeesicon, R.drawable.stayinalivecover, R.raw.stayinalive,"stayinAlive.txt")
        val stressedOut = Song("Stressed Out", "Twenty One Pilots", R.drawable.twentyonepilotsicon, R.drawable.stressedoutcover, R.raw.stressedout,"stressedout.txt")
        val imaman = Song("I'm A Man", "Black Strobe", R.drawable.blackstrobeicon, R.drawable.imamancover, R.raw.imaman,"imaman.txt")
        believer.prev = imaman
        believer.next = countingStars
        countingStars.prev = believer
        countingStars.next = stayinAlive
        stayinAlive.prev = countingStars
        stayinAlive.next = stressedOut
        stressedOut.prev = stayinAlive
        stressedOut.next = imaman
        imaman.prev = stressedOut
        imaman.next = believer
        songs.add(believer)
        songs.add(countingStars)
        songs.add(stayinAlive)
        songs.add(stressedOut)
        songs.add(imaman)
    }

    private fun createNotificationChannel() {
        val name = "Notification name"
        val descriptionText = "Notification description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun sendNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Time is up")
            .setContentText("Stop listening music!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

    private fun timer(time: Int) {
        object : CountDownTimer((time * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                sendNotification()
            }
        }.start()
    }
}