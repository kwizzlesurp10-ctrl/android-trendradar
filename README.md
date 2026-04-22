# Android TrendRadar

A production-ready Android analytics dashboard for developers and app publishers.

## Features
- Radar Dashboard with animated spider/radar chart
- My Apps Hub (Google Sign-In + Play Console sync)
- Ecosystem Trends feed
- AI Insights powered by Gemini API
- Competitor Radar with multi-app comparison
- Alerts & Reports
- Trending Feed

## Tech Stack
- Kotlin 2.0+, Jetpack Compose + Material 3
- Clean Architecture + MVVM + Hilt DI
- Room, DataStore, Retrofit, OkHttp
- Firebase (Auth, Firestore, FCM, Analytics, Crashlytics)
- Gemini API, Play Developer Reporting API
- Play Billing v7+

## Setup
1. Clone/run this script: `bash setup_trendradar.sh`
2. Open project in Android Studio Narwhal (2025.1+)
3. Add `google-services.json` to `app/` folder (from Firebase Console)
4. Set API keys in `local.properties`:
   ```
   GEMINI_API_KEY=your_key_here
   PLAY_API_KEY=your_key_here
   ```
5. Sync Gradle and Run

## Package Structure
```
com.devradar.trendradar
├── core/           # Data layer (network, storage, repository, DI)
│   ├── di/
│   ├── network/
│   ├── storage/
│   ├── repository/
│   └── notifications/
└── ui/             # Presentation layer
    ├── theme/
    ├── components/
    ├── navigation/
    ├── dashboard/
    ├── myapps/
    ├── trends/
    ├── insights/
    ├── competitor/
    ├── alerts/
    └── feed/
```
