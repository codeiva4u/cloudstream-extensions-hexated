package com.HindiProviders

import com.lagradost.cloudstream3.extractors.StreamWishExtractor
import com.lagradost.cloudstream3.extractors.VidhideExtractor


class Multimovies : StreamWishExtractor() {
    override var name = "Multimovies Cloud"
    override var mainUrl = "https://multimovies.cloud"
}

class MultimoviesAIO: StreamWishExtractor() {
    override var name = "Multimovies Cloud AIO"
    override var mainUrl = "https://allinonedownloader.fun"
}

class Animezia : VidhideExtractor() {
    override var name = "Animezia"
    override var mainUrl = "https://animezia.cloud"
}

class server2 : VidhideExtractor() {
    override var name = "Multimovies Vidhide"
    override var mainUrl = "https://server2.shop"
}


class Gogoanime : ExtractorApi() {
    override var name = "GogoAnime"
    override val mainUrl = "https://gogoanime.website"
    override val requiresReferer = false

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val doc = app.get(
            url,
            referer = referer,
            allowRedirects = false
        ).document

        val videoElement = doc.select("video.jw-video").first()
        val videoUrl = videoElement?.attr("src")

        if (!videoUrl.isNullOrBlank()) {
            callback(
                ExtractorLink(
                    this.name,
                    this.name,
                    videoUrl,
                    referer ?: "$mainUrl/",
                    getQualityFromName(""),
                    videoUrl.contains("m3u8")
                )
            )
        }
    }
}

