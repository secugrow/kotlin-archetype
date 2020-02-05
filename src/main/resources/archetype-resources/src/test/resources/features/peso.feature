Feature: [peso] Example Feature


  Scenario: [PES-01 [peso]
    Given the start page is loaded
    Then the peso logo should be displayed

    @boris
  Scenario: [PES-02 [peso]
    Given the start page is loaded
    When the team site is opened
    Then should "Boris Wrubel" be part of the Core team

  @no_mobile
  Scenario: [PES-03 [peso]
    Given the start page for "User_1" is loaded
    And the start page for "User_2" is loaded
    Then the active session should contain 1 window
    When "User_2" opens the team site
    Then "User_1" should be still on start page
    And "User_2" should be still on team site



    #add another example which is using picocontainer