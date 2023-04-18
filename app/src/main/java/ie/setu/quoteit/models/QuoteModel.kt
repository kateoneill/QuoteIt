package ie.setu.quoteit.models
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuoteModel(
    var id: Long = 0,
    var image: Uri = Uri.EMPTY,
    var quotation: String = "",
    var bookTitle: String ="",
    var pageNumber: Int = 0,
    var quoteTheme: String = "",
    var lat : Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f,
    var isFavourite: Boolean = false
) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable
