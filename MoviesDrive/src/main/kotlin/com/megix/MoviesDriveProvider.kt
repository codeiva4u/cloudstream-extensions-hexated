package com.megix

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

open class MoviesDriveProvider : MainAPI() { // all providers must be an instance of MainAPI
    override var mainUrl = "https://moviesdrive.website"
    override var name = "MoviesDrive"
    override val hasMainPage = true
    override var lang = "hi"
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries
    )

    override val mainPage = mainPageOf(
        "$mainUrl/page/" to "Latest Releases Movies",
        "$mainUrl/category/hindi-dubbed/" to "Hindi Dubbed Movies",
        "$mainUrl/category/south/" to "South Movies",
        "$mainUrl/category/bollywood/" to "Bollywood Movies",
        "$mainUrl/category/amzn-prime-video/page/" to "Prime Video",
        "$mainUrl/category/netflix/page/" to "Netflix",
        "$mainUrl/category/hotstar/page/" to "Hotstar",
        
    )

    override suspend fun getMainPage(
        page: Int,
        request: MainPageRequest
    ): HomePageResponse {
        val document = app.get(request.data + page).document
        val home = document.select("ul.recent-movies > li").mapNotNull {
            it.toSearchResult()
        }
        return newHomePageResponse(request.name, home)
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val titleElement = this.selectFirst("figure > img")
        val trimTitle = titleElement?.attr("title")?.let {
            if (it.contains("Download ")) {
                it.replace("Download ", "")
            } else {
                it
            }
        } ?: ""

        val href = fixUrl(this.selectFirst("figure > a")?.attr("href").toString())
        val posterUrl = fixUrlNull(titleElement?.attr("src").toString())

        return newMovieSearchResponse(trimTitle, href, TvType.Movie) {
            this.posterUrl = posterUrl
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val searchResponse = mutableListOf<SearchResponse>()

        for (i in 1..3) {
            val document = app.get("$mainUrl/page/$i/?s=$query").document

            val results = document.select("ul.recent-movies > li").mapNotNull { it.toSearchResult() }

            if (results.isEmpty()) {
                break
            }
            searchResponse.addAll(results)
        }

        return searchResponse
    }

    override suspend fun load(url: String): LoadResponse? {
        val document = app.get(url).document
        val ogTitle = document.selectFirst("meta[property=og:title]")?.attr("content")
        val trimTitle = ogTitle?.let {
            if (it.contains("Download ")) {
                it.replace("Download ", "")
            } else {
                it
            }
        } ?: ""

        val posterUrl = document.selectFirst("img[decoding=\"async\"]")?.attr("src") ?: ""
        val seasonRegex = """(?i)season\s*\d+""".toRegex()
        val tvType = if (
            ogTitle?.contains("Episode", ignoreCase = true) == true ||
            seasonRegex.containsMatchIn(ogTitle ?: "") ||
            ogTitle?.contains("series", ignoreCase = true) == true
        ) {
            TvType.TvSeries
        } else {
            TvType.Movie
        }

        return if (tvType == TvType.TvSeries) {
            val tvSeriesEpisodes = mutableListOf<Episode>()
            val buttons = document.select("h5 > a")
                .filter { element -> !element.text().contains("Zip", true) }

            if (buttons.isNotEmpty()) {
                var seasonNum = 1
                buttons.forEach { button ->
                    val titleElement = button.parent()?.previousElementSibling()
                    val mainTitle = titleElement?.text() ?: ""
                    val episodeLink = button.attr("href") ?: ""

                    val doc = app.get(episodeLink).document

                    val elements = doc.select("span:matches((?i)(Ep))").ifEmpty {
                        doc.select("a:matches((?i)(HubCloud))")
                    }
                    val episodes = elements.mapIndexed { index, element ->
                        val episodeString = if (element.tagName() == "span") {
                            val titleTag = element.parent()
                            titleTag?.text() ?: ""
                            var linkTag = titleTag?.nextElementSibling()
                            var episodeString = ""
                            while (linkTag != null && linkTag.text().contains("HubCloud", ignoreCase = true)) {
                                episodeString += linkTag.toString()
                                linkTag = linkTag.nextElementSibling()
                            }
                            episodeString
                        } else {
                            element.toString()
                        }

                        Episode(
                            name = "$mainTitle",
                            data = episodeString,
                            season = seasonNum,
                            episode = index + 1
                        )
                    }
                    tvSeriesEpisodes.addAll(episodes)
                    seasonNum++
                }
                newTvSeriesLoadResponse(trimTitle, url, TvType.TvSeries, tvSeriesEpisodes) {
                    this.posterUrl = posterUrl
                }
            } else {
                val episodesList = document.select("p.p1").mapNotNull { pTag ->
                    val text = pTag.text() ?: ""
                    val nextTag = pTag.nextElementSibling()
                    val nextTagString = nextTag?.toString()
                    nextTagString?.let {
                        Episode(
                            name = text,
                            data = it,
                        )
                    }
                }
                newTvSeriesLoadResponse(trimTitle, url, TvType.TvSeries, episodesList) {
                    this.posterUrl = posterUrl
                }
            }
        } else {
            newMovieLoadResponse(trimTitle, url, TvType.Movie, url) {
                this.posterUrl = posterUrl
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        if (data.contains("graph.")) {
            val regex = Regex("""(?i)https?:\/\/[^\s"<]+""")
            val links = regex.findAll(data).mapNotNull { it.value }.toList()
            links.forEach { link ->
                val doc = app.get(link).document
                doc.select("h3 > a").forEach {
                    val src = it.attr("href")
                    loadExtractor(src, subtitleCallback, callback)
                }
            }
        } else if (data.contains("moviesdrive")) {
            val document = app.get(data).document
            document.select("h5 > a").forEach { button ->
                val link = button.attr("href")
                val doc = app.get(link).document
                doc.select("h5 > a").forEach { innerButton ->
                    val source = innerButton.attr("href")
                    loadExtractor(source, subtitleCallback, callback)
                }
            }
        } else {
            val hubCloudRegex = Regex("""(?i)https?:\/\/[^\s"<]+""")
            val hubCloudLinks = hubCloudRegex.findAll(data).mapNotNull { it.value }.toList()
            hubCloudLinks.forEach { link ->
                loadExtractor(link, subtitleCallback, callback)
            }
        }
        return true
    }
}
