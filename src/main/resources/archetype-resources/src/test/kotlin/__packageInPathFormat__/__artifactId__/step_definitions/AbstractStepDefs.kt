package ${groupId}

import assertk.fail
import ${groupId}.driverutil.PageNotFoundException
import ${groupId}.driverutil.WebDriverSession
import ${groupId}.driverutil.WebDriverSessionStore
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.remote.RemoteWebDriver
import ${groupId}.pageobjects.AbstractPage
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
                testDataContainer.setTestData("browser.version", (webDriverSession.webDriver as RemoteWebDriver).capabilities.version)
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
}

fun extractTestIdFromScenarioName(scenarioName: String): String {
    val regex = "^\\[(.*) \\[.*\$".toRegex()
    try {
        return regex.find(scenarioName)!!.groups[1]!!.value
    } catch (e: NullPointerException) {
        fail("Scenarioname is not correct formated $scenarioName. Pattern: '[XXX-99 [Filename]")
    }
}

