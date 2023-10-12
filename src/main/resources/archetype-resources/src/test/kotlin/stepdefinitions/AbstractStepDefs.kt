#set( $doublequote = '"' )
#set( $bracketOpen = '(' )
#set( $bracketClose = ')' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
#set( $dollar = '$' )

package ${package}.stepdefinitions

import assertk.fail
import ${package}.driverutil.PageNotFoundException
import ${package}.driverutil.WebDriverSession
import ${package}.driverutil.WebDriverSessionStore
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.remote.RemoteWebDriver
import ${package}.pageobjects.AbstractPage
import io.cucumber.java8.En
import io.cucumber.java.Scenario
//a11y-start
import ${package}.a11y.A11yHelper
//a11y-end
import kotlin.reflect.KClass
import logger
import org.assertj.core.description.TextDescription
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot

open class AbstractStepDefs(protected val testDataContainer: TestDataContainer) : En {

    private val log by logger()
    private var currentSessionID: String = "not set"

    fun getWebDriverSession(): WebDriverSession {
        if (currentSessionID == "not set") {
            testDataContainer.setTestData("session.id", testDataContainer.getTestId())
            currentSessionID = testDataContainer.getTestId()
        }
        return getWebDriverSession(currentSessionID)
    }

    fun getWebDriverSession(sessionID: String): WebDriverSession {

        val webDriverSession = WebDriverSessionStore.get(sessionID)

        if (testDataContainer.needsInitializing()) {
            if (webDriverSession.webDriver is RemoteWebDriver) {
                testDataContainer.setTestData("browser.version", (webDriverSession.webDriver as RemoteWebDriver).capabilities.browserVersion)
                testDataContainer.setTestData("browser", (webDriverSession.webDriver as RemoteWebDriver).capabilities.browserName)
            }

            if (webDriverSession.webDriver is ChromeDriver) {
                testDataContainer.setTestData("mobileEmulation", System.getProperty("browser").contains("emulation"))
            }

            testDataContainer.setTestData("initialized", true)
        }
        currentSessionID = sessionID
        testDataContainer.setTestData("session.id", currentSessionID)
        return webDriverSession
    }

    fun getWebDriver(): WebDriver {
        return getWebDriverSession().webDriver
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : AbstractPage> getPage(pageClass: KClass<T>): T {
        val page = getWebDriverSession().currentPage

        if (pageClass.isInstance(page)) {
            //a11y-start
            doA11YCheck()
            //a11y-end
            return page as T
        }
        log.error("Expect Page from type $pageClass but was $page")
        throw PageNotFoundException("Expect Page from type $pageClass but was $page")
    }

    fun getCurrentPage(): AbstractPage? {
        return getWebDriverSession().currentPage
    }

    fun setCurrentPage(page: AbstractPage) {
        getWebDriverSession().currentPage = page
    }

    //a11y-start
    private fun doA11YCheck() {
        if (testDataContainer.doA11YCheck()) {
            val scenario = testDataContainer.getScenario()
            val a11yExclusions = extractA11YExclusions(scenario)
            val webDriver = getWebDriver()
            val issues = A11yHelper.hasAccessibilityIssues(webDriver, a11yExclusions)

            if (issues.isNotEmpty()) {
                issues.forEach { violation ->
                    val violationString = "Violated Rule: $dollar$curlyOpen violation.id $curlyClose on page ${
                        getCurrentPage().toString().substringAfterLast(".")
                    } - $dollar$curlyOpen violation.nodes.joinToString("") { node -> $doublequote\n\t on: $dollar$curlyOpen node.html $curlyClose$doublequote$curlyClose$curlyClose$doublequote
                    testDataContainer.addScreenshot(
                        (webDriver as TakesScreenshot).getScreenshotAs(OutputType.BYTES),
                        "Forced A11y screenshot for step#${testDataContainer.getStepIndex()} - ${violation.description}"
                    )
                    testDataContainer.addA11ydescription(violationString)
                }

                val softAssertions = testDataContainer.getSoftAssertionObject()
                softAssertions
                    .assertThat(issues)
                    .`as`(TextDescription("Found ${issues.size} relevant A11Y violations in sceanrio '${scenario.name}' step# ${testDataContainer.getStepIndex()}"))
                    .isEmpty()
            }
        }
    }
    //a11y-end
}

fun extractTestIdFromScenarioName(scenarioName: String): String {
    val regex = "^\\[(.*) \\[.*\$".toRegex()
    try {
        return regex.find(scenarioName)!!.groups[1]!!.value
    } catch (e: NullPointerException) {
        fail("Scenarioname is not correct formated $scenarioName. Pattern: '[XXX-99 [Filename]")
    }
}

//a11y-start
private fun extractA11YExclusions(scenario: Scenario): List<String> {
    return scenario.sourceTagNames.filter { it.startsWith("@A11YExclude:") }.map { it.substring(it.indexOf(":")+1) }
}
//a11y-end

