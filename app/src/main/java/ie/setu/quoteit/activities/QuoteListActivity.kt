package ie.setu.quoteit.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.setu.quoteit.R
import ie.setu.quoteit.databinding.ActivityQuoteListBinding
import ie.setu.quoteit.databinding.CardQuoteBinding
import ie.setu.quoteit.main.MainApp
import ie.setu.quoteit.models.QuoteModel

class QuoteListActivity : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityQuoteListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuoteListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = QuoteAdapter(app.quotes)
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

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.quotes.size)
            }
        }
}

class QuoteAdapter constructor(private var quotes: List<QuoteModel>) :
    RecyclerView.Adapter<QuoteAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardQuoteBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val quote = quotes[holder.adapterPosition]
        holder.bind(quote)
    }

    override fun getItemCount(): Int = quotes.size

    class MainHolder(private val binding : CardQuoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(quote: QuoteModel) {
            binding.quotation.text = quote.quotation
            binding.bookTitle.text = quote.bookTitle
        }
    }
}
