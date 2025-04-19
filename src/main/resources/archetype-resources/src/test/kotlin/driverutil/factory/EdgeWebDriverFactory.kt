package ${package}.driverutil.factory

import org.openqa.selenium.WebDriver
import org.openqa.selenium.edge.EdgeDriver

class EdgeWebDriverFactory : WebDriverFactory(){
    override fun createDriver(): WebDriver {

        checkEnvironment()

        webDriver = EdgeDriver()
        webDriver.manage().window().size = screenDimension.dimension

        return webDriver
    }

    private fun checkEnvironment() {

        if (!System.getProperty("os.name").contains("Windows")) {
            log.warn("Your OS seems not to be Windows")
        }

        if (!System.getenv("Path").lowercase().contains("edge")) {
            log.warn("Your SystemPath seems not to contain edge binary")
        }

    }

}
