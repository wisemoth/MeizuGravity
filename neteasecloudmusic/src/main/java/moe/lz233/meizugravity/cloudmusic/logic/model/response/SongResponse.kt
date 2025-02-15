package moe.lz233.meizugravity.cloudmusic.logic.model.response

import com.google.gson.annotations.SerializedName
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.Comment
import moe.lz233.meizugravity.cloudmusic.logic.model.meta.MusicUrl

data class SongUrlResponse(val code: Int, val data: List<MusicUrl>)

data class SongLyricResponse(val code: Int,
                             val uncollected: Boolean?,
                             @SerializedName("nolyric") val noLyric: Boolean?,
                             @SerializedName("lrc") val lyric: LyricData?,
                             @SerializedName("tlyric") val translatedLyric: LyricData?) {
    data class LyricData(val version: Int, val lyric: String)
}

data class SongCommentResponse(val hotComments: List<Comment>, val comments: List<Comment>)

data class SongLikeResponse(val code: Int, val playListId: Long)

data class MvDetailResponse(val code: Int, val data: MvDetailData) {
    data class MvDetailData(@SerializedName("id") val mvId: Long,
                            val artistId: Long,
                            @SerializedName("cover") val coverUrl: String,
                            @SerializedName("brs") val resolutionData: List<MvResolutionData>) {
        data class MvResolutionData(val size: Long, @SerializedName("br") val resolution: Int, val point: Int)
    }
}

data class MvUrlResponse(val code: Int, val data: MvUrlData) {
    data class MvUrlData(val id: Long, val url: String)
}