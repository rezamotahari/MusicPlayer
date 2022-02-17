package com.novimdevelopers.project_music_player


import android.opengl.Visibility
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.squareup.picasso.Picasso

class MusicAdapter(private val musicList: List<MusicModel>,val onClickItemMusic: OnClickItemMusic) :
    RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    var itemPostion = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        return MusicViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_music, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.bindMusic(musicList[position])

    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    inner class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imagMusic: ImageView
        private val txtArtist: TextView
        private val txtName: TextView
        private val animationView: LottieAnimationView
        private val frMusic: FrameLayout

        fun bindMusic(music: MusicModel) {
            Picasso.get().load(music.coverRes).into(imagMusic)

            txtArtist.text = music.artist
            txtName.text = music.name
            if (adapterPosition == itemPostion)
            {
                animationView.visibility = View.VISIBLE
                frMusic.background =ContextCompat.getDrawable(itemView.context,R.drawable.shape_fr_item)
            }
            else
            {
                animationView.visibility =View.GONE
                frMusic.background = null
            }
            itemView.setOnClickListener {

                onClickItemMusic.onClickItem(music,adapterPosition)
            }

        }

        init {
            imagMusic = itemView.findViewById(R.id.iv_music)
            txtArtist = itemView.findViewById(R.id.tv_music_artist)
            txtName = itemView.findViewById(R.id.tv_music_name)
            animationView = itemView.findViewById(R.id.animationView)
            frMusic = itemView.findViewById(R.id.fr_music)

        }
    }

    fun notifyMusic(music: MusicModel) {
        val indexItem = musicList.indexOf(music)
        if (indexItem != -1) {

            if (indexItem != itemPostion)
            {
                notifyItemChanged(itemPostion)
                itemPostion = indexItem
                notifyItemChanged(itemPostion)
            }
        }
    }
interface OnClickItemMusic{
    fun onClickItem(music: MusicModel,position: Int)
}
}