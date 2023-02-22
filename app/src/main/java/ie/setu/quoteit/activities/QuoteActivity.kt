package ie.setu.quoteit.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.material.snackbar.Snackbar
import ie.setu.quoteit.R
import ie.setu.quoteit.databinding.ActivityQuoteitBinding
import ie.setu.quoteit.main.MainApp
import ie.setu.quoteit.models.QuoteModel
import timber.log.Timber.i

class QuoteActivity : AppCompatActivity() {
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
            quote = intent.extras?.getParcelable("quote_edit")!!
            binding.quotation.setText(quote.quotation)
            binding.bookTitle.setText(quote.bookTitle)
        }

        binding.btnAdd.setOnClickListener() {
            quote.quotation = binding.quotation.text.toString()
            quote.bookTitle = binding.bookTitle.text.toString()
            quote.pageNumber = binding.pageNumber.value
            quote.quoteTheme = binding.spinnerThemes.selectedItem.toString()
            if (quote.quotation.isNotEmpty()) {
                app.quotes.create(quote.copy())
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar.make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        setUpNumberPicker()
        spinnerSetup()
    }

    private fun spinnerSetup() {
        val quoteThemes = resources.getStringArray(R.array.themes)

        val themesDropdown = findViewById<Spinner>(R.id.spinnerThemes)
        if(themesDropdown != null) {
            val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, quoteThemes)
            themesDropdown.adapter = adapter

//            themesDropdown.onItemSelectedListener = object :
//                AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(parent: AdapterView<*>,
//                                            view: View, position: Int, id: Long) {
////                    Toast.makeText(this@MainActivity,
////                        getString(R.string.selected_item) + " " +
////                                "" + languages[position], Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>) {
//                    // write code to perform some action
//                }
//            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_quote, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> { finish() }
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




