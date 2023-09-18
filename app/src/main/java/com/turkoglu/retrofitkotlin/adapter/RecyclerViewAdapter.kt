package com.turkoglu.retrofitkotlin.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.turkoglu.retrofitkotlin.databinding.RecyclerRowBinding
import com.turkoglu.retrofitkotlin.model.CryptoModel

class RecyclerViewAdapter(private val cryptoList : ArrayList<CryptoModel>) : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerAdapter>() {


    private val colors: Array<String> = arrayOf("#13bd27","#29c1e1","#b129e1","#d3df13","#f6bd0c","#a1fb93","#0d9de3","#ffe48f")


    class RecyclerAdapter(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter {

        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return RecyclerAdapter(binding)

    }

    override fun getItemCount(): Int {
        return cryptoList.count()
    }

    override fun onBindViewHolder(holder: RecyclerAdapter, position: Int) {
        holder.itemView.setBackgroundColor(Color.parseColor(colors[position % 8]))
        holder.binding.textName.text = cryptoList[position].currency
        holder.binding.textPrice.text = cryptoList[position].price
    }


}