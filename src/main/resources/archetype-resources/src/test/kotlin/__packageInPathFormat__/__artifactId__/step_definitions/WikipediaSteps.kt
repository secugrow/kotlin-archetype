package ${package}.${artifactId}.step_definitions

import assertk.assertThat
import assertk.assertions.isTrue
import  ${package}.${artifactId}.pageobjects.WikipediaPage

class WikipediaSteps(testDataContainer: TestDataContainer) : AbstractStepDefs(testDataContainer) {

    init {
        Then("the searchbar is visible") {
            assertThat(getPage(WikipediaPage::class).isSearchbarPresent(), "Searchbar is not present").isTrue()

        }
    }
}