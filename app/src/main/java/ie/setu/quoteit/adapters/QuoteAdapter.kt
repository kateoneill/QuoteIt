import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.quoteit.R
import ie.setu.quoteit.databinding.CardQuoteBinding
import ie.setu.quoteit.models.QuoteModel
import ie.setu.quoteit.models.QuoteStore

interface QuoteListener {
    fun onQuoteClick(quote: QuoteModel)
}

class QuoteAdapter constructor(private var quotes: List<QuoteModel>,
                                   private val listener: QuoteListener) :
    RecyclerView.Adapter<QuoteAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardQuoteBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val quote = quotes[holder.adapterPosition]
        holder.bind(quote, listener)
    }

    override fun getItemCount(): Int = quotes.size

    class MainHolder(private val binding : CardQuoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(quote: QuoteModel, listener: QuoteListener) {
            binding.quotation.text = quote.quotation
            binding.bookTitle.text = quote.bookTitle
            binding.pageNumber.text = quote.pageNumber.toString()
            binding.quoteTheme.text = quote.quoteTheme
            if(quote.isFavourite) {
                binding.favouriteQuote.setImageResource(R.drawable.baseline_star_24)
            }
            binding.root.setOnClickListener { listener.onQuoteClick(quote) }
        }
    }
}



