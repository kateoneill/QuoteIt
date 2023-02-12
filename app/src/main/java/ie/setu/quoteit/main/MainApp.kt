package ie.setu.quoteit.main

import android.app.Application
import ie.setu.quoteit.models.QuoteModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    val quotes = ArrayList<QuoteModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Quote it started")
    }
}

