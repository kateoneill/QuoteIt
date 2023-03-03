package ie.setu.quoteit.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ie.setu.quoteit.R
import ie.setu.quoteit.databinding.ActivityQuoteitBinding
import ie.setu.quoteit.main.MainApp
import ie.setu.quoteit.models.QuoteModel
import timber.log.Timber.i


class QuoteActivity : AppCompatActivity() {
    private var edit = false
    private lateinit var binding: ActivityQuoteitBinding
    var quote = QuoteModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuoteitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)


        app = application as MainApp
        i("Quoteit Activity started...")

        if (intent.hasExtra("quote_edit")) {
            edit = true
            quote = intent.extras?.getParcelable<QuoteModel>("quote_edit")!!
            binding.quotation.setText(quote.quotation)
            binding.bookTitle.setText(quote.bookTitle)
            binding.pageNumber.setValue(quote.pageNumber)
            binding.quoteTheme.setSelection(quote.quoteTheme.length)
            quote.isFavourite = if (!quote.isFavourite) {
                binding.hintFave.isChecked.apply { true }
            } else {
                binding.hintFave.isChecked.apply{false}
            }
            binding.btnAdd.setText(R.string.save_quote)
        }

        binding.btnAdd.setOnClickListener() {
            quote.quotation = binding.quotation.text.toString()
            quote.bookTitle = binding.bookTitle.text.toString()
            quote.pageNumber = binding.pageNumber.value
            quote.quoteTheme = binding.quoteTheme.selectedItem.toString()
            quote.isFavourite = if (!quote.isFavourite) {
                binding.hintFave.isChecked.apply { true }
            } else {
                binding.hintFave.isChecked.apply{false}
            }

            if (quote.quotation.isEmpty()) {
                Snackbar.make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
            else {
                if(edit) {
                    app.quotes.update(quote.copy())
                    setResult(RESULT_OK)
                    finish()
                } else {
                    app.quotes.create(quote.copy())
                    setResult(RESULT_OK)
                    finish()
                }

            }
        }

        setUpNumberPicker()
        spinnerSetup()
    }

    private fun spinnerSetup() {
        val quoteThemes = resources.getStringArray(R.array.themes)

        val themesDropdown = findViewById<Spinner>(R.id.quoteTheme)
        if(themesDropdown != null) {
            val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, quoteThemes)
            themesDropdown.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_quote, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> { finish() }

            R.id.item_delete -> {
                app.quotes.delete(quote)
                setResult(RESULT_OK)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpNumberPicker() {
        val numberPicker = binding.pageNumber
        numberPicker.minValue = 0
        numberPicker.maxValue = 200
        numberPicker.wrapSelectorWheel = true
    }
}




