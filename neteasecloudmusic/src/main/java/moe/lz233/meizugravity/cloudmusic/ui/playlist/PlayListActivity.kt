package moe.lz233.meizugravity.cloudmusic.ui.playlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import moe.lz233.meizugravity.cloudmusic.R
import moe.lz233.meizugravity.cloudmusic.databinding.ActivityPlaylistBinding
import moe.lz233.meizugravity.cloudmusic.logic.dao.UserDao
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.PlayList
import moe.lz233.meizugravity.cloudmusic.logic.network.CloudMusicNetwork
import moe.lz233.meizugravity.cloudmusic.ui.BaseActivity
import moe.lz233.meizugravity.cloudmusic.ui.playlistdetail.PlaylistDetailActivity
import moe.lz233.meizugravity.cloudmusic.utils.ktx.adjustParam
import moe.lz233.meizugravity.cloudmusic.utils.ktx.setOnItemSelectedListener

class PlayListActivity : BaseActivity() {
    private val playLists = mutableListOf<PlayList>()
    private val playlistAdapter by lazy { PlayListAdapter(this, playLists) }
    private val viewBuilding by lazy { ActivityPlaylistBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBuilding.root)
        LayoutInflater.from(this@PlayListActivity).inflate(R.layout.item_playlist, viewBuilding.playlistListView, false).run {
            viewBuilding.playlistListView.addHeaderView(this, null, false)
            viewBuilding.playlistListView.addFooterView(this, null, false)
        }
        launch {
            val userPlaylistResponse = CloudMusicNetwork.getUserPlaylist(UserDao.id)
            playLists.addAll(userPlaylistResponse.playlists)
            viewBuilding.progressBar.visibility = View.GONE
            viewBuilding.playlistListView.adapter = playlistAdapter
            Glide.with(viewBuilding.coverImageView)
                    .load(playLists[0].coverImgUrl.adjustParam("150", "150"))
                    .into(viewBuilding.coverImageView)
            viewBuilding.playlistListView.setOnItemClickListener { adapterView, view, position, id ->
                val playList = playLists[position - 1]
                PlaylistDetailActivity.actionStart(this@PlayListActivity, playList.id)
            }
            viewBuilding.playlistListView.setOnItemSelectedListener { selected, parent, view, position, id ->
                if (selected) {
                    val playList = playLists[position!! - 1]
                    Glide.with(viewBuilding.coverImageView)
                            .load(playList.coverImgUrl.adjustParam("150", "150"))
                            .into(viewBuilding.coverImageView)
                }
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
        fun actionStart(context: Context) = context.startActivity(Intent(context, PlayListActivity::class.java))
    }
}