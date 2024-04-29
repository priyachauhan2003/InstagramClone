package com.techmania.instagramclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.techmania.instagramclone.Models.Reel
import com.techmania.instagramclone.R
import com.techmania.instagramclone.databinding.ReelBgBinding

class ReelAdapter(var context:Context,var reelList:ArrayList<Reel>)
    :RecyclerView.Adapter<ReelAdapter.ViewHolder>(){
    inner class ViewHolder(var binding :ReelBgBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding=ReelBgBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return reelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(reelList.get(position).profileLink).placeholder(R.drawable.profile).into(holder.binding.profileImage2)
        holder.binding.caption.setText(reelList.get(position).caption)
        holder.binding.videoView.setVideoPath(reelList.get(position).reelUrl)
        holder.binding.videoView.setOnPreparedListener {
            holder.binding.progressBar.visibility= View.GONE
            holder.binding.videoView.start()
        }
    }
}