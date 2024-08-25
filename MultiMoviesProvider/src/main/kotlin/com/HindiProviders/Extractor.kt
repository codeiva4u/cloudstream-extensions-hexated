package com.HindiProviders

import com.lagradost.cloudstream3.extractors.StreamWishExtractor
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.INFER_TYPE
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.network.WebViewResolver
import com.lagradost.cloudstream3.utils.M3u8Helper
import com.lagradost.cloudstream3.utils.getAndUnpack
import com.lagradost.cloudstream3.utils.getPacked

class MultimoviesAIO : StreamWishExtractor() {
    override var name = "Multimovies Cloud AIO"
    override var mainUrl = "https://allinonedownloader.fun"
    override var requiresReferer = true
}

class Multimovies : StreamWishExtractor() {
    override var name = "Multimovies Cloud"
    override var mainUrl = "https://multimovies.cloud"
    override var requiresReferer = true
}

class Animezia : VidhideExtractor() {
    override var name = "Animezia"
    override var mainUrl = "https://animezia.cloud"
    override var requiresReferer = true
}

class server2 : VidhideExtractor() {
    override var name = "Multimovies Vidhide"
    override var mainUrl = "https://server2.shop"
    override var requiresReferer = true
}

class GDMirrorbot : ExtractorApi() {
    override var name = "GDMirrorbot"
    override var mainUrl = "https://gdmirrorbot.nl"
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
    override var name = "VidHide"
    override var mainUrl = "https://vidhide.com"
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
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val headers = mapOf(
            "Accept" to "*/*",
            "Accept-Encoding" to "gzip, deflate, br, zstd",
            "Accept-Language" to "en-US,en;q=0.7",
            "Connection" to "keep-alive",
            "Host" to "k3q37qtf.cdn-jupiter.com",
            "Origin" to "$mainUrl/",
            "Referer" to "$mainUrl/",
            "Sec-Fetch-Dest" to "empty",
            "Sec-Fetch-Mode" to "cors",
            "Sec-Fetch-Site" to "cross-site",
            "Sec-GPC" to "1",
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36",
            "sec-ch-ua" to "\"Chromium\";v=\"128\", \"Not;A=Brand\";v=\"24\", \"Brave\";v=\"128\"",
            "sec-ch-ua-mobile" to "?0",
            "sec-ch-ua-platform" to "\"Windows\""
        )

        val response = app.get(getEmbedUrl(url), referer = referer, headers = headers)
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
