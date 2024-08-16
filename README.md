# E-Commerce App

This project is an example e-commerce mobile application developed following Clean Architecture principles and using the MVVM (Model-View-ViewModel) architecture.

## Features
* **Architecture**: Developed according to Clean Architecture principles and the MVVM (Model-View-ViewModel) architecture.
* **User Management**: Supports user login and registration features.
* **Product Management**: Allows users to add products to the cart and to favorites.
* **Search and Filtering**: Users can search for products using barcodes and filter products based on various criteria.
* **Cart and Discounts**: Includes shopping cart functionality and discount notifications.
* **Address Management**: Users can add and manage their addresses.
* **Purchase**: Enables users to purchase products.
* **Order History**: Provides users with the ability to view their order history.

## Notes
* Unit and UI tests are currently being written.

## Libraries
* [Foundation]
  * [Android KTX](https://developer.android.com/kotlin/ktx) - It is used to make the application more readable and easier to use.
  * [AppCompat](https://developer.android.com/jetpack/androidx/releases/appcompat) - It is a library that ensures Android applications are compatible with material design.
* [Architecture]
  * [Room](https://developer.android.com/jetpack/androidx/releases/room) - It is a library used to create and manage local databases.
  * [Lifecycles](https://developer.android.com/topic/libraries/architecture/lifecycle) - It is used to facilitate lifecycle management of activities and fragments.
  * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - It is used to manage and monitor data flow.
  * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - It is used for storing data and sharing data between UI components.
* [UI]
  * [Fragment](https://developer.android.com/guide/fragments) - It is used as a reusable UI component.
  * [Layout](https://developer.android.com/develop/ui/views/layout/declaring-layout) - Widgets are used to design the user interface.
* [Testing]
  * [JUnit](https://junit.org/junit4/javadoc/latest/) - It is used for unit testing.
  * [Mockito](https://site.mockito.org/) - It is used for mocking objects in tests.
  * [Espresso](https://developer.android.com/training/testing/espresso) - It is used for UI testing.
* [Third Party]
  * [Glide](https://github.com/bumptech/glide) - It is used for image loading and caching.
  * [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - It is used for asynchronous operations.
  * [Dagger-Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - It is used to perform Dependency Injection.
  * [SharedPreferences](https://developer.android.com/reference/android/content/SharedPreferences) - It is used to save small data.
  * [Barcode Scanner](https://github.com/yuriy-budiyev/code-scanner) - It is used to scan barcodes.
  * [Material Components](https://material.io/develop/android) - It is used for designing modern UI components.
  * [Splash Screen](https://developer.android.com/guide/topics/ui/splash-screen) - It is used to create splash screens.
  * [Location Services](https://developers.google.com/android/guides/location) - It is used for location-related operations.


## Setup
Make sure to have the following pre-requisites installed:
1. Java 17
2. Android Studio
3. Android 7.0+ Phone or Emulation setup

Fork and clone this repository and import into Android Studio
```bash
git clone https://github.com/hasanalic/E-Commerce
```

Use one of the Android Studio builds to install and run the app in your device or a simulator.


## Screenshots
| Login Page | Home Page | Shopping Cart Page |
|-------------------|-------------------|-------------------|
| <img src="https://github.com/user-attachments/assets/9e2e35c7-f8c3-486a-bbec-95ed5499a59c" width=250> | <img src="https://github.com/user-attachments/assets/206a1341-2a48-44b0-a872-d0039207fc6a" width=250> | <img src="https://github.com/user-attachments/assets/d580e2c7-5b7a-46ac-9fb7-629c3a2290a3" width=250> |

| Orders Page | Filter Page | Product Detail Page |
|-------------------|-------------------|-------------------|
| <img src="https://github.com/user-attachments/assets/158bb6b7-925a-48ed-9cf3-ae4e84f08943" width=250> | <img src="https://github.com/user-attachments/assets/ede1b010-3a25-4905-a012-4ffdbb1d6a69" width=250> | <img src="https://github.com/user-attachments/assets/085e2558-1232-40eb-aeca-49aaf7a5e6e7" width=250> |

## Designs
You can access the figma design [here](https://www.figma.com/design/EnAJztl6dgFd6U8piN9EjZ/E-Commerce).

![design](https://github.com/user-attachments/assets/0222f236-0f63-45b1-bb75-10eff5f0d975)
