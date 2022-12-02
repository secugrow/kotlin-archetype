package ${package}.driverutil

import java.time.LocalDateTime

abstract class RemoteWebDriverFactory: WebDriverFactory() {

    init {
        val videoRecording = System.getProperty("videoRecording", "no")

        caps.setVersion(getBrowserVersion())
        val executionTag = System.getProperty("execution.tag", "executionTag_not_set")
        val providerName = System.getProperty("remote.options", "selenoid")
        val providerOptions = mutableMapOf(
            "env" to listOf("LANG=de_AT.UTF-8", "LANGUAGE=at:de", "LC_ALL=de_AT.UTF-8", "TZ=Europe/Vienna"),
            "name" to executionTag,
            "screenResolution" to getScreenSizeAsString(screenDimension),
            "enableVNC" to true
        )

        caps.setCapability("$providerName:options", providerOptions)
        caps.setCapability("screenResolution", getScreenSizeAsString(screenDimension))

        if (videoRecording.toBoolean()) {
            caps.setCapability("enableVideo", true)
            caps.setCapability("videoName", "Test_${LocalDateTime.now()}.mp4")
        }
    }


    protected fun getRemoteTestingServer(): String {
        return System.getProperty("selenium.grid", "http://localhost:4444")
    }

    private fun getScreenSizeAsString(screenDimension: ScreenDimension, colordepth: Int = 24): String {
        return "${screenDimension.dimension.width}x${screenDimension.dimension.height}x$colordepth"
    }

}

