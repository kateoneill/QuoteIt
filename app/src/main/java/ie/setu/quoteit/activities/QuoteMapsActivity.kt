package ie.setu.quoteit.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso

import ie.setu.quoteit.databinding.ActivityQuoteMapsBinding
import ie.setu.quoteit.databinding.ContentQuoteMapsBinding
import ie.setu.quoteit.main.MainApp
import ie.setu.quoteit.models.QuoteModel

class QuoteMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityQuoteMapsBinding
    private lateinit var contentBinding: ContentQuoteMapsBinding
    lateinit var map: GoogleMap
    lateinit var app: MainApp


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuoteMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        contentBinding = ContentQuoteMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)

        app = application as MainApp

        contentBinding.mapView.getMapAsync {
            map = it
            configureMap()
        }
    }

    private fun configureMap() {
        map.uiSettings.isZoomControlsEnabled = true
        app.quotes.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions()
                .title(it.quotation).position(loc)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
            map.addMarker(options)?.tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
            map.setOnMarkerClickListener(this)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val tag = marker.tag as Long
        val quote = app.quotes.findById(tag)
        contentBinding.currentQuotation.text = quote!!.quotation
        contentBinding.currentBookTitle.text = quote.bookTitle
        contentBinding.currentTheme.text = quote.quoteTheme
        contentBinding.currentPageNumber.text = quote.pageNumber.toString()
        Picasso.get().load(quote.image).into(contentBinding.imageView2)
        return false
    }


    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }
}
