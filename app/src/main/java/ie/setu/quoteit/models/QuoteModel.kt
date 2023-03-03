package ie.setu.quoteit.models
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuoteModel(
    var id: Long = 0,
    var quotation: String = "",
    var bookTitle: String ="",
    var pageNumber: Int = 0,
    var quoteTheme: String = "",
    var isFavourite: Boolean = false
) : Parcelable
