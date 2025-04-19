package ${package}.driverutil.factory

import ${package}.driverutil.ScreenDimension
import java.time.LocalDateTime

/**
 * Abstract base class for remote WebDriver factory implementations.
 *
 * This class extends WebDriverFactory to provide common functionality for creating
 * remote WebDriver instances, including configuration for video recording, VNC access,
 * and environment settings.
 */
abstract class RemoteWebDriverFactory: WebDriverFactory() {

    init {
        runCatching {
            val videoRecording = System.getProperty("videoRecording", "no")

            // Set browser version if specified
            caps.setVersion(getBrowserVersion())

            // Configure remote provider options
            val executionTag = System.getProperty("execution.tag", "executionTag_not_set")
            val providerName = System.getProperty("remote.options", "selenoid")
            val providerOptions = mutableMapOf(
                "env" to listOf("LANG=de_AT.UTF-8", "LANGUAGE=at:de", "LC_ALL=de_AT.UTF-8", "TZ=Europe/Vienna"),
                "name" to executionTag,
                "screenResolution" to getScreenSizeAsString(screenDimension),
                "enableVNC" to true
            )

            caps.setCapability("$providerName:options", providerOptions)

            // Configure video recording if enabled
            if (videoRecording.toBoolean()) {
                log.info("Video recording enabled")
                caps.setCapability("enableVideo", true)
                caps.setCapability("videoName", "Test_${LocalDateTime.now()}.mp4")
            }
        }.getOrElse { e ->
            log.error("Error configuring remote WebDriver capabilities: ${e.message}")
            throw e
        }
    }

    /**
     * Gets the URL of the remote Selenium server.
     *
     * @return The URL of the remote Selenium server
     */
    protected fun getRemoteTestingServer(): String {
        return System.getProperty("selenium.grid", "http://localhost:4444")
    }

    /**
     * Converts screen dimensions to a string format required by remote providers.
     *
     * @param screenDimension The screen dimension to convert
     * @param colordepth The color depth to use
     * @return A string representation of the screen dimensions
     */
    private fun getScreenSizeAsString(screenDimension: ScreenDimension, colordepth: Int = 24): String {
        return "${screenDimension.dimension.width}x${screenDimension.dimension.height}x$colordepth"
    }

}

