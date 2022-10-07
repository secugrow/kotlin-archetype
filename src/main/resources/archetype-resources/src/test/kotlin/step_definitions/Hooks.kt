#set( $dollar = '$' )
#set( $curlyOpen = '{' )
#set( $curlyClose = '}' )
#set( $bracketOpen = '(' )
#set( $bracketClose = ')' )
package ${package}.step_definitions

import ${package}.driverutil.WebDriverSessionStore
import ${package}.driverutil.isMobile
import ${package}.driverutil.DriverType
import io.appium.java_client.AppiumDriver
import io.appium.java_client.android.AndroidDriver
import io.cucumber.java.After
import io.cucumber.java.AfterStep
import io.cucumber.java.Before
import io.cucumber.java.BeforeStep
import io.cucumber.java.Scenario
import logger
import org.apache.commons.io.FileUtils
import org.assertj.core.api.SoftAssertions
import org.imgscalr.Scalr
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


class Hooks(private val testDataContainer: TestDataContainer) {

    private val log by logger()
    private val skipA11Y = System.getProperty("skipA11y", "true").toBoolean()

    @BeforeStep
    fun beforeStep() {
        testDataContainer.incrementStepIndex()
    }

    @Before
    fun beforeScrenario(scenario: Scenario) {

        val fillchar = '#'
        val debuglength = 80


        testDataContainer.setScenario(scenario)
        testDataContainer.setTestData("browser.type", DriverType.valueOf(System.getProperty("browser", "no browser set").uppercase()))
        testDataContainer.setTestData("browser.version", System.getProperty("browser.version", "no version set"))
        testDataContainer.setTestData("initialized", false)
        testDataContainer.setTestData("baseurl", System.getProperty("baseUrl", "no base Url given"))
        testDataContainer.setTestData("skipA11y", skipA11Y)

        testDataContainer.setTestData("stepIndex", 0)
        testDataContainer.setTestData("softAssertion.object", SoftAssertions())

        if (skipA11Y.not()) {
            testDataContainer.setTestData("softAssertions.present", true)
            testDataContainer.setTestData("a11y.description", mutableListOf<String>())
        }

        // to check if it runs on Jenkins or local
        val jobname = System.getenv("JOB_NAME")

        //Do Database resets here

        if (jobname != null) {
            log.debug(
                "${fillchar.toString().padEnd(debuglength, fillchar)}\n" +
                        " JENKINS INFOS:".padStart(18, fillchar).padEnd(debuglength, fillchar) + "\n" +
                        "$dollar$curlyOpen String.format("BUILD_NUMBER: %s", System.getProperty("BUILD_NUMBER")).padEnd(debuglength, fillchar)$curlyClose\n" +
                        "# BUILD_NUMBER:".plus(System.getenv("BUILD_NUMBER")).plus("".padEnd(debuglength, fillchar)).plus("\n") +
                        "# JOB_NAME: " + System.getenv("JOB_NAME") + "".padEnd(debuglength, fillchar) + "\n" +
                        "# JENKINS_URL: " + System.getenv("JENKINS_URL") + "".padEnd(debuglength, fillchar) + "\n" +
                        "# WORKSPACE: " + System.getenv("WORKSPACE") + "".padEnd(debuglength, fillchar) + "\n" +
                        "# NODE_NAME: " + System.getenv("NODE_NAME") + "".padEnd(debuglength, fillchar) + "\n" +
                        "#".padEnd(debuglength, fillchar)
            )
            testDataContainer.setTestData("localRun", false)
        } else {
            testDataContainer.setTestData("localRun", true)
        }

        log.debug("#".padEnd(debuglength, fillchar))
        log.info("# executing Scenario: " +  scenario.name + "".padEnd(debuglength, fillchar))
        log.debug("#".padEnd(debuglength, fillchar))
    }

