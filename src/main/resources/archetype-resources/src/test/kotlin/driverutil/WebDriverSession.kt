package ${package}.driverutil


import ${package}.pageobjects.AbstractPage
import ${package}.pageobjects.PageUrls
import ${package}.stepdefinitions.TestDataContainer
import org.assertj.core.api.Assertions.fail
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class WebDriverSession(private val testId: String) {

    var currentPage: AbstractPage? = null
    private var lastPage: AbstractPage? = null
    val webDriver: WebDriver by lazy { DriverFactory.createWebDriver(testId) }
    val wdwait: WebDriverWait by lazy { WebDriverWait(webDriver, Duration.ofSeconds(WEBDRIVER_TIMEOUT_SECONDS)) }
    private val baseUrl: String by lazy {
        if (System.getProperty("baseUrl").isBlank()) {
            fail<Nothing>("No BaseUrl is defined, do not know where to run the tests. Use '-DbaseUrl' to add the url where testenvironment is running ")
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
        scenario.log("URL used: $fullUrl")
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
            fail<Nothing>("invalid primary constructor of page object")
        }
        return primaryConstructor!!.call(this)

    }

    fun activate(page: AbstractPage) {
        if (lastPage != null) {
            lastPage = currentPage
        }
        currentPage = page
    }

}
