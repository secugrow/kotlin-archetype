package ${package}.${artifactId}.step_definitions

import ${package}.${artifactId}.pageobjects.MainPage
import ${package}.${artifactId}.pageobjects.PageUrls


class NavigationSteps(testDataContainer: TestDataContainer) : AbstractStepDefs(testDataContainer) {

    init {
        Given("the start page is loaded") {
            getWebDriverSession().gotoUrl(PageUrls.HOME, MainPage::class, testDataContainer)
        }

    }

}
