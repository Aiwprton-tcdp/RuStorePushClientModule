package ru.aiwprton.CustomPushClient

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.vk.push.common.messaging.RemoteMessage
import ru.rustore.sdk.core.feature.model.FeatureAvailabilityResult
import ru.rustore.sdk.core.tasks.OnCompleteListener
import ru.rustore.sdk.pushclient.RuStorePushClient
import ru.rustore.sdk.pushclient.common.logger.DefaultLogger
import ru.rustore.sdk.pushclient.messaging.exception.RuStorePushClientException
import ru.rustore.sdk.pushclient.messaging.service.RuStoreMessagingService
import ru.rustore.sdk.pushclient.utils.resolveForPush


class MessagingService: RuStoreMessagingService() {
    var clientToken: String = ""
    var isAvailable: Boolean = true
    var messages: List<RemoteMessage> = emptyList()
    var FirstMessage = String()

    var mBinder: IBinder = LocalBinder()

    fun Init(intent: Intent): IBinder {
        return mBinder;
    }

    class LocalBinder : Binder() {
        val serverInstance: MessagingService
            get() = MessagingService()
    }

    override fun onNewToken(token: String) {
        clientToken = token
    }

    override fun onMessageReceived(message: ru.rustore.sdk.pushclient.messaging.model.RemoteMessage) {
        messages.plus(message)
    }

    override fun onDeletedMessages() {
    }

    override fun onError(errors: List<RuStorePushClientException>) {
    }

    fun InitPushClient(id: String) {
        RuStorePushClient.init(application, id, DefaultLogger())
        CheckAvailability()
    }

    fun RefreshToken() {
        RuStorePushClient.getToken().addOnCompleteListener(object : OnCompleteListener<String?> {
            override fun onFailure(throwable: Throwable) {
                clientToken = ""
            }

            override fun onSuccess(result: String?) {
                if (result != null) {
                    clientToken = result
                }
            }
        })
    }

    fun CheckAvailability() {
        RuStorePushClient.checkPushAvailability(application.applicationContext)
            .addOnCompleteListener(object : OnCompleteListener<FeatureAvailabilityResult> {
                override fun onSuccess(result: FeatureAvailabilityResult) {
                    when (result) {
                        FeatureAvailabilityResult.Available -> {
                            isAvailable = true
                        }

                        is FeatureAvailabilityResult.Unavailable -> {
                            isAvailable = true
                            result.cause.resolveForPush(application.applicationContext)
                        }
                    }
                }

                override fun onFailure(throwable: Throwable) {
                    isAvailable = true
                }
            })
    }

    fun ReturnAvailability() : Boolean {
        return isAvailable
    }
}
