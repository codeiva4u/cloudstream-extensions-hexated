package com.megix

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import java.net.URI
import okhttp3.FormBody

class Sharepoint : ExtractorApi() {
    override val name: String = "Sharepoint"
    override val mainUrl: String = "https://indjatin-my.sharepoint.com"
    override val requiresReferer = false

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        callback.invoke(
            ExtractorLink(
                this.name,
                this.name,
                url,
                referer = "",
                quality = Qualities.Unknown.value
            )
        )
    }
}

class GDFlix : ExtractorApi() {
    override val name: String = "GDFlix"
    override val mainUrl: String = "https://new2.gdflix.cfd"
    override val requiresReferer = false

    private suspend fun extractBollyTag(url: String): String {
        val tagDoc = app.get(url).text
        return """\b\d{3,4}p\b""".toRegex().find(tagDoc)?.value?.trim() ?: ""
    }

    private suspend fun extractBollyTag2(url: String): String {
        val tagDoc = app.get(url).text
        return """\b\d{3,4}p\b\s(.*?)\[""".toRegex().find(tagDoc)?.groupValues?.get(1)?.trim() ?: ""
    }

    private fun getBaseUrl(url: String): String {
        return URI(url).let {
            "${it.scheme}://${it.host}"
        }
    }

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        var currentUrl = url
        val tags = extractBollyTag(currentUrl)
        val tagQuality = extractBollyTag2(currentUrl)
        if (currentUrl.startsWith("https://new2.gdflix.cfd/goto/token/")) {
            val partialUrl = app.get(currentUrl).text.substringAfter("replace(\"").substringBefore("\")")
            currentUrl = mainUrl + partialUrl
        }

