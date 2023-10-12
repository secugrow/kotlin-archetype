package ${package}.stepdefinitions

import  ${package}.pageobjects.WikipediaPage
import org.assertj.core.api.Assertions.assertThat

class WikipediaSteps(testDataContainer: TestDataContainer) : AbstractStepDefs(testDataContainer) {

    init {
        Then("the searchbar is visible") {
            assertThat(getPage(WikipediaPage::class).isSearchbarPresent()).isTrue()

        }
    }
}
