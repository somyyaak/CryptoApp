package com.example.cryptoapp.data.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoapp.R
import com.example.cryptoapp.data.CryptoUIModel
import java.math.RoundingMode
import java.text.DecimalFormat

class CryptoAdapter(private val context: Context, private val itemsData: ArrayList<CryptoUIModel?>) :
 RecyclerView.Adapter<CryptoViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CryptoViewHolder {

        val v1 : View = LayoutInflater.from(context)
            .inflate(R.layout.currency_item,p0,false)
        return CryptoViewHolder(v1)
    }

    override fun getItemCount(): Int {
        return itemsData.size
    }

    override fun onBindViewHolder(p0: CryptoViewHolder, p1: Int) {
        val df = DecimalFormat(DECIMAL_PATTERN)
        df.roundingMode = RoundingMode.CEILING
        p0.itemName.text = itemsData[p1]?.nameFull
        p0.itemRate.text = df.format(itemsData[p1]?.exchangeRate)
        Glide.with(p0.itemImage.context).load(itemsData[p1]?.iconUrl)
            .into(p0.itemImage)
    }

    fun update(updatedData: List<CryptoUIModel?>) {
        itemsData.clear()
        itemsData.addAll(updatedData)
        notifyDataSetChanged()
    }

    companion object {
        const val DECIMAL_PATTERN = "#.######"
    }

}

class CryptoViewHolder( itemView : View) : RecyclerView.ViewHolder(itemView){
    var itemName : TextView = itemView.findViewById(R.id.full_name)
    var itemImage : ImageView = itemView.findViewById(R.id.item_img)
    var itemRate : TextView = itemView.findViewById(R.id.exchange)
}