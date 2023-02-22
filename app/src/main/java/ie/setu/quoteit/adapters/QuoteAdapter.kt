import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.setu.quoteit.databinding.CardQuoteBinding
import ie.setu.quoteit.models.QuoteModel

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
            binding.root.setOnClickListener { listener.onQuoteClick(quote) }
        }
    }
}
