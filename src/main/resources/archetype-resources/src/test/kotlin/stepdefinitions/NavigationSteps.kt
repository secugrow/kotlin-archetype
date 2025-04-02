package ${package}.stepdefinitions

import ${package}.pageobjects.WikipediaStartPage
import ${package}.pageobjects.PageUrls

class NavigationSteps(testDataContainer: TestDataContainer) : AbstractStepDefs(testDataContainer) {

    init {
        Given("the start page is loaded") {
            getWebDriverSession().gotoUrl(PageUrls.HOME, WikipediaStartPage::class, testDataContainer)
        }

        When("{string} opens software testing") { userid: String ->
            getWebDriverSession(userid).gotoUrl(PageUrls.SOFTWARETEST, WikipediaStartPage::class, testDataContainer)
        }

    }

}