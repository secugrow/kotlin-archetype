package at.co.boris.secuton.pageobjects

import at.co.boris.secuton.driverutil.WebDriverSession
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

open class MainPage(session: WebDriverSession) : AbstractPage(session) {
    fun getLogo(): MutableList<WebElement>? {
        return webDriver.findElements(By.cssSelector("div.navbar-header a.navbar-brand img"))
    }

    fun clickMenuEntry(menustring: String): MainPage {

        if (session.isMobile()) {
            val hamburger = webDriver.findElement(By.cssSelector("a.navbar-toggle"))
            if (hamburger.getAttribute("aria-expanded") == "false") {
                hamburger.click()
            }
        }

        val link = webDriver.findElement(By.linkText(menustring))

        link.click()


        return when(menustring.toLowerCase()) {
            "team" -> TeamPage(session)
            "contact" -> ContactPage(session)
            else -> this
        }

    }


}
