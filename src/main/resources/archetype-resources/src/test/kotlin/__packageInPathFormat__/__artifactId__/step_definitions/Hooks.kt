package at.co.boris.secuton.step_definitions

import at.co.boris.secuton.driverutil.WebDriverSessionStore
import at.co.boris.secuton.driverutil.isMobile
import io.appium.java_client.AppiumDriver
import io.appium.java_client.android.AndroidDriver
import io.cucumber.core.api.Scenario
import io.cucumber.java.After
import io.cucumber.java.Before
import logger
import org.apache.commons.io.FileUtils
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.RemoteWebDriver
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO
import org.imgscalr.Scalr


class Hooks(private val testDataContainer: TestDataContainer) {

    private val log by logger()

    @Before
    fun beforeScrenario(scenario: Scenario) {


        val fillchar = '#'
        val debuglength = 80


        testDataContainer.setScenario(scenario)
        testDataContainer.setTestData("browser.type", System.getProperty("browser", "no browser set"))
        testDataContainer.setTestData("browser.version", System.getProperty("browser.version", "no version set"))
        testDataContainer.setTestData("initialized", false)
        testDataContainer.setTestData("baseurl", System.getProperty("baseUrl", "no base Url given"))

        // to check if it runs on Jenkins or local
        val jobname = System.getenv("JOB_NAME")

        //Do Database resets here

        if (jobname != null) {
            log.debug("#".padEnd(debuglength, fillchar))
            log.debug("################## JENKINS INFOS: ".padEnd(debuglength, fillchar))
            log.debug("# BUILD_NUMBER: ${System.getenv("BUILD_NUMBER")}".padEnd(debuglength, fillchar))
            log.debug("# JOB_NAME: ${System.getenv("JOB_NAME")}".padEnd(debuglength, fillchar))
            log.debug("# JENKINS_URL: ${System.getenv("JENKINS_URL")}".padEnd(debuglength, fillchar))
            log.debug("# WORKSPACE: ${System.getenv("WORKSPACE")}".padEnd(debuglength, fillchar))
            log.debug("# NODE_NAME: ${System.getenv("NODE_NAME")}".padEnd(debuglength, fillchar))
            log.debug("#".padEnd(debuglength, fillchar))
            testDataContainer.setTestData("localRun", false)
        } else {
            testDataContainer.setTestData("localRun", true)
        }

        log.debug("#".padEnd(debuglength, fillchar))
        log.info("# executing Scenario: ${scenario.name}".padEnd(debuglength, fillchar))
        log.debug("#".padEnd(debuglength, fillchar))
    }

    @After
    fun afterScenario(scenario: Scenario) {

        val testId = testDataContainer.getTestId()
        val webDriverSession = WebDriverSessionStore.getIfExists(testDataContainer.getCurrentSessionId())

        if (!scenario.isFailed) {
            WebDriverSessionStore.quitAll()
            return
        }

        if (webDriverSession?.currentPage != null) {
            try {
                val isMobile = (webDriverSession.webDriver as RemoteWebDriver).isMobile()
                scenario.write("isMobile active for used webdriver: $isMobile")
                scenario.write("Last page which was used: ${webDriverSession.currentPage}")
                scenario.embed(webDriverSession.webDriver.pageSource.toByteArray(), "text/html")

                if (isMobile) {
                    val currentContext = (webDriverSession.webDriver as AppiumDriver<*>).context
                    (webDriverSession.webDriver as AppiumDriver<*>).context("NATIVE_APP")
                    scenario.embed((webDriverSession.webDriver as TakesScreenshot).getScreenshotAs(OutputType.BYTES), "image/png")
                    (webDriverSession.webDriver as AppiumDriver<*>).context(currentContext)
                } else {
                    val jsExecutor = (webDriverSession.webDriver as JavascriptExecutor)
                    jsExecutor.executeScript("window.scrollTo(0,0)")
                    val isScrollbarPresent = jsExecutor.executeScript("return document.documentElement.scrollHeigh>window.screen.height") as Boolean
                    if (isScrollbarPresent) {
                        val heightDocument = jsExecutor.executeScript("return document.body.scrollHeight") as Long
                        val heightViewport = jsExecutor.executeScript("return window.innerHeight") as Long
                        var pos: Long = 0

                        while (pos < heightDocument) {
                            scenario.addResizedScreenshotToReport(webDriverSession.webDriver)
                            pos += heightViewport
                            jsExecutor.executeScript("window.scrollBy(0,$heightViewport)")
                        }
                    }
                    scenario.addResizedScreenshotToReport(webDriverSession.webDriver)

                }

                if (testDataContainer.isLocalRun()) {
                    val screenshot = (webDriverSession.webDriver as TakesScreenshot).getScreenshotAs(OutputType.FILE)
                    FileUtils.copyFile(screenshot, File(System.getProperty("user.dir") + "/target/error_selenium_${testId}_${testDataContainer.getCurrentSessionId()}.png"))
                } else {
                    scenario.embed((webDriverSession.webDriver as TakesScreenshot).getScreenshotAs(OutputType.BYTES), "image/png")
                }

            } finally {
                WebDriverSessionStore.quitAll()
            }
        }
    }


    private fun Scenario.addResizedScreenshotToReport(webDriver: WebDriver) {
        if (testDataContainer.isMobileEmulation()) {
            this.embed(resizeScreenshot(webDriver), "image/png", "Mobile Screenshot")
            return
        }

        if (webDriver is AndroidDriver<*>) {
            this.embed(resizeScreenshot(webDriver), "image/png", "Android Screenshot")
            return
        }

        //else
        this.embed((webDriver as TakesScreenshot).getScreenshotAs(OutputType.BYTES), "image/png", "Screenshot")

    }


    private fun resizeScreenshot(webDriver: WebDriver): ByteArray {
        val image = (webDriver as TakesScreenshot).getScreenshotAs(OutputType.BYTES)
        val testimage: BufferedImage = Scalr.resize(ImageIO.read(ByteArrayInputStream(image)), 800)
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(testimage, "png", outputStream)
        return outputStream.toByteArray()
    }


}