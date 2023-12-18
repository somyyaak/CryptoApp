package com.example.cryptoapp

import android.os.Bundle
import android.os.Handler
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.cryptoapp.data.models.CryptoAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

class MainActivity : AppCompatActivity() {
    private lateinit var cryptoViewModel: CryptoViewModel
    private lateinit var cryptoAdapter: CryptoAdapter

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var mHandler: Handler? = null
    private var mRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView: RecyclerView = findViewById(R.id.itemList)
        val shimmer: ShimmerFrameLayout = findViewById(R.id.shimmer_layout)
        val timeStamp = findViewById<TextView>(R.id.textView2)
        cryptoAdapter = CryptoAdapter(this, arrayListOf())
        mHandler = Handler()
        mRunnable = object : Runnable {
            override fun run() {
                shimmer.visibility = VISIBLE
                timeStamp.visibility = GONE
                recyclerView.visibility = GONE
                shimmer.startShimmerAnimation()
                cryptoViewModel.fetchData()
                mHandler?.postDelayed(this, (3 * 60 * 1000).toLong())
            }
        }
        shimmer.visibility = VISIBLE
        timeStamp.visibility = GONE
        recyclerView.visibility = GONE
        shimmer.startShimmerAnimation()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = cryptoAdapter
        cryptoViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[CryptoViewModel::class.java]
        cryptoViewModel.fetchData()
        cryptoViewModel.cryptosLive.observe(this
        ) { t ->
            if (t.isNotEmpty() && t != null) {
                shimmer.visibility = GONE
                timeStamp.visibility = VISIBLE
                recyclerView.visibility = VISIBLE
                shimmer.stopShimmerAnimation()
                cryptoAdapter.update(t)
                timeStamp.text = getDate(t[0]?.timeStamp ?: 0)

            }
        }
        cryptoViewModel.successStatusLive.observe(this) {
            if(it == false) {
                showErrorDialog("An Error Occurred", "Something went wrong. Please try again later.");
            }
        }
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener {
            shimmer.visibility = VISIBLE
            timeStamp.visibility = GONE
            recyclerView.visibility = GONE
            shimmer.startShimmerAnimation()
            cryptoViewModel.fetchData()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        if(mRunnable != null) {
            mHandler?.postDelayed(mRunnable!!, (3 * 60 * 1000).toLong())
        }
    }

    override fun onPause() {
        super.onPause()
        if(mRunnable != null) {
            mHandler?.removeCallbacks(mRunnable!!)
        }
    }

    private fun showErrorDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("RETRY") { dialog, _ ->
                cryptoViewModel.fetchData()
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }

    private fun getDate(timeStamp: Long): String {
        val format: DateFormat = SimpleDateFormat("E, dd MMM yyyy HH:mm:ss")
        val timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        format.timeZone = timeZone
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = timeStamp * 1000
        return format.format(calendar.time)
    }
}