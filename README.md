# Pasti System Coding Challenge

## Getting Started

## Modules:

* Game List
* Game Details
* Locale

### Libraries & Tools Used

* Android Compose
* Test Unit (Junit, kluent, mockk, coroutines test)
* google dagger hilt
* Retrofit
* Kotlin coroutines

### Folder Structure
Here is the core folder structure which flutter provides.

Here is the folder structure we have been using in this project

lib/
|- api/
|- Model/
|- network/
|- DI/
|- repositories/
|- ui/
    - common
    - home
    - moviedetail
    - movielist
    - theme
|- utils/

Now, lets dive into the lib folder which has the main code for the application.

1- api - Contain API Service used by the app.
2- DI - Contains dependency config.
3- Model - Contain kotlin file used for raw response from API decoded to data class .
4- repositories — Contain repositories class injected API Service class for view models.
5- UI — Contains main module (views, components, states, view models).
6- network — Contain base data class.
7- utils — Contains helper class.
