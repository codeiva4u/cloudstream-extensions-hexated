package com.IndianTV

import android.annotation.SuppressLint
import android.util.Base64
import android.util.Log
import com.lagradost.cloudstream3.HomePageList
import com.lagradost.cloudstream3.HomePageResponse
import com.lagradost.cloudstream3.LoadResponse
import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.MainPageRequest
import com.lagradost.cloudstream3.SearchResponse
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.fixUrl
import com.lagradost.cloudstream3.fixUrlNull
import com.lagradost.cloudstream3.mainPageOf
import com.lagradost.cloudstream3.newHomePageResponse
import com.lagradost.cloudstream3.newMovieLoadResponse
import com.lagradost.cloudstream3.newMovieSearchResponse
import com.lagradost.cloudstream3.utils.DrmExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.INFER_TYPE
import com.lagradost.cloudstream3.utils.Qualities
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.nio.charset.StandardCharsets

class IndianTVProvider : MainAPI() {
    override var name = "Indian TV"
    override val hasMainPage = true
    override var lang = "hi"
    override val supportedTypes = setOf(TvType.Live)

    override val mainPage = mainPageOf(
        INDIANTATAAPI to "TATA",
        INDIANJIOAPI to "Jio TV",
//        INDIANDiscoveryAPI to "Discovery",
//        INDIANTVVootAPI to "Voot",
//        INDIANAirtelAPI to "Airtel"
    )

    companion object {
        const val INDIANJIOAPI = "https://madstream.one/pages/jiotv.php"
        const val INDIANTATAAPI = "https://madstream.one/pages/tataplay.php"
//        const val INDIANDiscoveryAPI = BuildConfig.INDIANTV_Discovery_API
//        const val INDIANAirtelAPI = BuildConfig.INDIANTV_Airtel_API
//        const val INDIANTVVootAPI = BuildConfig.INDIANTV_Voot_API

        val Useragent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:125.0) Gecko/20100101 Firefox/125.0"
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get(request.data, headers = mapOf("User-Agent" to Useragent)).document
        val home =
            document.select("div#listContainer > div.box1").mapNotNull { it.toSearchResult() }
        return newHomePageResponse(
            list = HomePageList(
                name = request.name,
                list = home,
                isHorizontalImages = true
            ),
            hasNext = false
        )
    }

    private fun Element.toSearchResult(): SearchResponse {
        val title = this.select("h2.text-center").text()
        val href = fixUrl(this.select("a").attr("href"))
        val posterUrl = fixUrlNull(this.select("img").attr("src"))
        return newMovieSearchResponse(title, href, TvType.Live) {
            this.posterUrl = posterUrl
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val documentTata = app.get(INDIANTATAAPI, headers = mapOf("User-Agent" to Useragent)).document
        val documentJiotv = app.get(INDIANJIOAPI, headers = mapOf("User-Agent" to Useragent)).document
        val mergedDocument = Document.createShell("")
        mergedDocument.body().append(documentTata.body().html())
        mergedDocument.body().append(documentJiotv.body().html())

        return mergedDocument.select("div#listContainer div.box1:contains($query)")
            .mapNotNull {
                it.toSearchResult()
            }
    }

    override suspend fun load(url: String): LoadResponse {
        if (url.contains("jiotv")) {
            val title = "JioTV"
            val poster = "https://i0.wp.com/www.smartprix.com/bytes/wp-content/uploads/2021/08/JioTV-on-smart-TV.png?fit=1200%2C675&ssl=1"
            val showname = "JioTV"

            return newMovieLoadResponse(title, url, TvType.Live, url) {
                this.posterUrl = poster
                this.plot = showname
            }
        } else if (url.contains("tata")) {
            val title = "TATA"
            val poster = "https://cdn.mos.cms.futurecdn.net/iYdoTcTScdApk3JV5GfEAT-1920-80.jpg"
            val showname = "TATA"

            return newMovieLoadResponse(title, url, TvType.Live, url) {
                this.posterUrl = poster
                this.plot = showname
            }
        }
        val document = app.get(url, headers = mapOf("User-Agent" to Useragent)).document
        val title = document.selectFirst("div.program-info > span.channel-name")?.text()?.trim().toString()
        val poster = "https://cdn.mos.cms.futurecdn.net/iYdoTcTScdApk3JV5GfEAT-1920-80.jpg"
        val showname = document.selectFirst("div.program-info > div.program-name")?.text()?.trim().toString()

        return newMovieLoadResponse(title, url, TvType.Live, url) {
            this.posterUrl = poster
            this.plot = showname
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("Phisher", data)
        if (data.contains("jiotv")) {
            val channelID = data.substringAfter("=")
            val link = "https://madplay.live/hls/jiotv/stream.php?id=$channelID&e=.m3u8"
            callback.invoke(
                ExtractorLink(
                    source = "INDIAN TV",
                    name = "INDIAN TV",
                    url = link,
                    referer = "",
                    quality = Qualities.Unknown.value,
                    isM3u8 = true,
                )
            )
        } else if (data.contains("tata")) {
            Log.d("Rhinoout", data)
            val channelID = data.substringAfter("=")
            val link = "https://madplay.live/hls/tata/stream.php?id=$channelID&e=.m3u8"
            callback.invoke(
                ExtractorLink(
                    source = "INDIAN TV",
                    name = "INDIAN TV",
                    url = link,
                    referer = "",
                    quality = Qualities.Unknown.value,
                    isM3u8 = true,
                )
            )
        } else if (data.contains("jwplayer.php?")) {
            val link = data.substringAfter("jwplayer.php?")
            callback.invoke(
                ExtractorLink(
                    source = "INDIAN TV",
                    name = "INDIAN TV",
                    url = link,
                    referer = "",
                    quality = Qualities.Unknown.value,
                    type = INFER_TYPE,
                )
            )
        }
        return true
    }

    private fun decodeHex(hexString: String): String {
        val length = hexString.length
        val byteArray = ByteArray(length / 2)

        for (i in 0 until length step 2) {
            byteArray[i / 2] = ((Character.digit(hexString[i], 16) shl 4) +
                    Character.digit(hexString[i + 1], 16)).toByte()
        }
        val base64ByteArray = Base64.encode(byteArray, Base64.NO_PADDING)
        return String(base64ByteArray, StandardCharsets.UTF_8).trim()
    }
}
