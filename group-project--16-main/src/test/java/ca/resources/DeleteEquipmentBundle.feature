Feature: Delete Equipment Bundle (p6)
  As an administrator, I wish to delete equipment bundles so that I can keep diveers up to date.

  Background: 
    Given the following DiveSafe system exists: (p6)
      | startDate  | numDays | priceOfGuidePerDay |
      | 2022-01-13 |      20 |                 50 |
    Given the following equipment exists in the system: (p6)
      | name       | weight | pricePerDay |
      | fins       |   3540 |           5 |
      | dive boots |    907 |          50 |
      | BCD        |    340 |         100 |
    Given the following equipment bundles exist in the system: (p6)
      | name         | discount | items               | quantities |
      | small bundle |       10 | fins,dive boots     |        2,1 |
      | large bundle |       25 | fins,dive boots,BCD |      2,1,1 |
    Given the following members exist in the system: (p6)
      | email             | password | name  | emergencyContact | numDays | guideRequired | hotelRequired | itemBookings      | quantity |
      | member1@email.com | pass1    | Peter | (666)555-5555    |       1 | true          | true          | large bundle,fins |      1,2 |

  Scenario: Successfully delete equipment bundle
    When the administrator attempts to delete the equipment bundle "small bundle" (p6)
    Then the number of equipment bundles in the system shall be "1" (p6)
    Then the equipment bundle "small bundle" shall not exist in the system (p6)
    Then the equipment bundle "large bundle" shall preserve the following properties: (p6)
      | name         | discount | items               | quantities |
      | large bundle |       25 | fins,dive boots,BCD |      2,1,1 |

  Scenario: Successfully delete equipment bundle that has been requested by a member
    When the administrator attempts to delete the equipment bundle "large bundle" (p6)
    Then the number of equipment bundles in the system shall be "1" (p6)
    Then the equipment bundle "large bundle" shall not exist in the system (p6)
    Then the equipment bundle "small bundle" shall preserve the following properties: (p6)
      | name         | discount | items           | quantities |
      | small bundle |       10 | fins,dive boots |        2,1 |
    Then the member "member1@email.com" shall have the following bookable items: (p6)
      | itemBookings | quantity |
      | fins         |        2 |

  Scenario Outline: Attempt to delete an equipment bundle that does not exist
    When the administrator attempts to delete the equipment bundle "<name>" (p6)
    Then the number of equipment bundles in the system shall be "2" (p6)
    Then the number of pieces of equipment in the system shall be "3" (p6)
    Then the equipment bundle "small bundle" shall preserve the following properties: (p6)
      | name         | discount | items           | quantities |
      | small bundle |       10 | fins,dive boots |        2,1 |
    Then the equipment bundle "large bundle" shall preserve the following properties: (p6)
      | name         | discount | items               | quantities |
      | large bundle |       25 | fins,dive boots,BCD |      2,1,1 |

    Examples: 
      | name            |
      | tiny bundle     |
      | clothing bundle |
      | BCD             |
