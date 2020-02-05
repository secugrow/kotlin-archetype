package at.co.boris.secuton.driverutil

import org.openqa.selenium.remote.RemoteWebDriver

fun RemoteWebDriver.getBrowserName(): String {
    return this.capabilities.browserName
}


fun RemoteWebDriver.isMobile(): Boolean {

    val caps = this.capabilities

    if (caps.version.contains("mobile")) {
        return true
    }

    if (caps.browserName.contains("mobile")) {
        return true
    }

    if (caps.capabilityNames.contains("mobileEmulationEnabled")) {
        if (caps.getCapability("mobileEmulationEnabled") as Boolean) {
            return true
        }
    }

    if (caps.capabilityNames.contains("appiumVersion")) {
        return true
    }

    if (System.getProperty("browser", "not set").contains("emulation")) {
        return true
    }

    return false
}