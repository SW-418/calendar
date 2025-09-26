package samwells.io.calendar.util;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {
    public boolean isNullOrEmpty(String s) {
        return s == null || s.isBlank();
    }
}
