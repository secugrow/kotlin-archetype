package at.co.boris.secuton.step_definitions

import at.co.boris.secuton.pageobjects.MainPage
import at.co.boris.secuton.pageobjects.PageUrls


class NavigationSteps(testDataContainer: TestDataContainer) : AbstractStepDefs(testDataContainer) {

    init {
        Given("the start page is loaded") {
            getWebDriverSession().gotoUrl(PageUrls.HOME, MainPage::class, testDataContainer)
        }

        When("the team site is opened") {
            getPage(MainPage::class).clickMenuEntry("Team")
        }
    }

}