        app.get(currentUrl).document.select("div.text-center a").mapNotNull {
            when {
                it.select("a").text().contains("FAST CLOUD DOWNLOAD") -> {
                    val link = it.attr("href")
                    val trueUrl = app.get("https://new2.gdflix.cfd$link", timeout = 40L).document.selectFirst("a.btn-success")?.attr("href") ?: ""
                    callback.invoke(
                        ExtractorLink(
                            "GDFlix[Fast Cloud]", "GDFLix[Fast Cloud] $tagQuality", trueUrl, "", getQualityFromName(tags)
                        )
                    )
                }
                it.select("a").text().contains("DRIVEBOT DOWNLOAD") -> {
                    val driveLink = it.attr("href")
                    val id = driveLink.substringAfter("id=").substringBefore("&")
                    val doId = driveLink.substringAfter("do=").substringBefore("==")
                    handleDriveBotDownload(id, doId, tags, tagQuality, callback)
                }
                it.select("a").text().contains("Instant Download") -> {
                    val instantLink = it.attr("href")
                    val token = instantLink.substringAfter("url=")
                    val domain = getBaseUrl(instantLink)
                    val downloadLink = app.post(
                        url = "$domain/api",
                        data = mapOf("keys" to token),
                        referer = instantLink,
                        headers = mapOf(
                            "x-token" to "direct.zencloud.lol",
                            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:125.0) Gecko/20100101 Firefox/125.0"
                        ),
                        timeout = 100L
                    ).toString()
                    val finalDownloadLink = downloadLink.substringAfter("url\":\"").substringBefore("\",\"name").replace("\\/", "/")
                    callback.invoke(
                        ExtractorLink(
                            "GDFlix[Instant Download]", "GDFlix[Instant Download] $tagQuality", finalDownloadLink, "", getQualityFromName(tags)
                        )
                    )
                }
            }
        }
    }

    private suspend fun handleDriveBotDownload(id: String, doId: String, tags: String, tagQuality: String, callback: (ExtractorLink) -> Unit) {
        val indexBotLink = "https://indexbot.lol/download?id=${id}&do=${doId}"
        handleIndexBot(indexBotLink, tags, tagQuality, callback)

        val driveBotLink = "https://drivebot.cfd/download?id=${id}&do=${doId}"
        handleDriveBot(driveBotLink, tags, tagQuality, callback)
    }

    private suspend fun handleIndexBot(indexBotLink: String, tags: String, tagQuality: String, callback: (ExtractorLink) -> Unit) {
        val indexBotResponse = app.get(indexBotLink, timeout = 60L)
        if (indexBotResponse.isSuccessful) {
            val cookiesSSID = indexBotResponse.cookies["PHPSESSID"]
            val indexBotDoc = indexBotResponse.document
            val token = Regex("""formData\.append\('token', '([a-f0-9]+)'\)""").find(indexBotDoc.toString())?.groupValues?.get(1) ?: "token"
            val postId = Regex("""fetch\('\/download\?id=([a-zA-Z0-9\/+]+)'""").find(indexBotDoc.toString())?.groupValues?.get(1) ?: "postId"

            val requestBody = FormBody.Builder()
                .add("token", token)
                .build()

            val headers = mapOf("Referer" to indexBotLink)
            val cookies = mapOf("PHPSESSID" to "$cookiesSSID")

            val response = app.post(
                "https://indexbot.lol/download?id=${postId}",
                requestBody = requestBody,
                headers = headers,
                cookies = cookies,
                timeout = 60L
            ).toString()

            var downloadLink = Regex("url\":\"(.*?)\"").find(response)?.groupValues?.get(1) ?: ""
            downloadLink = downloadLink.replace("\\", "")

            callback.invoke(
                ExtractorLink(
                    "IndexBot", "IndexBot $tagQuality", downloadLink, "https://indexbot.lol/", getQualityFromName(tags)
                )
            )
        }
    }

    private suspend fun handleDriveBot(driveBotLink: String, tags: String, tagQuality: String, callback: (ExtractorLink) -> Unit) {
        val driveBotResponse = app.get(driveBotLink, timeout = 60L)
        if (driveBotResponse.isSuccessful) {
            val cookiesSSID = driveBotResponse.cookies["PHPSESSID"]
            val driveBotDoc = driveBotResponse.document
            val token = Regex("""formData\.append\('token', '([a-f0-9]+)'\)""").find(driveBotDoc.toString())?.groupValues?.get(1) ?: "token"
            val postId = Regex("""fetch\('\/download\?id=([a-zA-Z0-9\/+]+)'""").find(driveBotDoc.toString())?.groupValues?.get(1) ?: "postId"

            val requestBody = FormBody.Builder()
                .add("token", token)
                .build()

            val headers = mapOf("Referer" to driveBotLink)
            val cookies = mapOf("PHPSESSID" to "$cookiesSSID")

            val response = app.post(
                "https://drivebot.cfd/download?id=${postId}",
                requestBody = requestBody,
                headers = headers,
                cookies = cookies,
                timeout = 60L
            ).toString()

            var downloadLink = Regex("url\":\"(.*?)\"").find(response)?.groupValues?.get(1) ?: ""
            downloadLink = downloadLink.replace("\\", "")

            callback.invoke(
                ExtractorLink(
                    "DriveBot", "DriveBot $tagQuality", downloadLink, "https://drivebot.cfd/", getQualityFromName(tags)
                )
            )
        }
    }
}

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
            val scriptTag = newDoc.selectFirst("script:containsData(url)")?.toString()
            scriptTag?.let { Regex("var url = '([^']*)'").find(it)?.groupValues?.get(1) } ?: ""
        } else {
            newDoc.selectFirst("div.vd > center > a")?.attr("href") ?: ""
        }

        val document = app.get(gamerLink).document

        val size = document.selectFirst("i#size")?.text()
        val div = document.selectFirst("div.card-body")
        val header = document.selectFirst("div.card-header")?.text()
        div?.select("a")?.apmap {
            val link = it.attr("href")
            when {
                link.contains("pixeldra") -> {
                    callback.invoke(
                        ExtractorLink(
                            "Pixeldrain", "Pixeldrain $size", link, "", getIndexQuality(header)
                        )
                    )
                }
                link.contains("dl.php") -> {
                    callback.invoke(
                        ExtractorLink(
                            "Hub-Cloud[Download]", "Hub-Cloud[Download] $size", link, "", getIndexQuality(header)
                        )
                    )
                }
                link.contains(".dev") -> {
                    callback.invoke(
                        ExtractorLink(
                            "Hub-Cloud", "Hub-Cloud $size", link, "", getIndexQuality(header)
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
        return Regex("(\\d{3,4})[pP]").find(str ?: "")?.groupValues?.getOrNull(1)?.toIntOrNull() ?: Qualities.Unknown.value
    }
}