package com.novimdevelopers.project_music_player


import java.util.*

class MusicModel {
    var id = 0
    var name: String? = null
    var artist: String? = null
    var coverRes = 0
    var musicRes = 0

    companion object {
        val list: List<MusicModel>
            get() {
                val musicList: MutableList<MusicModel> = ArrayList()
                val music1 = MusicModel()
                music1.artist = "ماکان بند"
                music1.name = "واقعی"
                music1.coverRes = R.drawable.vaghei
                music1.musicRes = R.raw.macan_band
                val music2 = MusicModel()
                music2.artist = "امید حاجی لو"
                music2.name = "سلام علیکم"
                music2.coverRes = R.drawable.salam
                music2.musicRes = R.raw.omid_hajilo
                val music3 = MusicModel()
                music3.artist = "احسان خواجه امیری"
                music3.name = "یکیو دارم"
                music3.coverRes = R.drawable.ehsan
                music3.musicRes = R.raw.yekio_dara
                musicList.add(music1)
                musicList.add(music2)
                musicList.add(music3)
                return musicList
            }

        fun convertMillisToString(durationInMillis: Long): String {
            val second = durationInMillis / 1000 % 60
            val minute = durationInMillis / (1000 * 60) % 60
            return String.format(Locale.US, "%02d:%02d", minute, second)
        }
    }
}