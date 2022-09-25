#set( $doublequote = '"' )
#set( $bracketOpen = '(' )
#set( $bracketClose = ')' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
#set( $dollar = '$' )

package ${package}.step_definitions

import assertk.fail
import ${package}.driverutil.PageNotFoundException
import ${package}.driverutil.WebDriverSession
import ${package}.driverutil.WebDriverSessionStore
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.remote.RemoteWebDriver
import ${package}.pageobjects.AbstractPage
import io.cucumber.java8.En
import kotlin.reflect.KClass
import logger

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
            doA11YCheck()
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


    private fun doA11YCheck() {
        if (testDataContainer.doA11YCheck()) {
            val scenario = testDataContainer.getScenario()
            val a11yExclusions = extractA11YExclusions(scenario)
            val webDriver = getWebDriver()
            val issues = A11yHelper.hasAccessibilityIssues(webDriver, a11yExclusions)

            if (issues.isNotEmpty()) {
                issues.forEach { violation ->
                    val violationString = "Violated Rule: $dollar$curlyOpenviolation.id$curlyClose on page ${
                        getCurrentPage().toString().substringAfterLast(".")
                    } - $dollar$curlyOpen violation.nodes.joinToString("") { node -> $doublequote ton: $dollar{node.html} $doublequote }$curlyClose"
                    testDataContainer.addScreenshot(
                        (webDriver as TakesScreenshot).getScreenshotAs(OutputType.BYTES),
                        "Forced A11y screenshot for step#${testDataContainer.getStepInex()} - ${violation.description}"
                    )
                    testDataContainer.addA11Ydescription(violationString)
                }

                val softAssertions = testDataContainer.getSoftAssertionObject()
                softAssertions
                    .assertThat(issues)
                    .`as`(TextDescription("Found ${issues.size} relevant A11Y violations in sceanrio \"${scenario.name}\" step# ${testDataContainer.getStepInex()}"))
                    .isEmpty()
            }
        }
    }
}

fun extractTestIdFromScenarioName(scenarioName: String): String {
    val regex = "^\\[(.*) \\[.*\$".toRegex()
    try {
        return regex.find(scenarioName)!!.groups[1]!!.value
    } catch (e: NullPointerException) {
        fail("Scenarioname is not correct formated $scenarioName. Pattern: '[XXX-99 [Filename]")
    }
}

