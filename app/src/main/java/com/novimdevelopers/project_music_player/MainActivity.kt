package com.novimdevelopers.project_music_player

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.slider.Slider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.play_list.*
import soup.neumorphism.ShapeType
import java.sql.Time
import java.util.*

class MainActivity : AppCompatActivity(),MusicAdapter.OnClickItemMusic {
    var mediaPlayer: MediaPlayer? = null
    lateinit var bottomSheetBehavior :BottomSheetBehavior<ConstraintLayout>
    var musicList: List<MusicModel> = MusicModel.list
    var timer: Timer? = null
    var itemIndex = 0
    var musicState :MusicState= MusicState.STOP
    var musicAdapter :MusicAdapter?=null
    var isCheckBt=false
    var isChange=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        musicAdapter = MusicAdapter(musicList,this)
        bottomSheetBehavior = BottomSheetBehavior.from(cr_play_list)

        fab_playlist.setOnClickListener {

        if (isCheckBt)
        {
            bottomSheetBehavior!!.state=BottomSheetBehavior.STATE_COLLAPSED
            isCheckBt=false
        }
            else
        {
            bottomSheetBehavior!!.state=BottomSheetBehavior.STATE_EXPANDED
            isCheckBt=true
        }
        }
       rc_play_list.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)

        rc_play_list.adapter = musicAdapter
        musicPlay(musicList[0])


        slider_music.addOnSliderTouchListener(object : Slider.OnSliderTouchListener{
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: Slider) {

                isChange =true
            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: Slider) {

                isChange=false
                mediaPlayer!!.seekTo(slider.value.toInt())
            }

        })


        fab_next.setOnClickListener {

            onNext()
        }

        fab_back.setOnClickListener {

            onBack()
        }

        fab_play.setOnClickListener {

            when(musicState)
            {
                MusicState.PLAY->
                {
                    mediaPlayer!!.pause()
                    musicState = MusicState.PAUSE
                    fab_play.setImageResource(R.drawable.ic_baseline_play_arrow_24)

                }
                MusicState.PAUSE,MusicState.STOP ->
                {
                    mediaPlayer!!.start()
                    musicState =MusicState.PLAY
                    fab_play.setImageResource(R.drawable.ic_baseline_pause_24)


                }
            }
        }
    }

    enum class MusicState
    {
        PLAY,PAUSE,STOP
    }
    fun onNext() {
        fab_play.setImageResource(R.drawable.ic_baseline_pause_24)

        timer!!.cancel()
        timer!!.purge()
        mediaPlayer!!.release()
        if (itemIndex < musicList.size - 1) {
            itemIndex++
        } else
            itemIndex = 0
        musicPlay(musicList[itemIndex])

    }

    fun onBack() {
        fab_play.setImageResource(R.drawable.ic_baseline_pause_24)

        timer!!.cancel()
        timer!!.purge()
        mediaPlayer!!.release()
        if (itemIndex == 0) {
            itemIndex = musicList.size - 1
        } else

            itemIndex--
        musicPlay(musicList[itemIndex])

    }

    fun musicPlay(music: MusicModel) {
        slider_music.value = 0f
        musicAdapter!!.notifyMusic(music)
        mediaPlayer = MediaPlayer.create(this, music.musicRes)
        mediaPlayer!!.setOnPreparedListener {
            mediaPlayer!!.start()
            timer = Timer()
            timer!!.schedule(object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        if (!isChange)
                        slider_music.value = mediaPlayer!!.currentPosition.toFloat()
                        txt_position.text =
                            MusicModel.convertMillisToString(mediaPlayer!!.currentPosition.toLong())
                    }

                }

            }, 1000, 1000)


        }
        txt_duration.text = MusicModel.convertMillisToString(mediaPlayer!!.duration.toLong())
        slider_music.valueTo = mediaPlayer!!.duration.toFloat()
        musicState = MusicState.PLAY
        Picasso.get().load(music.coverRes).into(img_music)
        txt_artist.text = music.artist
        txt_name_music.text = music.name

        mediaPlayer!!.setOnCompletionListener { onNext() }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer!!.cancel()
        timer!!.purge()
        mediaPlayer!!.release()
        mediaPlayer =null
    }

    override fun onClickItem(music: MusicModel, position: Int) {

        timer!!.cancel()
        timer!!.purge()
        mediaPlayer!!.release()
        when(musicState)
        {
            MusicState.PLAY->
            {
                fab_play.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
            MusicState.PAUSE,MusicState.STOP ->
            {
                fab_play.setImageResource(R.drawable.ic_baseline_pause_24)

            }
        }
        itemIndex = position
        musicPlay(musicList[itemIndex])
    }
}