Feature: Bill Pay
  As a registered bank customer
  I want to pay bills to payees from my account
  So that I can settle my payments online

  Background:
    Given I am a registered customer with two accounts

  Scenario: Successful bill payment
    When I pay "20" to payee "Electric Co" with account number "12345" verified as "12345"
    Then I should see the bill payment confirmation for "Electric Co" and "20"

  Scenario Outline: Bill payment is blocked by form validation
    When I pay "<amount>" to payee "Electric Co" with account number "<account>" verified as "<verify>"
    Then I should see the validation message "<message>"
    And the bill payment should not be completed

    Examples:
      | amount | account | verify | message                           |
      | 20     | 12345   | 54321  | The account numbers do not match. |
      | abc    | 12345   | 12345  | Please enter a valid amount.      |

  Scenario: Bill payment fails when all details are empty
    When I submit the bill payment form with no details
    Then I should see the validation message "Payee name is required."
    And I should see the validation message "The amount cannot be empty."
    And the bill payment should not be completed

  @known-defect
  Scenario: Bill payment is rejected when it exceeds the available balance
    When I pay "1000000" to payee "Electric Co" with account number "12345" verified as "12345"
    Then the bill payment should not be completed