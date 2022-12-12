package ru.aiwprton.CustomPushClient

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.PersistableBundle
import android.widget.Toast
import org.json.JSONArray


class CustomLibActivity : Activity() {
    private var ID: String = ""// "-NB4lppuauJJF4IKL92uDqBSz3NTm1LF"
    private var mBounded = false
    private var ms: MessagingService? = null
    private var Availability: String = "0"
    private var Token: String = ""
    private var Messages: JSONArray = JSONArray()

    var mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            Toast.makeText(CustomLibActivity(), "Service is disconnected", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder) {
            Toast.makeText(CustomLibActivity(), "Service is connected", Toast.LENGTH_SHORT).show()
            mBounded = true
            val mLocalBinder = service as MessagingService.LocalBinder
            ms = mLocalBinder.serverInstance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        ID = intent.getStringExtra("id").toString()
        InitClient(ID)
    }

    override fun onStart() {
        super.onStart()
        val mIntent = Intent(this, MessagingService::class.java)
        bindService(mIntent, mConnection, BIND_AUTO_CREATE)
    }

    fun Start(id: String? = "") {
        if (id != null) {
            InsertId(id)
        }
        if (ID != "") {
            ms?.InitPushClient(ReturnId())
        }
    }

    fun ReturnId(): String {
        return ID
    }

    fun ReturnAvailability(): String {
        return Availability
    }

    fun ReturnMessages(): String {
        return Messages.toString()
    }

    fun InsertId(id: String) {
        ID = id
    }

    fun RefreshAvailability() {
        Availability = ms?.ReturnAvailability().toString()
        ms?.CheckAvailability()
    }

    fun RefreshToken() {
        ms?.RefreshToken()
        Token = ms?.clientToken.toString()
    }

    fun LastMessage() {
        Messages = JSONArray(ms?.messages)
    }

    fun InitClient(id: String?) {
        if (id != null && id != "") {
            InsertId(id)
        }
        ms?.InitPushClient(ID)
    }
}

// ./gradlew createJar
// D:\RuStorePushClientModule\mylibrary\libs
// C:\Users\User\source\repos\PushClientByJava\PushClientByJava\bin\Debug
// CustomModules
