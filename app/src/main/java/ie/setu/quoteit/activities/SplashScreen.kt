package ie.setu.quoteit.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import ie.setu.quoteit.R
import timber.log.Timber.i



@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {

    companion object {
        const val ANIMATION_TIME: Long = 1700
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler(this.mainLooper).postDelayed({
            startActivity(Intent(this, QuoteListActivity::class.java))
            finish()

        }, ANIMATION_TIME)

    }

}