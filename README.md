# LocationTracker 📍

## 📱 About The Project

Location Tracker is an Android app that tracks the user's real-time movements and displays them on a map. This app can even track the user's route in the background and record location points at specific distance intervals.

## 🌟 Features

- The application tracks the user's location in the foreground and background
- Location points are automatically recorded after every 100 meters of movement
- User movements are shown with markers on the map
- Address information is displayed for each recorded marker
- Route information is stored, and the current route is secured when the application is closed and opened
- All location data can be cleared

## 🏗 Architecture
MVVM is the architectural pattern used to structure the app's user interface and business logic. It separates the app into three main components:

1. Model: Represents the data and business logic of the application. It handles data retrieval from the Spaceflight News APIs.
2. View: Represents the user interface. It observes changes from the ViewModel and updates the UI accordingly.
3. ViewModel: Acts as a mediator between the Model and the View. It holds and processes data, as well as handles user interactions. It also exposes data to the View via Kotlin Flows.

Clean Architecture emphasizes the separation of concerns by dividing the app into layers, each with its specific responsibilities:

1. Presentation Layer: This layer contains the MVVM components (View and ViewModel) and is responsible for rendering the user interface and handling user interactions.
2. Domain Layer: Contains the business logic and use cases. It is independent of the Presentation Layer and communicates through interfaces or contracts.
3. Data Layer: Handles data retrieval and storage. It communicates with the Domain Layer through interfaces, abstracting the data sources.
   
## 🛠 Built With

- Kotlin
- Android Jetpack Components
  - ViewModel
  - Compose UI
  - Coroutines
  - Flows
- Dependency Injection - Hilt
- Google Maps API
- Fused Location Provider
- Geocoding API
- Retrofit for API calls
- Room Database
- Foreground Service
- Notification Channel
- Material Design 3

## 🏃‍♂️ Getting Started

### Prerequisites

- Latest version of Android Studio
- JDK 11
- Android min SDK 24
- Android target SDK 35
- Gradle 8.10.2

### Installation

1. Clone the repository
   ```bash
   git clone https://github.com/MertTalas/LocationTracker.git
2. Open project in Android Studio
3. Add MAPS_API_KEY to local.properties in Gradle Scripts
4. Sync project with Gradle files
5. Run the app on an emulator or physical device
6. Allow all the required permissions
7. Enjoy 🎉
