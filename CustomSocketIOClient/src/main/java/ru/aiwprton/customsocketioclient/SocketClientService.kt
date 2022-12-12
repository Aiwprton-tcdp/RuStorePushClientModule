package ru.aiwprton.customsocketioclient

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter;
import org.json.JSONException
import org.json.JSONObject

class SocketClientService : Service() {
    private var mSocket: Socket = IO.socket("https://xn--j1ab.xn--d1aadjija0bkk2khl.xn--p1ai:1009")
    private var Messages: List<JSONObject> = emptyList()

    var mBinder: IBinder = LocalBinder()


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    fun Init(intent: Intent): IBinder {
        return mBinder;
    }

    class LocalBinder : Binder() {
        val serverInstance: SocketClientService
            get() = SocketClientService()
    }

    fun Start() {
//        try {
//            mSocket = IO.socket("http://chat.socket.io")
//        } catch (e: URISyntaxException) {
//        }

        mSocket.connect()
    }

    fun SelectNewMessage() {
        Emitter.Listener { args ->
//            SocketClientActivity.runOnUiThread(Runnable() {
            val data: JSONObject = args[0] as JSONObject
            val username: String
            val message: String

            try {
                username = data.getString("username")
                message = data.getString("message")
            } catch (e: JSONException) {
//                    return e.message
            }

            Messages.plus(data)
            // add the message to view
//                addMessage(username, message)
//            })
        }
    }

    fun ReturnConnected() : Boolean {
        return mSocket.connected()
    }

    fun ReturnMessages() : List<JSONObject> {
        return Messages
    }

}
