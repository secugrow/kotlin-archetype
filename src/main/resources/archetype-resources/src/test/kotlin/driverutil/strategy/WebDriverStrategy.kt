package ${package}.driverutil.strategy

import at.ptss.driverutil.factory.WebDriverFactory
import org.openqa.selenium.WebDriver

/**
 * Interface for the WebDriver strategy pattern.
 * Each concrete strategy will implement this interface to provide a specific WebDriver implementation.
 */
interface WebDriverStrategy {
    /**
     * Creates a WebDriver instance based on the specific strategy implementation.
     * @return WebDriver instance
     */
    fun createDriver(): WebDriver
}

/**
 * Adapter class that adapts WebDriverFactory to WebDriverStrategy.
 * This allows us to use existing WebDriverFactory implementations with the strategy pattern.
 */
class WebDriverFactoryAdapter(private val factory: WebDriverFactory) : WebDriverStrategy {
    override fun createDriver(): WebDriver {
        return factory.createDriver()
    }

    /**
     * Gets the underlying WebDriverFactory instance.
     * This allows access to factory-specific features like cleanup() and configurable timeouts.
     * 
     * @return The WebDriverFactory instance
     */
    fun getFactory(): WebDriverFactory {
        return factory
    }
}
