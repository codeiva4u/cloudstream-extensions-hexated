package com.BanglaPlex

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context
import com.hexated.Banglaplex
import com.hexated.Vectorx

@CloudstreamPlugin
class BanglaPlexProvider: Plugin() {
    override fun load(context: Context) {
        // registerMainAPI(Banglaplex())
        // registerExtractorAPI(Vectorx())
    }
}
