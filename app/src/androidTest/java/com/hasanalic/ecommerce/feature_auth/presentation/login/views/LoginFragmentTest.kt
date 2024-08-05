package com.hasanalic.ecommerce.feature_auth.presentation.login.views

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.hasanalic.ecommerce.R
import com.hasanalic.ecommerce.feature_home.presentation.HomeActivity
import com.hasanalic.ecommerce.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class LoginFragmentTest {

    /*
        adb shell settings put global window_animation_scale 0
        adb shell settings put global transition_animation_scale 0
        adb shell settings put global animator_duration_scale 0
     */

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var intentsRule = IntentsRule()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testGuestLoginButtonNavigatesToHomeActivity() {
        launchFragmentInHiltContainer<LoginFragment>()

        onView(withId(R.id.buttonGuest)).perform(click())
        intended(hasComponent(HomeActivity::class.java.name))
    }
}