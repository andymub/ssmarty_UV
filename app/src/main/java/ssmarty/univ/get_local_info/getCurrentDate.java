package ssmarty.univ.get_local_info;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class getCurrentDate {
    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd  / MM / yyyy , HH:mm");
        String strDate = "" + mdformat.format(calendar.getTime());

        return strDate;
    }
}
