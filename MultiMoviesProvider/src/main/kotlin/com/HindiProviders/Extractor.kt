package com.HindiProviders

import com.lagradost.cloudstream3.extractors.StreamWishExtractor
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.USER_AGENT
import com.lagradost.cloudstream3.network.WebViewResolver
import com.lagradost.cloudstream3.utils.M3u8Helper
import com.lagradost.cloudstream3.utils.getAndUnpack
import com.lagradost.cloudstream3.utils.getPacked

class MultimoviesAIO : StreamWishExtractor() {
    override var name = "Multimovies Cloud AIO"
    override val mainUrl = "https://allinonedownloader.fun"
    override val requiresReferer = true
}

class Multimovies : StreamWishExtractor() {
    override var name = "Multimovies Cloud"
    override val mainUrl = "https://multimovies.cloud"
    override val requiresReferer = true
}

class Animezia : VidhideExtractor() {
    override val name = "Animezia"
    override val mainUrl = "https://animezia.cloud"
    override val requiresReferer = true
}

class Server2 : VidhideExtractor() {
    override val name = "Multimovies Vidhide"
    override val mainUrl = "https://server2.shop"
    override val requiresReferer = true
}

class GDMirrorbot : ExtractorApi() {
    override val name = "GDMirrorbot"
    override val mainUrl = "https://gdmirrorbot.nl"
    override val requiresReferer = false

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        app.get(url).document.select("ul#videoLinks li").map {
            val link = it.attr("data-link")
            loadExtractor(link, subtitleCallback, callback)
        }
    }
}

open class VidhideExtractor : ExtractorApi() {
    override val name = "VidHide"
    override val mainUrl = "https://vidhide.com"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?): List<ExtractorLink>? {
        val response = app.get(
            url, referer = referer ?: "$mainUrl/", interceptor = WebViewResolver(
                Regex("""master\.m3u8""")
            )
        )
        val sources = mutableListOf<ExtractorLink>()
        if (response.url.contains("m3u8"))
            sources.add(
                ExtractorLink(
                    source = name,
                    name = name,
                    url = response.url,
                    referer = referer ?: "$mainUrl/",
                    quality = Qualities.Unknown.value,
                    isM3u8 = true
                )
            )
        return sources
    }
}

class Asnwish : StreamWishExtractor() {
    override var name = "Asnwish"
    override val mainUrl = "https://asnwish.com"

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val headers = mapOf(
            "Accept" to "*/*",
            "Connection" to "keep-alive",
            "Sec-Fetch-Dest" to "empty",
            "Sec-Fetch-Mode" to "cors",
            "Sec-Fetch-Site" to "cross-site",
            "Origin" to "$mainUrl/",
            "User-Agent" to USER_AGENT
        )
        val response = app.get(getEmbedUrl(url), referer = referer)
        val script = if (!getPacked(response.text).isNullOrEmpty()) {
            getAndUnpack(response.text)
        } else if (!response.document.select("script").firstOrNull {
                it.html().contains("jwplayer(\"vplayer\").setup(")
            }?.html().isNullOrEmpty()
        ) {
            response.document.select("script").firstOrNull {
                it.html().contains("jwplayer(\"vplayer\").setup(")
            }?.html()
        } else {
            response.document.selectFirst("script:containsData(sources:)")?.data()
        }
        val m3u8 =
            Regex("file:\\s*\"(.*?m3u8.*?)\"").find(script ?: return)?.groupValues?.getOrNull(1)
        M3u8Helper.generateM3u8(
            name,
            m3u8 ?: return,
            mainUrl,
            headers = headers
        ).forEach(callback)
    }

    private fun getEmbedUrl(url: String): String {
        return if (url.contains("/f/")) {
            val videoId = url.substringAfter("/f/")
            "$mainUrl/$videoId"
        } else {
            url
        }
    }
}