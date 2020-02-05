package at.co.boris.secuton.step_definitions

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import at.co.boris.secuton.pageobjects.MainPage


class MainPageSteps(testDataContainer: TestDataContainer) : AbstractStepDefs(testDataContainer) {

    init {
        Then("the peso logo should be displayed") {

            val eleLogoList = getPage(MainPage::class).getLogo()


            assertThat(eleLogoList!!.size, "found Logos via css").isGreaterThan(0)
            assertThat(eleLogoList[0].getAttribute("alt"), "checked the alternative Text from Logo").isEqualTo("PESO - Logo")
        }
    }
}