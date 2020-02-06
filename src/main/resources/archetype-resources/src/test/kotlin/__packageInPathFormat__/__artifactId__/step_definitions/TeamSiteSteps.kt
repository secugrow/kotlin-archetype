package ${groupId}

import assertk.assertThat
import assertk.assertions.contains
import ${package}.pageobjects.TeamPage


class TeamSiteSteps(testDataContainer: TestDataContainer) : AbstractStepDefs(testDataContainer) {

    init {
        Then("should {string} be part of the Core team") { name: String ->
            assertThat(getPage(TeamPage::class).getTeamMembers(), "Searching for $name in Core Team list").contains(name)
        }
    }

}
