Feature: Request Loan
  As a registered bank customer
  I want to apply for a loan
  So that I can finance a purchase

  Background:
    Given I am a registered customer with two accounts

  Scenario: Loan is approved for a reasonable amount with a down payment
    When I apply for a loan of "1000" with a down payment of "100"
    Then I should see the loan status "Approved"
    And I should see the loan message "Congratulations, your loan has been approved."

  Scenario: Loan is denied when the amount exceeds available funds
    When I apply for a loan of "100000" with a down payment of "1"
    Then I should see the loan status "Denied"
    And I should see the loan message "We cannot grant a loan in that amount with your available funds."

  Scenario Outline: Loan application with invalid input is not processed
    When I apply for a loan of "<loan>" with a down payment of "<down>"
    Then I should see an error and no loan decision

    Examples:
      | loan | down |
      |      |      |
      | abc  | 100  |