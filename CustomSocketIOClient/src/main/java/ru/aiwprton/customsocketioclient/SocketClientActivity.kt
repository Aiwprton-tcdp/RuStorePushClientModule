package ru.aiwprton.customsocketioclient

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.PersistableBundle
import android.widget.Toast
import org.json.JSONObject

class SocketClientActivity : Activity() {
    private var scs: SocketClientService? = null
    private var mBounded = false

    var mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            Toast.makeText(SocketClientActivity(), "Service is disconnected", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder) {
            Toast.makeText(SocketClientActivity(), "Service is connected", Toast.LENGTH_SHORT).show()
            mBounded = true
            val mLocalBinder = service as SocketClientService.LocalBinder
            scs = mLocalBinder.serverInstance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_socket_client)
    }

    override fun onStart() {
        super.onStart()
        val mIntent = Intent(this, SocketClientService::class.java)
        bindService(mIntent, mConnection, BIND_AUTO_CREATE)
    }

    fun Start() {
        scs?.Start()
    }

    fun IsConnected() : Boolean? {
//        if (scs == null) return false

        return scs?.ReturnConnected()
    }

    fun SelectNewMessage() {
        scs?.SelectNewMessage()
    }

    fun ReturnMessages() : List<JSONObject>? {
        return scs?.ReturnMessages()
    }
}

// https://socket.io/blog/native-socket-io-and-android/
// D:\RuStorePushClientModule\CustomSocketIOClient\libs