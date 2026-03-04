# StockStream -- Real-Time Stock Price Tracker

StockStream is a **Jetpack Compose Android application** that displays
**real-time stock price updates** using a WebSocket connection.\
The app follows **Clean Architecture with MVVM**, uses **Kotlin
Coroutines + StateFlow**, and demonstrates scalable patterns used in
production mobile applications.

This project was built as part of a **technical assessment** to
demonstrate architecture, real-time data handling, testing, and modern
Android development practices.

------------------------------------------------------------------------

# Features

• Real-time stock price updates using **WebSocket**\
• Displays **multiple stock symbols simultaneously**\
• Live **price change indicators (↑ ↓)**\
• Search and filter stocks by symbol\
• Stock **details screen with live updates**\
• Deep linking support for stock symbols\
• Clean Architecture separation (Data / Domain / Presentation)\
• Unit tests for ViewModel and Repository\
• GitHub Actions CI for automated tests

------------------------------------------------------------------------

# Tech Stack

Technology               Usage
  ------------------------ ---------------------------------
**Kotlin**               Primary language
**Jetpack Compose**      UI framework
**Coroutines + Flow**    Async and reactive data streams
**StateFlow**            UI state management
**Hilt (DI)**            Dependency injection
**OkHttp WebSocket**     Real-time data streaming
**Navigation Compose**   App navigation
**Turbine**              Flow testing
**JUnit**                Unit testing
**GitHub Actions**       CI pipeline

------------------------------------------------------------------------

# Architecture

The project follows **Clean Architecture with MVVM**.

    presentation
    │
    ├── screens
    │   ├── feed
    │   └── details
    │
    ├── navigation
    │
    └── viewmodels

    domain
    │
    ├── model
    ├── repository
    └── usecase

    data
    │
    ├── repository
    ├── websocket
    └── mapper

### Presentation Layer

Responsible for UI and user interaction.

Components: - Jetpack Compose screens - ViewModels - Navigation - UI
state

ViewModels expose state using:

    StateFlow<UiState>

------------------------------------------------------------------------

### Domain Layer

Contains **business logic and abstractions**.

Components:

    Repository Interfaces
    Use Cases
    Domain Models

Example use cases:

    StartPriceFeed
    StopPriceFeed
    ObserveQuotes
    ObserveConnectionStatus
    ObserveQuoteForSymbol

Use cases allow the presentation layer to remain independent of the data
layer.

------------------------------------------------------------------------

### Data Layer

Responsible for **data sources and repositories**.

Components:

    WebSocket Client
    Repository Implementation
    DTO / Mapping

The repository:

• listens to WebSocket messages\
• converts raw messages into domain models\
• maintains the latest quotes using `StateFlow`

------------------------------------------------------------------------

# WebSocket Flow

    WebSocket Server
            │
            ▼
    StocksWebSocketClient
            │
            ▼
    PriceFeedRepository
            │
            ▼
    UseCases
            │
            ▼
    ViewModel
            │
            ▼
    Jetpack Compose UI

------------------------------------------------------------------------

# UI

### Feed Screen

Displays:

• Search field\
• Live connection status\
• Real-time stock price list\
• Price change indicators

Updates are pushed every **2 seconds**.

------------------------------------------------------------------------

### Details Screen

Displays:

• Current stock price\
• Price change indicator\
• Additional metadata\
• Live updates for selected symbol

------------------------------------------------------------------------

# Deep Linking

The app supports deep links to directly open stock details.

Example:

    stocks://symbol/NVDA

Test using adb:

    adb shell am start -a android.intent.action.VIEW -d "stocks://symbol/NVDA"

------------------------------------------------------------------------

# Testing

The project includes unit tests for:

### ViewModel

Tests include:

• filtering stocks by symbol\
• sorting stocks by price\
• starting and stopping the price feed

Tools used:

    JUnit
    Turbine
    Coroutine Test

------------------------------------------------------------------------

### Repository

Tests verify:

• WebSocket message processing\
• quote updates\
• previous price calculation

------------------------------------------------------------------------

# CI Pipeline

GitHub Actions automatically runs:

    ./gradlew testDebugUnitTest

on every push and pull request.

Workflow file:

    .github/workflows/android-ci.yml

------------------------------------------------------------------------

# Running the Project

1️⃣ Clone the repository

    git clone https://github.com/yourusername/StockStream.git

2️⃣ Open in Android Studio

3️⃣ Run the app on emulator or device

------------------------------------------------------------------------

# Project Highlights

This project demonstrates:

• scalable **Clean Architecture**\
• **reactive UI with StateFlow**\
• **real-time streaming using WebSockets**\
• proper **dependency injection with Hilt**\
• production-level **architecture decisions**\
• testable architecture

------------------------------------------------------------------------

# Possible Improvements

Future enhancements could include:

• persistent watchlist\
• stock charts\
• error retry handling\
• pagination for larger symbol sets\
• UI animations for price changes

------------------------------------------------------------------------

# Author

**Sikander Bakht**

Staff Mobile Engineer\
Android • React Native • Fintech Systems
