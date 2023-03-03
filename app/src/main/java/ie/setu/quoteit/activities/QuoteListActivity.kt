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
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.quoteit.R
import ie.setu.quoteit.databinding.ActivityQuoteListBinding
import ie.setu.quoteit.databinding.CardQuoteBinding
import ie.setu.quoteit.main.MainApp
import ie.setu.quoteit.models.QuoteModel
import ie.setu.quoteit.models.QuoteStore
import java.util.*
import kotlin.collections.ArrayList
import androidx.appcompat.widget.SearchView
import java.util.Locale.filter

class QuoteListActivity : AppCompatActivity(), QuoteListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityQuoteListBinding
    lateinit var searchView: SearchView
    var quoteAdapter: QuoteAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuoteListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = QuoteAdapter(app.quotes.findAll(),this)
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

