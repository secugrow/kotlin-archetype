package ${package}.${artifactId}.step_definitions

import assertk.assertThat
import assertk.assertions.endsWith
import assertk.assertions.isEqualTo
import ${package}.${artifactId}.pageobjects.MainPage
import ${package}.${artifactId}.pageobjects.PageUrls

class SessionSteps(testDataContainer: TestDataContainer) : AbstractStepDefs(testDataContainer) {


    init {
        When("the start page for {string} is loaded") { sessionID: String ->
            getWebDriverSession(sessionID).gotoUrl(PageUrls.HOME, MainPage::class, testDataContainer)
        }

        When("{string} is activated") { sessionID: String ->
            getWebDriverSession(sessionID)
        }

        Then("{string} should be still on start page") { sessionID: String ->
            assertThat(getWebDriverSession(sessionID).webDriver.currentUrl).isEqualTo((testDataContainer.getTestData("baseurl") as String) + "/")
        }

        Then("the active session should contain {int} window") { expCount: Int ->
            assertThat(getWebDriverSession().webDriver.windowHandles.size).isEqualTo(expCount)
        }

    }
}
