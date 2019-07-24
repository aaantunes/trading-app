package ca.jrvs.apps.trading.util;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ParametersUtil {

    public static <T> List<String> checkIfNullsInObject(T ob) {

        List<String> fieldsAsNull = new LinkedList<>();

        for (Field f : ob.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
                if (Objects.isNull(f.get(ob))) {
                    fieldsAsNull.add(f.getName());
                }
            } catch (Exception e) {
                e.getMessage();
                e.printStackTrace();
            }
        }
        return fieldsAsNull;
    }

}
