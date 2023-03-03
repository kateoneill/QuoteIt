package ie.setu.quoteit.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class QuoteMemStore : QuoteStore {

    private val quotes = ArrayList<QuoteModel>()

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
            foundQuote.pageNumber = quote.pageNumber
            foundQuote.quoteTheme = quote.quoteTheme
            foundQuote.isFavourite = quote.isFavourite
            logAll()
        }
    }

    override fun delete(quote: QuoteModel) {
        quotes.remove(quote)
        i("quote is deleted!")
    }

    private fun logAll() {
        quotes.forEach { i("$it") }
    }
}