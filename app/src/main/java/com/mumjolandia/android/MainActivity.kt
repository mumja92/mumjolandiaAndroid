package com.mumjolandia.android

import android.app.Notification
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import android.app.NotificationManager
import android.content.Intent
import android.app.NotificationChannel
import android.app.PendingIntent
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import com.android.R


class MainActivity : AppCompatActivity() {
    private var mAppBarConfiguration: AppBarConfiguration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_planner,
            R.id.nav_home,
            R.id.nav_remote_controller,
            R.id.nav_tetris,
            R.id.nav_settings
        )
                .setDrawerLayout(drawer)
                .build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration!!)
        NavigationUI.setupWithNavController(navigationView, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration!!)
                || super.onSupportNavigateUp())
    }

    override fun onDestroy() {
        super.onDestroy()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    fun showNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notifications = notificationManager.activeNotifications
        if (notifications.size == 0) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            val channelId = "MUMJOLANDIA_CHANNEL"
            val notificationChannel = NotificationChannel(channelId, "mumjolandia", NotificationManager.IMPORTANCE_DEFAULT)
            val pendingIntent = PendingIntent.getActivity(applicationContext, 1, intent, 0)
            val notification = Notification.Builder(applicationContext, channelId)
                .setContentText("mumjolandia")
                .setContentIntent(pendingIntent)
                .setChannelId(channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .build()
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.notify(1, notification)
        }
    }
}