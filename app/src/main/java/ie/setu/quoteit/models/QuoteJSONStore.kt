package ie.setu.quoteit.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.setu.quoteit.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "quotess.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<QuoteModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class QuoteJSONStore(private val context: Context) : QuoteStore {

    var quotes = mutableListOf<QuoteModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<QuoteModel> {
        logAll()
        return quotes
    }

    override fun create(quote: QuoteModel) {
        quote.id = generateRandomId()
        quotes.add(quote)
        serialize()
    }


    override fun update(quote: QuoteModel) {
        val quotesList = findAll() as ArrayList<QuoteModel>
        var foundQuote: QuoteModel? = quotes.find { p -> p.id == quote.id }
        if (foundQuote != null) {
            foundQuote.quotation = quote.quotation
            foundQuote.bookTitle = quote.bookTitle
            foundQuote.pageNumber = quote.pageNumber
            foundQuote.quoteTheme = quote.quoteTheme
            foundQuote.isFavourite = quote.isFavourite
            foundQuote.image = quote.image
            foundQuote.lat = quote.lat
            foundQuote.lng = quote.lng
            foundQuote.zoom = quote.zoom
            logAll()
        }
        serialize()
    }

    override fun findById(id:Long) : QuoteModel? {
        val foundQuote: QuoteModel? = quotes.find { it.id == id }
        return foundQuote
    }

    override fun delete(quote: QuoteModel) {
        quotes.remove(quote)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(quotes, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        quotes = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        quotes.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}
