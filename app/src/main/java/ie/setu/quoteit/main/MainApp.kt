package ie.setu.quoteit.main

import android.app.Application
import ie.setu.quoteit.models.QuoteJSONStore
import ie.setu.quoteit.models.QuoteMemStore
import ie.setu.quoteit.models.QuoteStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var quotes: QuoteStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        quotes = QuoteJSONStore(applicationContext)
        i("QuoteIt started")
    }
}
