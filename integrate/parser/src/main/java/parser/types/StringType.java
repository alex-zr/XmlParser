package parser.types;

import parser.common.ParseException;

import java.lang.reflect.Field;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 19.03.12
 */
public class StringType extends Type {

    @Override
    public void setValue(Object obj, Field field, String value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new ParseException(e);
        }
    }
}
