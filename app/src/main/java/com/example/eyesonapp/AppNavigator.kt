package com.example.eyesonapp

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import com.example.eyesonapp.ui.calls.CallsActivity
import java.lang.ref.WeakReference

interface AppNavigator {
    fun navigate(appDestination: AppDestination)
}

class AppNavigatorImpl(private val appActivityManager: AppActivityManager) : AppNavigator {

    override fun navigate(appDestination: AppDestination) {
        val activity = appActivityManager.getCurrentActivity() ?: return
        when (appDestination) {
            is AppDestination.JoinRoomAsHost -> {
                activity.startActivity(
                    Intent(activity, CallsActivity::class.java).putExtra(ARG_ROOM_ACCESS_KEY, appDestination.accessKey)
                )
            }
            is AppDestination.InviteLink -> {
                sendInviteLink(activity, appDestination.url)
            }
            is AppDestination.DefaultError -> {
                Toast.makeText(activity, DEBUG_ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
            }
            is AppDestination.ShowErrorMessageAndFinishFlow -> {
                AlertDialog.Builder(activity)
                    .setTitle(appDestination.title)
                    .setMessage(appDestination.message)
                    .setPositiveButton(android.R.string.ok) { _, _ -> activity.finish() }
                    .setOnDismissListener { activity.finish() }
                    .setCancelable(false)
                    .create()
                    .show()
            }
            is AppDestination.FinishCurrentActivity -> {
                activity.finish()
            }
        }
    }

    private fun sendInviteLink(activity: Activity, link: String) {
        if (link.contains(BuildConfig.EYESON_DEEP_LINK_URL)) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.home_screen_room_invite, link))
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            activity.startActivity(shareIntent)
        }
    }
}

sealed class AppDestination {
    data class JoinRoomAsHost(val accessKey: String) : AppDestination()
    data class InviteLink(val url: String) : AppDestination()
    object FinishCurrentActivity : AppDestination()
    data class ShowErrorMessageAndFinishFlow(
        @StringRes val title: Int,
        @StringRes val message: Int,
    ) : AppDestination()

    object DefaultError : AppDestination()
}

const val ARG_ROOM_ACCESS_KEY = "ARG_ROOM_ACCESS_KEY"

interface AppActivityManager {
    fun getCurrentActivity(): Activity?
}

class AppActivityManagerImpl(applicationContext: Context) : AppActivityManager {

    private var activity: WeakReference<Activity?>? = null

    private val activityCallback by lazy {
        object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, instanceState: Bundle?) {
                this@AppActivityManagerImpl.activity = WeakReference(activity)
            }

            override fun onActivityStarted(activity: Activity) {
                this@AppActivityManagerImpl.activity = WeakReference(activity)
            }

            override fun onActivityResumed(activity: Activity) {
                this@AppActivityManagerImpl.activity = WeakReference(activity)
            }

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, instanceState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        }
    }

    init {
        if (applicationContext is Application) {
            applicationContext.unregisterActivityLifecycleCallbacks(activityCallback)
            applicationContext.registerActivityLifecycleCallbacks(activityCallback)
        }
    }

    override fun getCurrentActivity(): Activity? {
        return activity?.get()
    }
}

const val DEBUG_ERROR_MESSAGE = "Something went wrong"