package ie.setu.quoteit.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import ie.setu.quoteit.databinding.ActivityQuoteitBinding
import ie.setu.quoteit.models.QuoteModel
import timber.log.Timber
import timber.log.Timber.i

class QuoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuoteitBinding
    var quote = QuoteModel()
    val quotes = ArrayList<QuoteModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuoteitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())

        i("QuoteIt Activity started...")

        binding.btnAdd.setOnClickListener() {
            quote.quotation = binding.quotation.text.toString()
            quote.bookTitle = binding.bookTitle.text.toString()
            if (quote.quotation.isNotEmpty()) {
                i("add Button Pressed: $quote.title \n $quote.desc")
                quotes.add(quote.copy())
                i("Quotes: \t $quotes")
            }
            else {
                Snackbar
                    .make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}

