package ${package}.stepdefinitions

import assertk.assertThat
import assertk.assertions.isTrue
import  ${package}.pageobjects.WikipediaPage

class WikipediaSteps(testDataContainer: TestDataContainer) : AbstractStepDefs(testDataContainer) {

    init {
        Then("the searchbar is visible") {
            assertThat(getPage(WikipediaPage::class).isSearchbarPresent(), "Searchbar is present").isTrue()

        }
    }
}
