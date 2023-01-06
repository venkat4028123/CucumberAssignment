Feature: Wales Test Ticket

   Scenario: User gets help or not
   Given I launch browser and Navigate to Application
   When I put my circumstances into the Checker tool Scenario1
   Then I should get a result of whether I will get help or not
 
	 Scenario: User gets help with free cost
   Given I launch browser and Navigate to Application
   When I put my circumstances into the Checker tool Scenario2
   Then I should get a result of whether I will get help with freecosts