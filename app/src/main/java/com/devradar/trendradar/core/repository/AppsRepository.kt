package com.devradar.trendradar.core.repository
import com.devradar.trendradar.core.network.PlayReportingApiService
import com.google.firebase.firestore.FirebaseFirestore
class AppsRepository(val api: PlayReportingApiService, val fs: FirebaseFirestore)
