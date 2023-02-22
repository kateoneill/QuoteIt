package ie.setu.quoteit.models

interface QuoteStore {
    fun findAll(): List<QuoteModel>
    fun create(quote: QuoteModel)
    fun update(quote: QuoteModel)
}