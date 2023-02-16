package ie.setu.quoteit.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import ie.setu.quoteit.R
import ie.setu.quoteit.databinding.ActivityQuoteitBinding
import ie.setu.quoteit.main.MainApp
import ie.setu.quoteit.models.QuoteModel
import timber.log.Timber
import timber.log.Timber.i

class QuoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuoteitBinding
    var quote = QuoteModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuoteitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp
        i("Placemark Activity started...")
        binding.btnAdd.setOnClickListener() {
            quote.quotation = binding.quotation.text.toString()
            quote.bookTitle = binding.bookTitle.text.toString()
            quote.pageNumber = binding.pageNumber.value
            if (quote.quotation.isNotEmpty()) {
                app.quotes.add(quote.copy())
                i("add Button Pressed: ${quote}")
                for (i in app.quotes.indices)
                { i("Quote[$i]:${this.app.quotes[i]}") }
            }
            else {
                Snackbar.make(it,"Please Enter a quotation", Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        binding.btnAdd.setOnClickListener() {
            quote.quotation = binding.quotation.text.toString()
            quote.bookTitle = binding.bookTitle.text.toString()
            quote.pageNumber = binding.pageNumber.value
            if (quote.quotation.isNotEmpty()) {
                app.quotes.add(quote.copy())
                i("add Button Pressed: ${quote}")
                for (i in app.quotes.indices) {
                    i("Quote[$i]:${this.app.quotes[i]}")
                }
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar.make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        setUpNumberPicker()
    }

    private fun setUpNumberPicker() {
        val numberPicker = binding.pageNumber
        numberPicker.minValue = 0
        numberPicker.maxValue = 200
        numberPicker.wrapSelectorWheel = true
        }

}




