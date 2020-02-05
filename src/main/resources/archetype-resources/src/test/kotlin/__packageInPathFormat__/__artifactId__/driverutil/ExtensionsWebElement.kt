package at.co.boris.secuton.driverutil

import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.remote.RemoteWebElement

fun WebElement.clearInput() {


    val browser = ((this as RemoteWebElement).wrappedDriver as RemoteWebDriver).getBrowserName()

    if (browser.equals("firefox")) {
        this.clear()
    } else {
        //(this as RemoteWebElement).wrappedDriver is FirefoxDriver
        click()
        sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END))
        sendKeys(Keys.DELETE)
    }
}


