package parser.types;

import parser.common.ParseException;

import java.lang.reflect.Field;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 19.03.12
 */
public class IntType extends Type {
    @Override
    public void setValue(Object obj, Field field, String value) {
        try {
            field.setAccessible(true);
            field.setInt(obj, Integer.parseInt(value));
        } /*catch (NoSuchFieldException e) {
            throw new ParseException("No such field found " + field.getName(), e);
        }*/ catch (IllegalAccessException e) {
            throw new ParseException("Can't set field value " + value + " for field " + field.getName(), e);
        }
    }
}
