package fun.lance.poetry.util;

import java.util.Arrays;
import java.util.Objects;

public class CheckUtil {

    public static boolean containsNull(Object... strings) {
        return Arrays.stream(strings).anyMatch(Objects::isNull);
    }
}
