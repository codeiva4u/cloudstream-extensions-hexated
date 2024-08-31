package com.HindiProviders

import android.content.Context
import com.lagradost.cloudstream3.extractors.AStreamHub
import com.lagradost.cloudstream3.extractors.Acefile
import com.lagradost.cloudstream3.extractors.Ahvsh
import com.lagradost.cloudstream3.extractors.Aico
import com.lagradost.cloudstream3.extractors.Bestx
import com.lagradost.cloudstream3.extractors.BullStream
import com.lagradost.cloudstream3.extractors.Cdnplayer
import com.lagradost.cloudstream3.extractors.CdnwishCom
import com.lagradost.cloudstream3.extractors.Chillx
import com.lagradost.cloudstream3.extractors.DBfilm
import com.lagradost.cloudstream3.extractors.DoodLaExtractor
import com.lagradost.cloudstream3.extractors.FEmbed
import com.lagradost.cloudstream3.extractors.FileMoon
import com.lagradost.cloudstream3.extractors.GMPlayer
import com.lagradost.cloudstream3.extractors.Gdriveplayer
import com.lagradost.cloudstream3.extractors.GenericM3U8
import com.lagradost.cloudstream3.extractors.HDMomPlayer
import com.lagradost.cloudstream3.extractors.JWPlayer
import com.lagradost.cloudstream3.extractors.Minoplres
import com.lagradost.cloudstream3.extractors.MixDrop
import com.lagradost.cloudstream3.extractors.StreamTape
import com.lagradost.cloudstream3.extractors.StreamWishExtractor
import com.lagradost.cloudstream3.extractors.VidSrcExtractor
import com.lagradost.cloudstream3.extractors.VidSrcExtractor2
import com.lagradost.cloudstream3.extractors.VidSrcTo
import com.lagradost.cloudstream3.extractors.XStreamCdn
import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin

@CloudstreamPlugin
class MultiMoviesProviderPlugin: Plugin() {
    override fun load(context: Context) {
        // All providers should be added in this manner. Please don't edit the providers list directly.
        registerMainAPI(MultiMoviesProvider())
        registerExtractorAPI(MixDrop())
        registerExtractorAPI(XStreamCdn())
        registerExtractorAPI(DoodLaExtractor())
        registerExtractorAPI(StreamTape())
        registerExtractorAPI(Chillx())
        registerExtractorAPI(AStreamHub())
        registerExtractorAPI(Acefile())
        registerExtractorAPI(Ahvsh())
        registerExtractorAPI(Aico())
        registerExtractorAPI(Bestx())
        registerExtractorAPI(BullStream())
        registerExtractorAPI(Cdnplayer())
        registerExtractorAPI(DBfilm())
        registerExtractorAPI(FEmbed())
        registerExtractorAPI(FileMoon())
        registerExtractorAPI(GMPlayer())
        registerExtractorAPI(Gdriveplayer())
        registerExtractorAPI(GenericM3U8())
        registerExtractorAPI(HDMomPlayer())
        registerExtractorAPI(JWPlayer())
        registerExtractorAPI(Minoplres())
        registerExtractorAPI(StreamWishExtractor())
        registerExtractorAPI(VidSrcExtractor())
        registerExtractorAPI(VidSrcExtractor2())
        registerExtractorAPI(VidSrcTo())
        registerExtractorAPI(MultimoviesAIO())
        registerExtractorAPI(Multimovies())
        registerExtractorAPI(Animezia())
        registerExtractorAPI(Asnwish())
        registerExtractorAPI(Strwishcom())
        registerExtractorAPI(CdnwishCom())
        
    }
}
