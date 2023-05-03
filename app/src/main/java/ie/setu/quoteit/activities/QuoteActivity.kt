package ie.setu.quoteit.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.quoteit.R
import ie.setu.quoteit.databinding.ActivityQuoteitBinding
import ie.setu.quoteit.helpers.showImagePicker
import ie.setu.quoteit.main.MainApp
import ie.setu.quoteit.models.Location
import ie.setu.quoteit.models.QuoteModel
import timber.log.Timber.i


class QuoteActivity : AppCompatActivity() {
    private var edit = false
    private lateinit var binding: ActivityQuoteitBinding
    var quote = QuoteModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var location = Location(52.245696, -7.139102, 15f)



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
            binding.pageNumber.value = quote.pageNumber
            binding.quoteTheme.setSelection(quote.quoteTheme.length)
            binding.hintFave.isChecked.apply { true }
            binding.btnAdd.setText(R.string.save_quote)
            Picasso.get()
                .load(quote.image)
                .into(binding.placemarkImage)
            if (quote.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_quote_image)
            }
        }

        binding.btnAdd.setOnClickListener {
            quote.quotation = binding.quotation.text.toString()
            quote.bookTitle = binding.bookTitle.text.toString()
            quote.pageNumber = binding.pageNumber.value
            quote.quoteTheme = binding.quoteTheme.selectedItem.toString()
            quote.isFavourite = binding.hintFave.isChecked.apply { true }

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
                }}
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher,this)
        }


        binding.placemarkLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (quote.zoom != 0f) {
                location.lat =  quote.lat
                location.lng = quote.lng
                location.zoom = quote.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }


        registerImagePickerCallback()
        setUpNumberPicker()
        spinnerSetup()
        registerMapCallback()
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            quote.lat = location.lat
                            quote.lng = location.lng
                            quote.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }


    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")

                            val image = result.data!!.data!!
                            contentResolver.takePersistableUriPermission(image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            quote.image = image

                            Picasso.get()
                                .load(quote.image)
                                .into(binding.placemarkImage)
                            binding.chooseImage.setText(R.string.change_quote_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
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
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.dialogTitle)
                    builder.setMessage(R.string.dialogMessage)
                    builder.setIcon(R.drawable.delete_alert)


                    builder.setPositiveButton("Yes"){ _, _ ->
                        Toast.makeText(applicationContext,"Quote Deleted",Toast.LENGTH_LONG).show()
                        i("Following quote has been deleted: $quote")
                        app.quotes.delete(quote)
                        setResult(RESULT_OK)
                        finish()
                    }
                    builder.setNeutralButton("Cancel"){ _, _ ->
                        Toast.makeText(applicationContext,"Maybe it was a mistake, maybe you changed your mind, this quote is happy regardless",Toast.LENGTH_LONG).show()
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
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




