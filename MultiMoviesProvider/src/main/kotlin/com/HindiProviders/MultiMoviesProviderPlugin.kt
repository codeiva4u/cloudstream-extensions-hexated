package com.HindiProviders

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context
import com.lagradost.cloudstream3.extractors.Chillx
import com.lagradost.cloudstream3.extractors.DoodLaExtractor
import com.lagradost.cloudstream3.extractors.MixDrop
import com.lagradost.cloudstream3.extractors.XStreamCdn

@CloudstreamPlugin
class MultiMoviesProviderPlugin: Plugin() {
    override fun load(context: Context) {
        // All providers should be added in this manner. Please don't edit the providers list directly.
        registerMainAPI(MultiMoviesProvider())
        registerExtractorAPI(Chillx())
        registerExtractorAPI(Beastx())
        registerExtractorAPI(Bestx())
        registerExtractorAPI(Boltx())
        registerExtractorAPI(Boosterx())
        registerExtractorAPI(MixDrop())
        registerExtractorAPI(Multimovies())
        registerExtractorAPI(XStreamCdn())
        registerExtractorAPI(DoodLaExtractor())
        registerExtractorAPI(Animezia())
        registerExtractorAPI(server2())
        registerExtractorAPI(MultimoviesAIO())
        //registerExtractorAPI(GDMirrorbot())
        //registerExtractorAPI(VidhideExtractor())
        registerExtractorAPI(Asnwish())
        registerExtractorAPI(CdnwishCom())
        registerExtractorAPI(Strwishcom())
        registerExtractorAPI(filelionslive())
        registerExtractorAPI(filelionsonline())
        registerExtractorAPI(filelionsto())
        registerExtractorAPI(kinogerbe())
        registerExtractorAPI(vidhidevipcom())
        registerExtractorAPI(vidhideprecom())
        registerExtractorAPI(lulustreamcom())
        registerExtractorAPI(luluvdocom())
        registerExtractorAPI(kinogerpw())
    }
}
