package ${package}.stepdefinitions

import  ${package}.pageobjects.WikipediaContentPage
import  ${package}.pageobjects.WikipediaStartPage
import org.assertj.core.api.Assertions.assertThat

class WikipediaSteps(testDataContainer: TestDataContainer) : AbstractStepDefs(testDataContainer) {

    init {
        Then("the searchbar is visible") {
            assertThat(getPage(WikipediaStartPage::class).isSearchbarPresent()).isTrue()
        }

        When("the Selenium page is opened") {
            getPage(WikipediaStartPage::class).searchFor("Selenium")
        }

        Then("the header should be {string}") { expHeader: String ->
            assertThat(getPage(WikipediaContentPage::class).getHeader()).contains(expHeader)
        }
    }
}