Feature: [wikipedia] Example Feature


  Scenario: [WIK-01 [wikipedia]
    Given the start page is loaded
    Then the searchbar is visible

  @boris @no_mobile
  Scenario: [WIK-02 [wikipedia]
    Given the start page for "User_1" is loaded
    And the start page for "User_2" is loaded
    Then the active session should contain 1 window
    When "User_2" opens google
    Then "User_1" should be still on start page
    And "User_2" should be still on google



    #add another example which is using picocontainer