    @After(order = 1000)
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
                scenario.log("isMobile active for used webdriver: " + isMobile)
                scenario.log("Last page which was used: " + webDriverSession.currentPage)
                scenario.attach(webDriverSession.webDriver.pageSource.toByteArray(), "text/html","pagesource")

                if (isMobile) {
                    val currentContext = (webDriverSession.webDriver as AppiumDriver<*>).context
                    (webDriverSession.webDriver as AppiumDriver<*>).context("NATIVE_APP")
                    scenario.attach((webDriverSession.webDriver as TakesScreenshot).getScreenshotAs(OutputType.BYTES),"image/png", "Screenshot")

                    (webDriverSession.webDriver as AppiumDriver<*>).context(currentContext)
                } else {
                    val jsExecutor = (webDriverSession.webDriver as JavascriptExecutor)
                    jsExecutor.executeScript("window.scrollTo(0,0)")
                    val isScrollbarPresent = jsExecutor.executeScript("return document.documentElement.scrollHeight>window.screen.height") as Boolean
                    if (isScrollbarPresent) {
                        val heightDocument = jsExecutor.executeScript("return document.body.scrollHeight") as Long
                        val heightViewport = jsExecutor.executeScript("return window.innerHeight") as Long
                        var pos: Long = 0

                        while (pos < heightDocument) {
                            scenario.addResizedScreenshotToReport(webDriverSession.webDriver)
                            pos += heightViewport
                            jsExecutor.executeScript("window.scrollBy(0," + heightViewport + ")")
                        }
                    }
                    scenario.addResizedScreenshotToReport(webDriverSession.webDriver)

                }

                //processing forced screenshots during test
                testDataContainer.getScreenshots().forEachIndexed { index, screenshot ->
                    scenario.attach(screenshot.first, "image/png", "Forced Screenshot - ${index + 1} - ${screenshot.second}")
                }

                if (testDataContainer.isLocalRun()) {
                    val screenshot = (webDriverSession.webDriver as TakesScreenshot).getScreenshotAs(OutputType.FILE)
                    FileUtils.copyFile(screenshot, File(System.getProperty("user.dir") + "/target/error_selenium_" + testId + "_" + testDataContainer.getCurrentSessionId() + ".png"))
                } else {
                    scenario.attach((webDriverSession.webDriver as TakesScreenshot).getScreenshotAs(OutputType.BYTES), "image/png", "Screenshot")
                }

            } finally {
                WebDriverSessionStore.remove(testId)
            }
        }
    }



    @After(order = 1100)
    fun softAssertAll() {
        if (testDataContainer.hasSoftAssertions() || !skipA11Y) {
            testDataContainer.getSoftAssertionObject().assertAll()
        }
    }


    @AfterStep
    fun a11y(scenario: Scenario) {
        testDataContainer.getAndClearA11Ydescriptions().forEachIndexed { index, issue ->
            scenario.attach(issue, "text/plain", "A11Y Issue $dollar$curlyOpen index + 1 $curlyClose in stepnumber $dollar$curlyOpen testDataContainer.getStepIndex() $curlyClose ")
        }
    }



    private fun Scenario.addResizedScreenshotToReport(webDriver: WebDriver) {
        if (testDataContainer.isMobileEmulation()) {
            this.attach(resizeScreenshot(webDriver), "image/png", "Mobile Screenshot")
            return
        }

        if (webDriver is AndroidDriver<*>) {
            this.attach(resizeScreenshot(webDriver), "image/png", "Android Screenshot")
            return
        }

        //else
        this.attach((webDriver as TakesScreenshot).getScreenshotAs(OutputType.BYTES), "image/png", "Screenshot")

    }


    private fun resizeScreenshot(webDriver: WebDriver): ByteArray {
        val image = (webDriver as TakesScreenshot).getScreenshotAs(OutputType.BYTES)
        val testimage: BufferedImage = Scalr.resize(ImageIO.read(ByteArrayInputStream(image)), 800)
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(testimage, "png", outputStream)
        return outputStream.toByteArray()
    }


}
