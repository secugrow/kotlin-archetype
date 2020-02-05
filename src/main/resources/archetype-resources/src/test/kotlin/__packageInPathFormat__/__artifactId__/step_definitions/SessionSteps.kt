package at.co.boris.secuton.step_definitions

import assertk.assertThat
import assertk.assertions.endsWith
import assertk.assertions.isEqualTo
import at.co.boris.secuton.pageobjects.MainPage
import at.co.boris.secuton.pageobjects.PageUrls
import at.co.boris.secuton.pageobjects.TeamPage

class SessionSteps(testDataContainer: TestDataContainer) : AbstractStepDefs(testDataContainer) {


    init {
        When("the start page for {string} is loaded") { sessionID: String ->
            getWebDriverSession(sessionID).gotoUrl(PageUrls.HOME, MainPage::class, testDataContainer)
        }

        When("{string} is activated") { sessionID: String ->
            getWebDriverSession(sessionID)
        }
        When("{string} opens the team site") { sessionID: String ->
            getWebDriverSession(sessionID).gotoUrl(PageUrls.TEAM,TeamPage::class, testDataContainer)
        }
        Then("{string} should be still on start page") { sessionID: String ->
            assertThat(getWebDriverSession(sessionID).webDriver.currentUrl).isEqualTo((testDataContainer.getTestData("baseurl") as String) + "/")
        }

        Then("the active session should contain {int} window") { expCount: Int ->
            assertThat(getWebDriverSession().webDriver.windowHandles.size).isEqualTo(expCount)
        }

        And("{string} should be still on team site") { sessionID: String ->
            assertThat(getWebDriverSession(sessionID).webDriver.currentUrl).endsWith(PageUrls.TEAM.subUrl + "/")
        }
    }
}