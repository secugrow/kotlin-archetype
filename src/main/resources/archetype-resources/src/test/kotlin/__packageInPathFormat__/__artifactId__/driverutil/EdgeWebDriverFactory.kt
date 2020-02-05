package at.co.boris.secuton.driverutil

import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.edge.EdgeDriver

class EdgeWebDriverFactory : WebDriverFactory(){
    override fun createDriver(): WebDriver {

        WebDriverManager.edgedriver().version(getDriverVersion()).setup()
        checkEnvironment()

        webDriver = EdgeDriver()
        webDriver.manage().window().size = screenDimension.dimension

        return webDriver
    }

    private fun checkEnvironment() {

        if (!System.getProperty("os.name").contains("Windows")) {
            log.warn("Your OS seems not to be Windows")
        }

        if (!System.getenv("Path").toLowerCase().contains("edge")) {
            log.warn("Your SystemPath seems not to contain edge binary")
        }

    }

}
