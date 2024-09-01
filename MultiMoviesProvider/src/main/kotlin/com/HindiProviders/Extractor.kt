package com.HindiProviders

import com.lagradost.cloudstream3.extractors.StreamWishExtractor
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.network.WebViewResolver
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.USER_AGENT
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.M3u8Helper.Companion.generateM3u8

class MultimoviesAIO: StreamWishExtractor() {
    override var name = "Multimovies Cloud AIO"
    override var mainUrl = "https://allinonedownloader.fun"
    override var requiresReferer = true
}

class Multimovies: StreamWishExtractor() {
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

class Asnwish : StreamWishExtractor() {
    override var name = "Streanwish Asn"
    override val mainUrl = "https://asnwish.com"
    override val requiresReferer = true
}

class CdnwishCom : StreamWishExtractor() {
    override var name = "Cdnwish"
    override val mainUrl = "https://cdnwish.com"
    override val requiresReferer = true
}

class Strwishcom : StreamWishExtractor() {
    override var name = "Strwish"
    override val mainUrl = "https://strwish.com"
    override val requiresReferer = true
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

class filelionslive : VidHidePro() {
    override var name = "filelionslive"
    override var mainUrl = "https://filelions.live"
    override val requiresReferer = true
}

class filelionsonline : VidHidePro() {
    override var name = "filelionsonline"
    override var mainUrl = "https://filelions.online"
    override val requiresReferer = true
}

class filelionsto : VidHidePro() {
    override var name = "filelionsto"
    override var mainUrl = "https://filelions.to"
    override val requiresReferer = true
}

class kinogerbe : VidHidePro() {
    override var name = "kinogerbe"
    override val mainUrl = "https://kinoger.be"
    override val requiresReferer = true
}

class vidhidevipcom : VidHidePro() {
    override var name = "vidhidevipcom"
    override val mainUrl = "https://vidhidevip.com"
    override val requiresReferer = true
}

class vidhideprecom : VidHidePro() {
    override var name = "vidhideprecom"
    override val mainUrl = "https://vidhidepre.com"
    override val requiresReferer = true
}

class lulustreamcom : VidHidePro() {
    override var name = "lulustreamcom"
    override val mainUrl = "https://lulustream.com"
    override val requiresReferer = true
}

class luluvdocom : VidHidePro() {
    override var name = "luluvdocom"
    override val mainUrl = "https://luluvdo.com"
    override val requiresReferer = true
}

class kinogerpw : VidHidePro() {
    override var name = "kinogerpw"
    override val mainUrl = "https://kinoger.pw"
    override val requiresReferer = true
}

open class VidHidePro : ExtractorApi() {
    override val name = "VidHidePro"
    override val mainUrl = "https://vidhidepro.com"
    override val requiresReferer = true

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
            "User-Agent" to USER_AGENT,
        )

        val response = app.get(getEmbedUrl(url), referer = referer)
        val script = if (!getPacked(response.text).isNullOrEmpty()) {
            getAndUnpack(response.text)
        } else {
            response.document.selectFirst("script:containsData(sources:)")?.data()
        }
        val m3u8 =
            Regex("file:\\s*\"(.*?m3u8.*?)\"").find(script ?: return)?.groupValues?.getOrNull(1)
        generateM3u8(
            name,
            m3u8 ?: return,
            mainUrl,
            headers = headers
        ).forEach(callback)
    }

    private fun getEmbedUrl(url: String): String {
        return when {
            url.contains("/d/") -> url.replace("/d/", "/v/")
            url.contains("/download/") -> url.replace("/download/", "/v/")
            url.contains("/file/") -> url.replace("/file/", "/v/")
            else -> url.replace("/f/", "/v/")
        }
    }
}
