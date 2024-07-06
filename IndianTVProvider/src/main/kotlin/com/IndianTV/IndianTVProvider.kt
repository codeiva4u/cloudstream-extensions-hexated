package com.IndianTV

import android.annotation.SuppressLint
import android.util.Log
import org.jsoup.nodes.Element
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import android.util.Base64
import org.jsoup.nodes.Document
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
        val document = app.get(request.data,headers = mapOf("User-Agent" to Useragent)).document
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
            //val category = this.select("p").text()
            return newMovieSearchResponse(title, href, TvType.Live) {
                this.posterUrl = posterUrl
            }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val documentTata = app.get(INDIANTATAAPI,headers = mapOf("User-Agent" to Useragent)).document
   //     val documentAirtel = app.get(INDIANAirtelAPI,headers = mapOf("User-Agent" to Useragent)).document
        val documentJiotv = app.get(INDIANJIOAPI,headers = mapOf("User-Agent" to Useragent)).document
   //     val documentdiscovery = app.get(INDIANDiscoveryAPI,headers = mapOf("User-Agent" to Useragent)).document
        val mergedDocument = Document.createShell("")
        mergedDocument.body().append(documentTata.body().html())
   //     mergedDocument.body().append(documentdiscovery.body().html())
   //     mergedDocument.body().append(documentAirtel.body().html())
        mergedDocument.body().append(documentJiotv.body().html())

            return mergedDocument.select("div#listContainer div.box1:contains($query), div#listContainer div.box1:contains($query)")
                .mapNotNull {
                    it.toSearchResult()
                }
    }

    override suspend fun load(url: String): LoadResponse {
        if (url.contains("jiotv"))
        {
            val title ="JioTV"
            val poster ="https://i0.wp.com/www.smartprix.com/bytes/wp-content/uploads/2021/08/JioTV-on-smart-TV.png?fit=1200%2C675&ssl=1"
            val showname ="JioTV"

            return newMovieLoadResponse(title, url, TvType.Live, url) {
                this.posterUrl = poster
                this.plot = showname
            }
        }
        else
            if (url.contains("tata"))
            {
                val title ="TATA"
                val poster ="https://cdn.mos.cms.futurecdn.net/iYdoTcTScdApk3JV5GfEAT-1920-80.jpg"
                val showname ="TATA"

                return newMovieLoadResponse(title, url, TvType.Live, url) {
                    this.posterUrl = poster
                    this.plot = showname
                }
            }
            val document = app.get(url, headers = mapOf("User-Agent" to Useragent)).document
            val title =document.selectFirst("div.program-info > span.channel-name")?.text()?.trim().toString()
            val poster ="https://cdn.mos.cms.futurecdn.net/iYdoTcTScdApk3JV5GfEAT-1920-80.jpg"
            val showname =document.selectFirst("div.program-info > div.program-name")?.text()?.trim().toString()
            //val description = document.selectFirst("div.program-info > div.program-description")?.text()?.trim().toString()

            return newMovieLoadResponse(title, url, TvType.Live, url) {
                this.posterUrl = poster
                this.plot = showname
            }
    }

    // Define a nullable global variable to store globalArgument

   @SuppressLint("SuspiciousIndentation")
override suspend fun loadLinks(
    data: String,
    isCasting: Boolean,
    subtitleCallback: (SubtitleFile) -> Unit,
    callback: (ExtractorLink) -> Unit
): Boolean {
    Log.d("Phisher", data)
    when {
        data.contains("jiotv") -> {
            val channelID = data.substringAfter("=")
            val link = "https://madplay.live/hls/jiotv/stream.php?id=$channelID&e=.m3u8"
            callback.invoke(
                ExtractorLink(
                    source = "INDIAN TV",
                    name = "INDIAN TV",
                    url = link,
                    referer = "",
                    quality = Qualities.Unknown.value,
                    isM3u8 = true
                )
            )
        }
        data.contains("tata") -> {
            val channelID = data.substringAfter("=").substringBefore("&")
            val link = "https://madplay.live/hls/tata/stream.php?id=$channelID&e=.m3u8"
            callback.invoke(
                ExtractorLink(
                    source = "INDIAN TV",
                    name = "INDIAN TV",
                    url = link,
                    referer = "",
                    quality = Qualities.Unknown.value,
                    isM3u8 = true
                )
            )
        }
        data.contains("jwplayer.php?") -> {
            val link = data.substringAfter("jwplayer.php?")
            callback.invoke(
                ExtractorLink(
                    source = "INDIAN TV",
                    name = "INDIAN TV",
                    url = link,
                    referer = "",
                    quality = Qualities.Unknown.value,
                    type = INFER_TYPE
                )
            )
        }
    }
    return true
}
