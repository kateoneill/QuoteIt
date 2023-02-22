package ie.setu.quoteit.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class QuoteMemStore : QuoteStore {

    val quotes = ArrayList<QuoteModel>()

    override fun findAll(): List<QuoteModel> {
        return quotes
    }

    override fun create(quote: QuoteModel) {
        quote.id = getId()
        quotes.add(quote)
        logAll()
    }

    override fun update(quote: QuoteModel) {
        var foundQuote: QuoteModel? = quotes.find { p -> p.id == quote.id }
        if (foundQuote != null) {
            foundQuote.quotation = quote.quotation
            foundQuote.bookTitle = quote.bookTitle
            logAll()
        }
    }

    private fun logAll() {
        quotes.forEach { i("$it") }
    }
}