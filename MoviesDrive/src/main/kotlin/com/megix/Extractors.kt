package com.megix

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*

class HubCloud : ExtractorApi() {
    override val name: String = "Hub-Cloud"
    override val mainUrl: String = "https://hubcloud.lol"
    override val requiresReferer = false

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val doc = app.get(url).text
        val newLink = doc.substringAfter("url=").substringBefore("\"")
        val newDoc = app.get(newLink).document
        var gamerLink: String

        if(newLink.contains("drive")) {
            val scriptTag = newDoc.selectFirst("script:containsData(url)")?.toString()
            gamerLink = scriptTag?.let { Regex("var url = '([^']*)'").find(it) ?. groupValues ?. get(1) }
                ?: ""
        }

        else {
            gamerLink = newDoc.selectFirst("div.vd > center > a") ?. attr("href") ?: ""
        }
        
        val document = app.get(gamerLink).document

        val size = document.selectFirst("i#size") ?. text()
        val div = document.selectFirst("div.card-body")
        val header = document.selectFirst("div.card-header") ?. text()
        if (div != null) {
            div.select("a").apmap {
                val link = it.attr("href")
                if (link.contains("pixeldra")) {
                    callback.invoke(
                        ExtractorLink(
                            "Pixeldrain",
                            "Pixeldrain $size",
                            link,
                            "",
                            getIndexQuality(header),
                        )
                    )
                }
                else if(link.contains("dl.php")) {
                    val downloadPage = app.get(link).document
                    val downloadLink = downloadPage.selectFirst("a#vd")?.attr("href")
                    downloadLink?.let { it1 ->
                        ExtractorLink(
                            "Hub-Cloud[Download]",
                            "Hub-Cloud[Download] $size",
                            it1,
                            "",
                            getIndexQuality(header),
                        )
                    }?.let { it2 ->
                        callback.invoke(
                            it2
                        )
                    }
                }
                else if(link.contains(".dev")) {
                    callback.invoke(
                        ExtractorLink(
                            "Hub-Cloud",
                            "Hub-Cloud $size",
                            link,
                            "",
                            getIndexQuality(header),
                        )
                    )
                }
                else {
                    //loadExtractor(link, subtitleCallback, callback)
                }
            }
        }
    }


    private fun getIndexQuality(str: String?): Int {
        return Regex("(\\d{3,4})[pP]").find(str ?: "") ?. groupValues ?. getOrNull(1) ?. toIntOrNull()
            ?: Qualities.Unknown.value
    }

}
