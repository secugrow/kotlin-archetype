package ${package}.driverutil

import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions

class FirefoxWebDriverFactory : WebDriverFactory() {


    override fun createDriver(): WebDriver {

        val ffOptions = FirefoxOptions()

        val linux = System.getProperty("os.name").lowercase().contains("linux")
        if (linux && System.getenv().get("TMPDIR")!!.contains("could_be_necessary_for_firefox_linux")) {
            log.warn("If you running on Linux and using a snap installed firefox you have to set the TMPDIR to a writeable directory in your homedirectory with the absolute path!")
        }


        webDriver = FirefoxDriver(ffOptions.merge(caps))
        webDriver.manage().window().size = screenDimension.dimension

        return webDriver
    }

}