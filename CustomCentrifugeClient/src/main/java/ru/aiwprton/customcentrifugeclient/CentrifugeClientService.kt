package ru.aiwprton.customcentrifugeclient

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import io.github.centrifugal.centrifuge.*


class CentrifugeClientService : Service() {
    private var client: Client? = null
    private val activityPaused = false

    var Token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMzMwIn0.b4DAobiD9SGIf28zViXWpv1nffWGwBta1zSJbt-sFJA"
    var Uri = "wss://xn--j1ab.xn--d1aadjija0bkk2khl.xn--p1ai:1010/connection/websocket"
    var ChannelName = "#chat.lk_chat_fe51510c80bfd6e5d78a164cd5b1f6882a38a4a9316c49e5a833517c45d31070"

    var IsConnected = false
    var IsConnecting = false
    var IsDisconnected = true
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

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    fun Start() {
        val listener = object : EventListener() {
            override fun onConnecting(client: Client?, event: ConnectingEvent) {
//                Thread {
////                    tv.setText(String.format("Connecting: %s", event.reason))
//                    IsConnecting = true
//                }.start()
                IsConnecting = true
            }

            override fun onConnected(client: Client?, event: ConnectedEvent) {
                IsConnecting = false
                IsConnected = true
            }

            override fun onDisconnected(client: Client?, event: DisconnectedEvent) {
                IsDisconnected = true
            }

            override fun onError(client: Client?, event: ErrorEvent) {
                IsErrored = true
                event.error.message?.let { Errors.add(it) }
            }

            override fun onSubscribed(client: Client?, event: ServerSubscribedEvent) {
                IsSubscribed = true
            }

            override fun onPublication(client: Client?, event: ServerPublicationEvent) {
                IsPublicated = true
            }
        }

        val subListener = object : SubscriptionEventListener() {
            override fun onSubscribing(sub: Subscription, event: SubscribingEvent) {
                IsSubSubscribing = true
            }

            override fun onSubscribed(sub: Subscription, event: SubscribedEvent) {
                IsSubSubscribed = true
            }

            override fun onUnsubscribed(sub: Subscription, event: UnsubscribedEvent) {
                IsSubUnsubscribed = true
            }

            override fun onError(sub: Subscription, event: SubscriptionErrorEvent) {
                IsSubErrored = true
                event.error.message?.let { SubErrors.add(it) }
            }

            override fun onPublication(sub: Subscription, event: PublicationEvent) {
                IsSubPublicated = true
            }
        }

        val opts = Options()
        opts.token = Token

        client = Client(Uri, opts, listener)
        try {
            client?.connect()
        } catch (ex: Exception) {
            ex.message?.let { Errors.add(it) }
            return
        }

        val sub: Subscription?
        try {
            sub = client?.newSubscription(ChannelName, subListener)
        } catch (ex: Exception) {
            ex.message?.let { SubErrors.add(it) }
            return
        }
        if (sub != null) {
            sub.subscribe()
        }
    }

    fun ReturnConnected() : Boolean {
        return IsConnected
    }

    fun ReturnConnecting() : Boolean {
        return IsConnecting
    }

    fun ReturnDisconnected() : Boolean {
        return IsDisconnected
    }

    fun ReturnErrored() : Boolean {
        return IsErrored
    }

    fun ReturnErrors() : List<String> {
        return Errors
    }

    fun ReturnSubErrors() : List<String> {
        return SubErrors
    }

    fun Disconnect() {
        client?.disconnect()
    }

    fun Resume() {
        client?.connect()
    }

    fun Destroy() {
        try {
            client?.close(5000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}