package $

import org.openqa.selenium.By

{package}.pageobjects

import ${package}.driverutil.WebDriverSession
import ${package}.pageobjects.AbstractPage
import ${package}.pageobjects.MainPage
import org.openqa.selenium.By

class WikipediaPage(session: WebDriverSession) : MainPage(session)  {

    fun isSearchbarPresent(): Boolean {
        return webDriver.findElements(By.id("searchInput")).size >= 1
    }

    fun submitSearch(): WikiPediaSearchresultPage {
        getWebDriverWait().until(ExpectedConditions.elementToBeClickable(By.id("cmdSearch"))).click()
        return WikiPediaSearchresultPage(session)
    }

    fun searchFor(searchstring: String?): WikipediaContentPage {
        getSearchbar().sendKeys(searchstring)
        submitSearch()
        return WikipediaContentPage(session)
    }


}
