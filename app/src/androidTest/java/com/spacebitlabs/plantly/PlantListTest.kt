package com.spacebitlabs.plantly

import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlantListTest {

    @Test
    fun tappingFabOpensAddPlant() {
        launch(NavigationActivity::class.java)
        PlantListRobot()
            .tapFab()
            .addPlantIsShown()
    }

    class PlantListRobot {
        fun tapFab(): AddPlantRobot.Assertions {
            onView(withId(R.id.add_plant)).perform(click())

            return AddPlantRobot.Assertions()
        }
    }

    class AddPlantRobot {
        class Assertions {
            fun addPlantIsShown() {
                onView(withId(R.id.save)).check(matches(not(ViewMatchers.isDisplayed())))
            }
        }
    }
}
