package net.easycloud.api.utils;

import java.security.SecureRandom;
import java.util.Locale;;

public final class RandomStringUtil {
    public static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER_CASE = UPPER_CASE.toLowerCase(Locale.ROOT);
    public static final String digits = "0123456789";

    public static String generate(int length) {
        if (length < 1) throw new IllegalArgumentException();
        var random = new SecureRandom();
        var symbols = (UPPER_CASE + LOWER_CASE + digits).toCharArray();
        var buf = new char[length];

        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }
}
