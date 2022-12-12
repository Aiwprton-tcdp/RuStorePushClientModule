package ru.aiwprton.customcentrifugeclient

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.PersistableBundle
import android.widget.Toast
import io.github.centrifugal.centrifuge.*


class CustomCentrifugeActivity : Activity() {
    private var scs: CentrifugeClientService = CentrifugeClientService()
//    private var mBounded = false

    var Token = "0"
    var Uri = "0"
    var ChannelName = "0"

    private var client: Client? = null
    private var activityPaused = false
    var IsConnected = false
    var IsConnecting = false
    var IsDisconnected = false
    var IsErrored = false
    var IsSubscribed = false
    var IsPublicated = false
    var IsSubSubscribing = false
    var IsSubPublicated = false
    var IsSubErrored = false
    var IsSubSubscribed = false
    var IsSubUnsubscribed = false
    val Errors = mutableListOf<String>()
    val SubErrors = mutableListOf<String>()

//    var mConnection: ServiceConnection = object : ServiceConnection {
//        override fun onServiceDisconnected(name: ComponentName?) {
//            Toast.makeText(CustomCentrifugeActivity(), "Service is disconnected", Toast.LENGTH_SHORT).show()
//        }
//
//        override fun onServiceConnected(name: ComponentName?, service: IBinder) {
//            Toast.makeText(CustomCentrifugeActivity(), "Service is connected", Toast.LENGTH_SHORT).show()
//            mBounded = true
//            val mLocalBinder = service as CentrifugeClientService.LocalBinder
//            scs = mLocalBinder.serverInstance
//            scs!!.Start()
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onCreate(savedInstanceState, persistentState)
////        setContentView(R.layout.activity_custom_centrifuge)
////        Start()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        val mIntent = Intent(this, CentrifugeClientService::class.java)
//        bindService(mIntent, mConnection, BIND_AUTO_CREATE)
////        Start()
//    }

    fun Start() {
        scs = CentrifugeClientService()
        try {
            scs.Start()
        } catch (e: NoClassDefFoundError) {
            e.message?.let { Errors.add(it) }
        }
    }

    fun ReturnErrors() {
        IsConnected = scs.ReturnConnected()
        IsConnecting = scs.ReturnConnecting()
        IsDisconnected = scs.ReturnDisconnected()
        IsErrored = scs.ReturnErrored()
        Errors.addAll(scs.ReturnErrors())
        SubErrors.addAll(scs.ReturnSubErrors())
    }

    fun ReturnInputs() {
        Token = scs.Token
        Uri = scs.Uri
        ChannelName = scs.ChannelName
    }


    override fun onPause() {
        super.onPause()
        activityPaused = true
        scs.Disconnect()
        Toast.makeText(this, "Paused", Toast.LENGTH_SHORT).show()
    }

    protected override fun onResume() {
        super.onResume()
        if (activityPaused) {
            scs.Resume()
            Toast.makeText(this, "Resumed", Toast.LENGTH_SHORT).show()
            activityPaused = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scs.Destroy()
        Toast.makeText(this, "Destroyed", Toast.LENGTH_SHORT).show()
    }
}