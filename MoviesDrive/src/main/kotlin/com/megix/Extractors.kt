package com.megix

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.nodes.Element

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

        val gamerLink = if (newLink.contains("drive")) {
            val scriptTag = newDoc.selectFirst("script:containsData(url)")?.toString() ?: ""
            Regex("var url = '([^']*)'").find(scriptTag)?.groupValues?.get(1) ?: ""
        } else {
            newDoc.selectFirst("div.vd > center > a")?.attr("href") ?: ""
        }

        val document = app.get(gamerLink).document
        val size = document.selectFirst("i#size")?.text()
        val header = document.selectFirst("div.card-header")?.text()
        val div = document.selectFirst("div.card-body")

        div?.select("a")?.forEach {
            val link = it.attr("href")
            val text = it.text()

            when {
                link.contains("pixeldra") -> {
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
                text.contains("Download [Server : 10Gbps]") -> {
                    val response = app.get(link, allowRedirects = false)
                    val downloadLink = response.headers["location"]?.split("link=")?.getOrNull(1) ?: link
                    callback.invoke(
                        ExtractorLink(
                            "Hub-Cloud[Download]",
                            "Hub-Cloud[Download] $size",
                            downloadLink,
                            "",
                            getIndexQuality(header),
                        )
                    )
                }
                link.contains(".dev") -> {
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
                else -> {
                    loadExtractor(link, subtitleCallback, callback)
                }
            }
        }
    }

    private fun getIndexQuality(str: String?): Int {
        return Regex("(\\d{3,4})[pP]").find(str ?: "")?.groupValues?.getOrNull(1)?.toIntOrNull()
            ?: Qualities.Unknown.value
    }
}