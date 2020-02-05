package at.co.boris.secuton.driverutil

import java.time.LocalDateTime

abstract class RemoteWebDriverFactory: WebDriverFactory() {

    init {
        val videoRecording = System.getProperty("videoRecording", "no")

        caps.version = getBrowserVersion()
        caps.setCapability("sessionTimeout", "5m")
        caps.setCapability("enableVNC", true)


        val executionTag = System.getProperty("executionTag", "executionTag_not_set")
        caps.setCapability("name", executionTag)

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

