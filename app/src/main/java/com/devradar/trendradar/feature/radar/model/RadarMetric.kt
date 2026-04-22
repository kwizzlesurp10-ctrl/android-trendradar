package com.devradar.trendradar.feature.radar.model
data class RadarMetric(val label: String, val value: Float, val target: Float? = null, val peer: Float? = null)
