Feature: Transfer Funds
  As a registered bank customer
  I want to transfer money between my accounts
  So that I can manage my funds

  Background:
    Given I am a registered customer with two accounts

  Scenario: Successful transfer between my own accounts
    When I transfer "50" from my first account to my second account
    Then I should see a transfer complete confirmation for "50"

  Scenario Outline: Transfer is rejected for an invalid amount
    When I transfer "<amount>" from my first account to my second account
    Then I should see an error and no transfer confirmation

    Examples:
      | amount |
      |        |
      | abc    |

  @known-defect
  Scenario: Transfer larger than the available balance is rejected
    When I transfer "100000" from my first account to my second account
    Then I should see an error and no transfer confirmation