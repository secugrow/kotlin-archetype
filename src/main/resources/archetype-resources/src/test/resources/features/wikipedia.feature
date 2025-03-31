@all
Feature: [wikipedia] Example Feature

  @boris
  Scenario: [WIK-01 [wikipedia]
    Given the start page is loaded
    Then the searchbar is visible
    When the Selenium page is opened
    Then the header should be "Selenium"

  @no_appium
  Scenario: [WIK-02 [wikipedia]
    Given the start page for "User_1" is loaded
    And the start page for "User_2" is loaded
    Then the active session should contain 1 window
    When "User_2" opens software testing
    Then "User_1" should be still on start page
    And "User_2" should be still on software testing
