package ${package}.stepdefinitions

import  ${package}.pageobjects.WikipediaPage
import org.assertj.core.api.Assertions.assertThat

class WikipediaSteps(testDataContainer: TestDataContainer) : AbstractStepDefs(testDataContainer) {

    init {
        Then("the searchbar is visible") {
            assertThat(getPage(WikipediaStartPage::class).isSearchbarPresent()).isTrue()
        }

        When("the Selenium page is opened") {
            //missing
        }

        Then("the header should be {string}") { header: String ->
            TODO("Not yet implemented")
        }
    }
}
