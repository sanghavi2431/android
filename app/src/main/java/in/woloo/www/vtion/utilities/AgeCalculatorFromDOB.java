package in.woloo.www.vtion.utilities;

import java.util.Calendar;
import java.util.Date;

public class AgeCalculatorFromDOB {

    public static int calculateAge(Date birthDate) {
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birthDate);

        Calendar currentCalendar = Calendar.getInstance();

        int years = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

        // Adjust age when the current date is before the birth date (e.g., just after New Year for a birthday in December)
        if (currentCalendar.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            years--;
        }

        return years;
    }
}
