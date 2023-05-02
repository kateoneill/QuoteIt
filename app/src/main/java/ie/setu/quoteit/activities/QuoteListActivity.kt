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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ie.setu.quoteit.models.QuoteMemStore
import ie.setu.quoteit.activities.QuoteMapsActivity
import timber.log.Timber.i
import kotlin.collections.ArrayList

class QuoteListActivity : AppCompatActivity(), QuoteListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityQuoteListBinding
    private lateinit var searchView: SearchView

    private var quotes = ArrayList<QuoteModel>()
    private lateinit var quoteAdapter: QuoteAdapter
    private lateinit var auth: FirebaseAuth


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

        quoteAdapter = QuoteAdapter(quotes, this)
        auth = Firebase.auth
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return super.onCreateOptionsMenu(menu)

        // below line is to get our inflater
        val inflater = menuInflater

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.menu_main, menu)

        //search

        val searchItem: MenuItem = menu.findItem(R.id.searchQuotes)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                i("hgcgjnch")
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                searchQuotes(msg)
                return false
            }
        })
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchQuotes(search: String) {
        i(search)
        if (search != null) {
            val filterList: ArrayList<QuoteModel> = ArrayList()
            quotes = app.quotes.findAll() as ArrayList<QuoteModel>
            for (quote in quotes) {
                if (quote.quotation.lowercase(Locale.ROOT).contains(search.lowercase(Locale.getDefault()))) {
                    filterList.add(quote)
                    i("This quote is in the quote system")
//                    println(quote)
                }
            }

            if (filterList.isEmpty()) {
                i("There are no quotes")
                Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show()
            } else {
                i("There are quotes")
                binding.recyclerView.adapter = QuoteAdapter(filterList, this@QuoteListActivity)
                quoteAdapter.notifyDataSetChanged()
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, QuoteActivity::class.java)
                getResult.launch(launcherIntent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.item_map -> {
                val launcherIntent = Intent(this, QuoteMapsActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.item_logout -> {
                i("Logout")
                auth.signOut()
                val logoutIntent = Intent(this, SignInActivity::class.java)
                logoutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(logoutIntent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQuoteClick(quote: QuoteModel) {
        val launcherIntent = Intent(this, QuoteActivity::class.java)
        launcherIntent.putExtra("quote_edit", quote)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        getClickResult.launch(launcherIntent)
    }

    private val mapIntentLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )    { }



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

