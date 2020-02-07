package ${package}.${artifactId}.step_definitions

import ${package}.${artifactId}.pageobjects.WikipediaPage
import ${package}.${artifactId}.pageobjects.PageUrls


class NavigationSteps(testDataContainer: TestDataContainer) : AbstractStepDefs(testDataContainer) {

    init {
        Given("the start page is loaded") {
            getWebDriverSession().gotoUrl(PageUrls.HOME, WikipediaPage::class, testDataContainer)
        }

        When("{string} opens software testing") { userid: String ->
            getWebDriverSession(userid).gotoUrl(PageUrls.SOFTWARETEST, WikipediaPage::class, testDataContainer)
        }

    }

}
