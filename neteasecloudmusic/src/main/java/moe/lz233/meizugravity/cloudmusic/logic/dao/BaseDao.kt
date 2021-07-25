package moe.lz233.meizugravity.cloudmusic.logic.dao

import moe.lz233.meizugravity.cloudmusic.App

object BaseDao {
    var baseurl: String
        get() = App.sp.getString("baseUrl", "")!!
        set(value) = App.editor.putString("baseUrl", value).apply()
}