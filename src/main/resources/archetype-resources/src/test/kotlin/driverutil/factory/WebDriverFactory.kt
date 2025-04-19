package ${package}.driverutil.factory

import ${package}.driverutil.ScreenDimension
import ${package}.driverutil.WEBDRIVER_TIMEOUT_SECONDS
import logger
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.DesiredCapabilities
import java.time.Duration

/**
 * Abstract base class for WebDriver factory implementations.
 *
 * This class provides common functionality for creating WebDriver instances,
 * including capabilities management, screen dimension handling, and browser version retrieval.
 * Concrete implementations should override the [createDriver] method to provide
 * browser-specific WebDriver creation logic.
 */
abstract class WebDriverFactory {

    /** The WebDriver instance created by this factory */
    lateinit var webDriver: WebDriver

    /** Logger instance for this factory */
    protected val log by logger()

    /** Desired capabilities for the WebDriver */
    val caps = DesiredCapabilities()

    /** Screen dimension to be used for the browser window */
    lateinit var screenDimension: ScreenDimension

    /** Flag to indicate if the browser should run in headless mode */
    val headless: Boolean by lazy { System.getProperty("headless", "false").toBoolean() }

    /** Default timeout for WebDriver operations */
    val timeout: Long by lazy { System.getProperty("webdriver.timeout", WEBDRIVER_TIMEOUT_SECONDS.toString()).toLong() }

    init {
        runCatching {
            val screenSize = System.getProperty("screen.size", ScreenDimension.Desktop_1080.toString())
            screenDimension = ScreenDimension.valueOf(screenSize)
        }.getOrElse { e ->
            log.error("Invalid screen size specified: ${System.getProperty("screen.size")}. Using default.")
            screenDimension = ScreenDimension.Desktop_1080
        }
    }

    /**
     * Creates a WebDriver instance with the specified capabilities and settings.
     * This method must be implemented by concrete factory classes.
     *
     * @return A configured WebDriver instance
     */
    abstract fun createDriver(): WebDriver

    /**
     * Configures common timeouts for the WebDriver instance.
     *
     * @param driver The WebDriver instance to configure
     * @return The configured WebDriver instance
     */
    protected fun configureTimeouts(driver: WebDriver): WebDriver {
        return driver.also {
            it.manage()
                .timeouts()
                .apply {
                    implicitlyWait(Duration.ofSeconds(timeout))
                    pageLoadTimeout(Duration.ofSeconds(timeout))
                    scriptTimeout(Duration.ofSeconds(timeout))
                }
        }
    }

    /**
     * Cleans up resources associated with this factory.
     * This method should be called when the WebDriver instance is no longer needed.
     */
    fun cleanup() {
        if (::webDriver.isInitialized) {
            runCatching {
                webDriver.quit()
            }.onFailure { e ->
                log.error("Error quitting WebDriver: ${e.message}")
            }
        }
    }

    @Deprecated(
        message = "use getBrowserVersion instead",
        replaceWith = ReplaceWith("System.getProperty(\"browser.version\")")
    )
    fun getDriverVersion(): String? {
        return System.getProperty("driver.version")
    }

    /**
     * Gets the browser version from system properties.
     *
     * @return The browser version or null if not specified
     */
    fun getBrowserVersion(): String? {
        return System.getProperty("browser.version")
    }
}
