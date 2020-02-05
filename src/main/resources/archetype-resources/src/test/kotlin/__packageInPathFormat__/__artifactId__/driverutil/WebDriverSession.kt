package at.co.boris.secuton.driverutil

import assertk.fail
import at.co.boris.secuton.pageobjects.AbstractPage
import at.co.boris.secuton.pageobjects.PageUrls
import at.co.boris.secuton.step_definitions.TestDataContainer
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.RemoteWebDriver
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class WebDriverSession(private val testId: String) {

    var currentPage: AbstractPage? = null
    private var lastPage: AbstractPage? = null
    val webDriver: WebDriver by lazy { DriverFactory.createWebDriver(testId) }
    private val baseUrl: String by lazy {
        if (System.getProperty("baseUrl").isBlank()) {
            fail("No BaseUrl is defined, do not know where to run the tests. Use '-DbaseUrl' to add the url where testenvironment is running ")
        }
        System.getProperty("baseUrl")
    }

    fun isMobile(): Boolean {
        return (webDriver as RemoteWebDriver).isMobile()
    }

    fun close() {
        webDriver.quit()
        currentPage = null
    }

    private fun getDomainFromBaseUrl(): String {
        return "^http[s]?://([^:/]*)".toRegex().find(baseUrl)!!.groups.get(1)!!.value
    }


    fun gotoUrl(subUrl: PageUrls, pageClass: KClass<out AbstractPage>, testDataContainer: TestDataContainer) {

        val scenario = testDataContainer.getScenario()

        val fullUrl = baseUrl + subUrl.subUrl
        webDriver.get(fullUrl)
        scenario.write("URL used: $fullUrl")
        openPage(subUrl, pageClass)
    }

    private fun <T : AbstractPage> openPage(subUrl: PageUrls, pageClass: KClass<T>): T {

        val fullUrl = baseUrl + subUrl.subUrl
        webDriver.get(fullUrl)
        return pageOpened(pageClass)
    }

    private fun <T : AbstractPage> pageOpened(currentPageClass: KClass<T>): T {
        val primaryConstructor = currentPageClass.primaryConstructor
        if (primaryConstructor == null || primaryConstructor.parameters.size != 1 || primaryConstructor.parameters[0].type.classifier != WebDriverSession::class) {
            fail("invalid primary constructor of page object")
        }
        return primaryConstructor.call(this)

    }

    fun activate(page: AbstractPage) {
        if (lastPage != null) {
            lastPage = currentPage
        }
        currentPage = page
    }

}