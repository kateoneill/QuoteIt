package ie.setu.quoteit.activities

import QuoteAdapter
import QuoteListener
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.quoteit.R
import ie.setu.quoteit.databinding.ActivityQuoteListBinding
import ie.setu.quoteit.main.MainApp
import ie.setu.quoteit.models.QuoteModel
import java.util.*
import androidx.appcompat.widget.SearchView
import ie.setu.quoteit.models.QuoteMemStore
import timber.log.Timber.i
import kotlin.collections.ArrayList

class QuoteListActivity : AppCompatActivity(), QuoteListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityQuoteListBinding
    private lateinit var searchView: SearchView

    private var quotes = ArrayList<QuoteModel>()
    private lateinit var quoteAdapter: QuoteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuoteListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        searchView = findViewById(R.id.searchView)

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = QuoteAdapter(app.quotes.findAll(),this)

        quoteAdapter = QuoteAdapter(quotes, this)
        addQuoteToList()
    }

    private fun addQuoteToList() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterQuotes(newText).toString()
                return true
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filterQuotes(search: String?) {
        if (search != null) {
            val filteredQuoteList: ArrayList<QuoteModel> = ArrayList()
            quotes = app.quotes.findAll() as ArrayList<QuoteModel>
            for (quote in quotes) {
                if (quote.quotation.lowercase(Locale.ROOT).contains(search.lowercase(Locale.getDefault()))) {
                    filteredQuoteList.add(quote)
                    i("This quote is in the quote system")
//                    println(quote)
                }
            }

            if (filteredQuoteList.isEmpty()) {
                i("There are no quotes")
                Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show()
            } else {
                i("There are quotes")
                quoteAdapter.setFilteredList(filteredQuoteList)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, QuoteActivity::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQuoteClick(quote: QuoteModel) {
        val launcherIntent = Intent(this, QuoteActivity::class.java)
        launcherIntent.putExtra("quote_edit", quote)
        getClickResult.launch(launcherIntent)
    }


    @SuppressLint("NotifyDataSetChanged")
    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                        notifyDataSetChanged()
//                notifyItemRangeChanged(0,app.quotes.findAll().size)
            }
        }


    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.quotes.findAll().size)
            }
        }
}

