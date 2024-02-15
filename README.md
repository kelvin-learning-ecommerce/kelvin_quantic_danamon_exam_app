# Pasti System Coding Challenge

## Getting Started

### Libraries & Tools Used

* Android Compose
* Test Unit (Junit, kluent, mockk, coroutines test)
* google dagger hilt
* Retrofit
* Kotlin coroutines

### Folder Structure
Here is the core folder structure which flutter provides.

Here is the folder structure we have been using in this project

https://kelvinprayitno94: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@github.com/99groupcodingchallenge/99group-flutter-coding-challenge.git
lib/
|- api/
|- di/
|- Model/
|- network/
|- repositories/
|- room/
    - daomodel
|- ui/
    - common
    - confirmpassword
    - login
    - photolist
    - register
    - splash
    - theme
|- utils/

Now, lets dive into the lib folder which has the main code for the application.

1- api - Contain API Service used by the app.
2- DI - Contains dependency config for retrofit & room module.
3- Model - Contain kotlin file used for raw response from API decoded to data class .
4- repositories — Contain repositories class injected API Service class for view models.
5- Room — Contains Room repository & DAO Model.
6- UI — Contains main module (views, components, states, view models).
    6.1- Common : common widget used by all pages
    6.2- Confirm Password : for confirm Admin password when deleting a user
    6.3- Login : for Login registered user
    6.4- Photo List : Containing list of photo fetch from API
    6.5- Register : For register a new user, and for editing a User when logged in as Admin
    6.6- Splash : Splash Screen activity
    6.7- Theme : Global Theme used by the app
7- network — Contain base data class.
8- utils — Contains helper class.
