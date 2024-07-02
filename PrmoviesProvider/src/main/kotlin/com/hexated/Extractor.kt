package com.hexated


import com.lagradost.cloudstream3.extractors.Minoplres

open class Minoplres : Minoplres() {

    override val name = "Minoplres" // formerly SpeedoStream
    override val requiresReferer = true
    override val mainUrl = "https://minoplres.xyz" // formerly speedostream.bond
    private val hostUrl = "https://minoplres.xyz"
}
