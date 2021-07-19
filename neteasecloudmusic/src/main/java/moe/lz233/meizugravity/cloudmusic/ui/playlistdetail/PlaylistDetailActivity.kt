package moe.lz233.meizugravity.cloudmusic.ui.playlistdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.zhy.mediaplayer_exo.playermanager.manager.MediaManager
import com.zhy.mediaplayer_exo.playermanager.meta.PlayListItem
import kotlinx.coroutines.launch
import moe.lz233.meizugravity.cloudmusic.R
import moe.lz233.meizugravity.cloudmusic.databinding.ActivityPlaylistDetailBinding
import moe.lz233.meizugravity.cloudmusic.databinding.ItemTitleBinding
import moe.lz233.meizugravity.cloudmusic.logic.dao.UserDao
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.Music
import moe.lz233.meizugravity.cloudmusic.logic.network.CloudMusicNetwork
import moe.lz233.meizugravity.cloudmusic.ui.BaseActivity
import moe.lz233.meizugravity.cloudmusic.utils.LogUtil
import moe.lz233.meizugravity.cloudmusic.utils.ktx.adjustParam
import moe.lz233.meizugravity.cloudmusic.utils.ktx.getSongUrl

class PlaylistDetailActivity : BaseActivity() {
    private val musicList = mutableListOf<Music>()
    private val musicAdapter by lazy { PlayListDetailAdapter(this, musicList) }
    private val viewBuilding by lazy { ActivityPlaylistDetailBinding.inflate(layoutInflater) }
    private val titleBuilding by lazy { ItemTitleBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBuilding.root)
        viewBuilding.musicListView.addFooterView(LayoutInflater.from(this).inflate(R.layout.item_music, viewBuilding.musicListView, false), null, false)
        launch {
            val playlistDetailResponse = CloudMusicNetwork.getPlaylistDetail(intent.getLongExtra("playlistId", 415524508))
            musicList.addAll(playlistDetailResponse.playlist.tracks)
            viewBuilding.musicListView.adapter = musicAdapter
            //musicAdapter.notifyDataSetChanged()
            Glide.with(viewBuilding.coverImageView)
                    .load(playlistDetailResponse.playlist.coverImgUrl.adjustParam("150", "150"))
                    .into(viewBuilding.coverImageView)
            viewBuilding.musicListView.setOnItemClickListener { adapterView, view, position, id ->
                val music = musicList[position - 1]
                LogUtil.d(music.id.getSongUrl())
                MediaManager.playlist(mutableListOf(PlayListItem(music.id, music.name, "", music.cover.picUrl, music.id.getSongUrl())))
                MediaManager.playOrPause()
            }
            titleBuilding.run {
                titleTextView.text = playlistDetailResponse.playlist.name.replace(UserDao.name, "我")
                summaryTextView.text = "${playlistDetailResponse.playlist.trackCount}首"
                viewBuilding.musicListView.addHeaderView(this.root, null, false)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_MENU -> finish()
            KeyEvent.KEYCODE_DPAD_UP -> {
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
            }
            KeyEvent.KEYCODE_ENTER -> {
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        fun actionStart(context: Context, playlistId: Long) = context.startActivity(Intent(context, PlaylistDetailActivity::class.java).apply { putExtra("playlistId", playlistId) })
    }
}