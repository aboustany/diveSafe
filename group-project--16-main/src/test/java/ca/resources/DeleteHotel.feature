Feature: Delete Hotel (p16)
  As an administrator, I want to delete an existing hotel in the system so that I can restrict the hotels available to diveers.

  Background: 
    Given the following DiveSafe system exists: (p16)
      | startDate  | numDays | priceOfGuidePerDay |
      | 2022-01-13 |      20 |                 50 |
    Given the following hotels exist in the system: (p16)
      | name                    | address                  | rating | type   |
      | Brian's Bed & Breakfast | 123 Crescent St., Neptan |      2 | Villa  |
      | The Ritz Carlton        | 456 Main St., Neptan     |      5 | Resort |

  Scenario: Successfully delete a hotel
    When the administator attempts to delete the hotel in the system with name "Brian's Bed & Breakfast" (p16)
    Then the number of hotels in the system shall be "1" (p16)
    Then the hotel with name "Brian's Bed & Breakfast", address "123 Crescent St., Neptan", rating "2", and type "Resort" shall not exist in the system (p16)
    Then the following hotels shall exist in the system: (p16)
      | name             | address              | rating | type   |
      | The Ritz Carlton | 456 Main St., Neptan |      5 | Resort |

  Scenario: Delete a hotel that does not exist in the system
    When the administator attempts to delete the hotel in the system with name "Sofitel" (p16)
    Then the number of hotels in the system shall be "2" (p16)
    Then the following hotels shall exist in the system: (p16)
      | name                    | address                  | rating | type   |
      | Brian's Bed & Breakfast | 123 Crescent St., Neptan |      2 | Villa  |
      | The Ritz Carlton        | 456 Main St., Neptan     |      5 | Resort |
