package com.kelvin.pastisystem

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kelvinquantic.danamon.ui.login.LoginPageScreen
import com.kelvinquantic.danamon.ui.login.viewmodel.LoginViewModel
import com.kelvinquantic.danamon.ui.theme.DanamonAppTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeUITest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    // use createAndroidComposeRule<YourActivity>() if you need access to
    // an activity
    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        hiltRule.inject()

        composeTestRule.setContent {
            DanamonAppTheme {
                LoginPageScreen()
            }
        }
    }

    @Test
    fun myUIComponentTest() = runTest {

        composeTestRule.onNodeWithText("Login").performClick()

    }
}
