package com.HindiProviders

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context
import com.HindiProviders.Akamaicdn
import com.HindiProviders.FMHD
import com.HindiProviders.Hdmovie2
import com.HindiProviders.Movierulzhd
import com.lagradost.cloudstream3.extractors.VidSrcTo

@CloudstreamPlugin
class MovierulzhdPlugin: Plugin() {
    override fun load(context: Context) {
        // All providers should be added in this manner. Please don't edit the providers list directly.
        registerMainAPI(Movierulzhd())
        registerMainAPI(Hdmovie2())
        registerExtractorAPI(FMHD())
        registerExtractorAPI(VidSrcTo())
        registerExtractorAPI(Akamaicdn())
    }
}