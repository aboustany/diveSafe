package ca.mcgill.ecse.divesafe.controller;

import java.sql.Date;
import ca.mcgill.ecse.divesafe.application.DiveSafeApplication;
import ca.mcgill.ecse.divesafe.persistence.DiveSafePersistence;

public class SetupController {

  private SetupController() {}

  public static String setup(Date startDate, int numDays, int priceOfGuidePerDay) {
    if (numDays < 0) {
      return "The number of diving days must be greater than or equal to zero";
    } else if (priceOfGuidePerDay < 0) {
      return "The price of guide per day must be greater than or equal to zero";
    } else {
      var diveSafe = DiveSafeApplication.getDiveSafe();
      diveSafe.setStartDate(startDate);
      diveSafe.setPriceOfGuidePerDay(priceOfGuidePerDay);
      diveSafe.setNumDays(numDays);
    }
    try {
      DiveSafePersistence.save();
    } catch (RuntimeException e) {
      return e.getMessage();
    } 
    return "";
  }
  
  /**
   * @Author Karim Kanafani
   * @param startDate
   * @return boolean
   * 
   * Helper method that checks if the date provided is valid
   */
  public static boolean isDateValid(String startDate){
    String day;
    String month;

    month = startDate.substring(5,7);
    day = startDate.substring(8,10);

    int dayProvided = Integer.parseInt(day); // Get day 
    int monthProvided = Integer.parseInt(month); // Get month 
    
    if(monthProvided < 0 || monthProvided > 12 || dayProvided < 0 || dayProvided > 31){ // Check if month or day is out of range
      return false;
    }else if(monthProvided == 2 && dayProvided > 29){ // If month is February, day cannot be greater than 29
      return false;
    }else if(monthProvided == 4 || monthProvided == 6 || monthProvided == 9 || monthProvided == 11){ // Months with 30 days at most
      if(dayProvided > 30){
        return false;
      }
      return true;
    }else{
      return true;
    }
  }


}
