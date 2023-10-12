package ${package}.pageobjects

import java.time.Duration
import ${package}.driverutil.WebDriverSession
import ${package}.pageobjects.AbstractPage
import ${package}.pageobjects.MainPage
import org.openqa.selenium.By

class WikipediaPage(session: WebDriverSession) : MainPage(session)  {

    fun isSearchbarPresent(): Boolean {
        return (webDriver.findElements(By.id("searchInput")).size >= 1).also { Thread.sleep(Duration.ofSeconds(2.toLong())) }
    }


}
