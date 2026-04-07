# SeCuGrow - your generated Selenium Cucumber test automation project

If you want to start as fast as possible with [Selenium](https://github.com/SeleniumHQ/selenium) in combination
with [Cucumber](https://github.com/cucumber/cucumber) - here's your place to be. Just clone this repo and start it. It
contains executable minimalistic examples so you can create your own scenarios easily and fast.

You should be familiar with [Kotlin](https://kotlinlang.org/) (or Java) and Cucumber to create new scenarios and
corresponding glue code.

This is a skeleton based on Selenium, Cucumber with Kotlin and parallel-execution support with JUnit 5.
Also, [Cucumber Picocontainers](https://github.com/cucumber/cucumber-jvm/tree/master/picocontainer) were added for a
smooth usage of test data among all steps.

## Prerequisites

* Java SDK (recommended Version > 17)
* Maven

### Optional

* If you do not want to run the test locally: Selenium Grid, [Selenoid](https://github.com/aerokube/selenoid) or Moon
* For Android or iOS test execution you can use it with [Appium](https://github.com/appium/appium)

# How to run tests locally

* Option 1: Start with Maven

  `mvn test -Dbrowser=chrome -Dheadless=true -DbaseUrl="https://www.wikipedia.org" -Dcucumber.filter.tags=@all`

* Option 2: Start directly from IDEA with a runConfiguration
  ![idea run configuration](docs/images/idea_runConfig.png)
  Use the runConfiguration File which is located in the resources-folder and adapt the parameter for your need

In both cases you need to define some parameters to get the tests running:

| Name | Description |
|------|-------------|
| baseUrl* | The base URL for your website under test. |
| browser | Choose the browser type. Allowed values are defined in DriverTypes Class. default = chrome |
| browser.version | If you do not want to use the latest browser version, which is provided by the Webdriver manager, you can set the version with this parameter. |
| selenium.grid | URL of Selenium grid server or a service which implements the Selenium grid protocol like Selenoid or Appium. |
| devices | Comma-separated list of Android device serials for Appium execution. Resolved dynamically via `adb devices` — see example below. Works with one or more connected devices. |
| skipA11y | default = true, false -> activates the accessibility audits while your testruns (if you created your project with (-Da11y=true) |

\* is mandatory

### Run tests with Selenium grid, Selenoid or Appium

    -Dselenium.grid=http://<ip-of-your-grid:4444>

Example runtime parameters:

    -Dbrowser=chrome
    -DbaseUrl="http://www.wikipedia.org"

If you need to pass additional capabilities for your environment (selenoid, selenium grid or a cloud provider of your choice)
you can name the provider with:

    -Dremote.options=selenoid

Add the corresponding options to RemoteWebDriverFactory.kt

# Scenarios

### Feature file structure

In order to find scenarios faster - especially if they fail - a scenario template with two components is used:

* Filename is given in "[ ]" (squared brackets)
* Description of every scenario starts with [XXX-99 followed again with the [filename]

example:

```gherkin
Feature: [WIK [wikipedia] Example Feature

  Background:
    Given the start page is loaded

  Scenario: [WIK-01 [wikipedia] 
    Then the searchbar is visible
```

[wikipedia] means that "wikipedia" is the filename. This makes it much easier to locate steps when IntelliJ Runner or
Jenkins Cucumber report mark a scenario or step as failed.

Scenarios have a unique ID which you have to assign manually and keep track of. If a scenario fails, you can easily jump
to the step definitions via text search in your IDE.

<img src="src/test/resources/docs/testresults_idea.png" alt="Testresults in IntelliJ" width="50%">

![testresults jenkins overview](src/test/resources/docs/jenkins_overall.png)
![testresults jenkins feature](src/test/resources/docs/jenkins_feature.png)
#Supported Browser in DriverType enum
![testresults from IntelliJ](docs/images/testresults_idea.png)
![testresults jenkins overview](docs/images/jenkins_overview.png)
![testresults jenkins feature](docs/images/jenkins_feature.png)

# Supported Browser in DriverType enum
Setup will be done via WebDriverManager as mentioned above.

### regular Browsers for local run

Chrome, Firefox, Opera, IE and Edge

### Remote Browsers (via Selenoid, Selenium Grid, ...)

Chrome, Firefox, Opera, Chrome Mobile, Chrome Mobile Emulation, Android

### Mobile Chrome emulation (with user agent manipulation)

Chrome is used with a manipulated user agent, similar to the mobile view in browser developer mode.
The following option is set in the DriverFactory:
`chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);`

### Mobile-chrome (via Selenoid)

This only works if you have a Selenoid or Moon environment. For further information take a look
at [Aerokube Website](https://aerokube.com)

Example of a runtime configuration for an emulated Pixel 2 with a desktop Chrome browser:

    -Dbrowser=chrome_mobile_emulation
    -DbaseUrl="http://www.wikipedia.org"
    -Demulated.device="Pixel 2"

### Android device (via Appium)

You can use an emulated device (AVD Manager) or a connected real device, which both have to be supported by Appium.
Connected devices are discovered automatically via `adb devices` — the same command works whether one or multiple devices are connected.

**Bare metal** — `adb` is installed locally on the machine running the tests:

```shell
mvn test \
  -Dbrowser=appium_android_device \
  -DbaseUrl="https://www.wikipedia.org" \
  -Dselenium.grid=http://localhost:4723 \
  -Dcucumber.filter.tags=@all \
  -Dcucumber.glue=<your.glue.package> \
  -Dcucumber.features=src/test/resources/features \
  -Ddevices=$(adb devices | grep -w device | awk '{print $1}' | paste -sd,)
```

**Docker** — `adb` is only available inside the Appium container, query it via `docker exec`:

```shell
mvn test \
  -Dbrowser=appium_android_device \
  -DbaseUrl="https://www.wikipedia.org" \
  -Dselenium.grid=http://localhost:4723 \
  -Dcucumber.filter.tags=@all \
  -Dcucumber.glue=<your.glue.package> \
  -Dcucumber.features=src/test/resources/features \
  -Ddevices=$(docker exec appium-server adb devices | grep -w device | awk '{print $1}' | paste -sd,)
```

# Troubleshooting

## IDEA Configuration

If the generated project does not compile or cannot be started from IDEA, check the Version of the JDK.
Use a JDK Version above 17.

* File - Project Structure ...
  ![name](src/test/resources/docs/project_sdk_settings.png)