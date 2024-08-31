package com.HindiProviders

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.network.WebViewResolver
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.extractors.*

class Ahvsh(private val ahvsh: Ahvsh) {
    var name: String
        get() = ahvsh.name
        set(value) {
            ahvsh.name = value
        }
    var requiresReferer: Boolean
        get() = ahvsh.requiresReferer
        set(value) {
            ahvsh.requiresReferer = value
        }
}

class Aico(private val aico: Aico) {
    var name: String
        get() = aico.name
        set(value) {
            aico.name = value
        }
    var requiresReferer: Boolean
        get() = aico.requiresReferer
        set(value) {
            aico.requiresReferer = value
        }
}

class Bestx(private val bestx: Bestx) {
    var name: String
        get() = bestx.name
        set(value) {
            bestx.name = value
        }
    var requiresReferer: Boolean
        get() = bestx.requiresReferer
        set(value) {
            bestx.requiresReferer = value
        }
}

class BullStream(private val bullStream: BullStream) {
    var name: String
        get() = bullStream.name
        set(value) {
            bullStream.name = value
        }
    var requiresReferer: Boolean
        get() = bullStream.requiresReferer
        set(value) {
            bullStream.requiresReferer = value
        }
}

class Cdnplayer(private val cdnplayer: Cdnplayer) {
    var name: String
        get() = cdnplayer.name
        set(value) {
            cdnplayer.name = value
        }
    var requiresReferer: Boolean
        get() = cdnplayer.requiresReferer
        set(value) {
            cdnplayer.requiresReferer = value
        }
}

class CdnwishCom(private val cdnwishCom: CdnwishCom) {
    var name: String
        get() = cdnwishCom.name
        set(value) {
            cdnwishCom.name = value
        }
    var requiresReferer: Boolean
        get() = cdnwishCom.requiresReferer
        set(value) {
            cdnwishCom.requiresReferer = value
        }
}

class Chillx(private val chillx: Chillx) {
    var name: String
        get() = chillx.name
        set(value) {
            chillx.name = value
        }
    var requiresReferer: Boolean
        get() = chillx.requiresReferer
        set(value) {
            chillx.requiresReferer = value
        }
}

class DBfilm(private val dBfilm: DBfilm) {
    var name: String
        get() = dBfilm.name
        set(value) {
            dBfilm.name = value
        }
    var requiresReferer: Boolean
        get() = dBfilm.requiresReferer
        set(value) {
            dBfilm.requiresReferer = value
        }
}

class DoodLaExtractor(private val doodLaExtractor: DoodLaExtractor) {
    var name: String
        get() = doodLaExtractor.name
        set(value) {
            doodLaExtractor.name = value
        }
    var requiresReferer: Boolean
        get() = doodLaExtractor.requiresReferer
        set(value) {
            doodLaExtractor.requiresReferer = value
        }
}

class FEmbed(private val fEmbed: FEmbed) {
    var name: String
        get() = fEmbed.name
        set(value) {
            fEmbed.name = value
        }
    var requiresReferer: Boolean
        get() = fEmbed.requiresReferer
        set(value) {
            fEmbed.requiresReferer = value
        }
}

class FileMoon(private val fileMoon: FileMoon) {
    var name: String
        get() = fileMoon.name
        set(value) {
            fileMoon.name = value
        }
    var requiresReferer: Boolean
        get() = fileMoon.requiresReferer
        set(value) {
            fileMoon.requiresReferer = value
        }
}

class GMPlayer(private val gMPlayer: GMPlayer) {
    var name: String
        get() = gMPlayer.name
        set(value) {
            gMPlayer.name = value
        }
    var requiresReferer: Boolean
        get() = gMPlayer.requiresReferer
        set(value) {
            gMPlayer.requiresReferer = value
        }
}

class Gdriveplayer(private val gdriveplayer: Gdriveplayer) {
    var name: String
        get() = gdriveplayer.name
        set(value) {
            gdriveplayer.name = value
        }
    var requiresReferer: Boolean
        get() = gdriveplayer.requiresReferer
        set(value) {
            gdriveplayer.requiresReferer = value
        }
}

class GenericM3U8(private val genericM3U8: GenericM3U8) {
    var name: String
        get() = genericM3U8.name
        set(value) {
            genericM3U8.name = value
        }
    var requiresReferer: Boolean
        get() = genericM3U8.requiresReferer
        set(value) {
            genericM3U8.requiresReferer = value
        }
}

class HDMomPlayer(private val hDMomPlayer: HDMomPlayer) {
    var name: String
        get() = hDMomPlayer.name
        set(value) {
            hDMomPlayer.name = value
        }
    var requiresReferer: Boolean
        get() = hDMomPlayer.requiresReferer
        set(value) {
            hDMomPlayer.requiresReferer = value
        }
}

class JWPlayer(private val jWPlayer: JWPlayer) {
    var name: String
        get() = jWPlayer.name
        set(value) {
            jWPlayer.name = value
        }
    var requiresReferer: Boolean
        get() = jWPlayer.requiresReferer
        set(value) {
            jWPlayer.requiresReferer = value
        }
}

class Minoplres(private val minoplres: Minoplres) {
    var name: String
        get() = minoplres.name
        set(value) {
            minoplres.name = value
        }
    var requiresReferer: Boolean
        get() = minoplres.requiresReferer
        set(value) {
            minoplres.requiresReferer = value
        }
}

class MixDrop(private val mixDrop: MixDrop) {
    var name: String
        get() = mixDrop.name
        set(value) {
            mixDrop.name = value
        }
    var requiresReferer: Boolean
        get() = mixDrop.requiresReferer
        set(value) {
            mixDrop.requiresReferer = value
        }
}

class StreamTape(private val streamTape: StreamTape) {
    var name: String
        get() = streamTape.name
        set(value) {
            streamTape.name = value
        }
    var requiresReferer: Boolean
        get() = streamTape.requiresReferer
        set(value) {
            streamTape.requiresReferer = value
        }
}

class VidSrcExtractor(private val vidSrcExtractor: VidSrcExtractor) {
    var name: String
        get() = vidSrcExtractor.name
        set(value) {
            vidSrcExtractor.name = value
        }
    var requiresReferer: Boolean
        get() = vidSrcExtractor.requiresReferer
        set(value) {
            vidSrcExtractor.requiresReferer = value
        }
}

class VidSrcExtractor2(private val vidSrcExtractor2: VidSrcExtractor2) {
    var name: String
        get() = vidSrcExtractor2.name
        set(value) {
            vidSrcExtractor2.name = value
        }
    var requiresReferer: Boolean
        get() = vidSrcExtractor2.requiresReferer
        set(value) {
            vidSrcExtractor2.requiresReferer = value
        }
}

class VidSrcTo(private val vidSrcTo: VidSrcTo) {
    var name: String
        get() = vidSrcTo.name
        set(value) {
            vidSrcTo.name = value
        }
    var requiresReferer: Boolean
        get() = vidSrcTo.requiresReferer
        set(value) {
            vidSrcTo.requiresReferer = value
        }
}

class XStreamCdn(private val xStreamCdn: XStreamCdn) {
    var name: String
        get() = xStreamCdn.name
        set(value) {
            xStreamCdn.name = value
        }
    var requiresReferer: Boolean
        get() = xStreamCdn.requiresReferer
        set(value) {
            xStreamCdn.requiresReferer = value
        }
}

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
