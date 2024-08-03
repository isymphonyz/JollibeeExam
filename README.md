# Jollibee Exam Application
This application demonstrates a simple user authentication flow using email and password. It follows the MVVM and Clean Architecture principles, Koin for dependency injection, Jetpack Compose for the UI and unit tests. The project is organized into modular architecture principles with data, domain, feature, and core modules.

## Screenshot
<img src="https://github.com/user-attachments/assets/8d041836-e3f8-438c-ba4c-d79b99659e11" width="200"/>
<img src="https://github.com/user-attachments/assets/417cb17d-efc1-42ad-9f29-919b03e2783a" width="200"/>
<img src="https://github.com/user-attachments/assets/3e293c7c-f9c0-472c-995b-c4a0e6ed5551" width="200"/>
<img src="https://github.com/user-attachments/assets/aafa47c5-9ef1-432c-a486-f5c8390bee84" width="200"/>

## About
- Package name: `com.plearn.jollibeeexam`
- Min SDK: 24

## Project Structure
```
JollibeeExam/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/plearn/jollibeeexam/
│   │   │   ├── JollibeeExamApplication.kt
│   │   │   ├── MainActivity.kt
│   │   │   ├── navigation/
│   │   │   │   ├── AppNavigation.kt
├── core/
│   ├── src/main/java/com/plearn/core/
│   │   ├── di/
│   │   │   ├── CoreModule.kt
│   │   ├── utils/
│   │   │   ├── ResourceProvider.kt
│   │   │   ├── Result.kt
├── data/
│   ├── src/main/java/com/plearn/data/
│   │   ├── di/
│   │   │   ├── DataModule.kt
│   │   ├── repository/
│   │   │   ├── AuthRepositoryImpl.kt
├── domain/
│   ├── src/main/java/com/plearn/domain/
│   │   ├── model/
│   │   │   ├── User.kt
│   │   ├── repository/
│   │   │   ├── AuthRepository.kt
│   │   ├── usecase/
│   │   │   ├── LoginUseCase.kt
├── feature/
│   ├── src/main/java/com/plearn/feature/ui/
│   │   ├── di/
│   │   │   ├── FeatureModule.kt
│   │   ├── login/
│   │   │   ├── LoginScreen.kt
│   │   │   ├── LoginUIState.kt
│   │   │   ├── LoginViewModel.kt
│   │   ├── home/
│   │   │   ├── HomeScreen.kt
│   │   ├── utils/
│   │   │   ├── EmailValidator.kt
```

## Project Structure
- Android Studio
- Kotlin
- Koin
- Jetpack Compose

## Installation
1. Clone the repository
```
git clone https://github.com/isymphonyz/JollibeeExam.git
```
2. Open the project in Android Studio
3. Sync the project with Gradle files

## Setup Koin for Dependency Injection
JollibeeExamApplication.kt
```
class JollibeeExamApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@JollibeeExamApplication)
            modules(listOf(coreModule, dataModule, featureModule))
        }
    }
}
```

MainActivity.kt
```
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}
```

## Navigation
AppNavigation.kt
```
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { username ->
                    navController.navigate("home/$username") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "home/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            HomeScreen(
                username = username,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
```

## How to Use the Application
1. Launch the Application: When you open the app, you will see the LoginScreen where you can enter your email and password.
2. Enter Credentials:
- Email: Enter your email address. The app will validate the format of the email. If the email format is incorrect, an inline error message will be displayed.
- Password: Enter your password. You can toggle the visibility of the password by clicking the eye icon in the password field.
3. Login:
- Click the Login button.
- If the email and password are correct, you will be navigated to the HomeScreen displaying a welcome message with your username.
- If the credentials are incorrect, an error message will be displayed.
4. Logout:
- On the HomeScreen, you can click the Logout button to log out and return to the LoginScreen.
- After logging out, pressing the back button will terminate the application to prevent navigation back to the HomeScreen.

## Conditions for Login
1. Valid Email Format: The email field must contain a valid email address format. The app uses Android's Patterns.EMAIL_ADDRESS to validate the email format.
2. Correct Credentials: The email and password combination must match the predefined credentials in the AuthRepositoryImpl. The app checks if the entered email is "jlbusr@jollibee.com" and the password is "p@ssw0rd#1234". If both match, the login is successful.

## How Does It Work?
1. App Initialization: The app initializes Koin for dependency injection in the JollibeeExamApplication.
2. Navigation: The AppNavigation function manages the navigation between the LoginScreen and HomeScreen using Jetpack Navigation.
3. Login Flow:
- The LoginScreen composable displays the login form and handles user interactions.
- The LoginViewModel manages the login state and performs the login logic using the LoginUseCase.
- The LoginUseCase interacts with the AuthRepository to validate the credentials.
- If the login is successful, the user is navigated to the HomeScreen with their username.
- If the login fails, an error message is displayed.
4. Logout Flow:
- The HomeScreen composable displays a welcome message and a Logout button.
- Clicking the Logout button navigates the user back to the LoginScreen and resets the navigation stack.
- The back button behavior is overridden to terminate the application after logging out, ensuring the user cannot navigate back to the HomeScreen.

## Example Login Flow
1. Open the App: The LoginScreen is displayed.
2. Enter Email: jlbusr@jollibee.com
3. Enter Password: p@ssw0rd#1234
4. Click Login: The app navigates to the HomeScreen displaying "Welcome jlbusr@jollibee.com!!!"
5. Click Logout: The app navigates back to the LoginScreen.
6. Press Back Button: The application terminates.

## Example Invalid Login Flow
1. Open the App: The LoginScreen is displayed.
2. Enter Email: invalid@jollibee.com
3. Enter Password: somePassword
4. Click Login: An error message "Invalid credentials" is displayed.