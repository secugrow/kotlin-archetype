package ${groupId}

import ${package}.pageobjects.MainPage
import ${package}.pageobjects.PageUrls


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
