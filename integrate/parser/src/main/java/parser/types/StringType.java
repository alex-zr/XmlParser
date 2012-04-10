package parser.types;

import parser.common.ParseException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 19.03.12
 */
public class StringType extends Type {

    @Override
    public void setValue(Object obj, String name, String value) {
        String setterName = "set" + capitalizeFirst(name);
        try {
            Method method = obj.getClass().getMethod(setterName, String.class);
            method.invoke(obj, value);
        } catch (NoSuchMethodException e) {
            throw new ParseException("Method " + setterName + "(String) not found in class " + obj.getClass().getName() , e);
        } catch (InvocationTargetException e) {
            throw new ParseException(e);
        } catch (IllegalAccessException e) {
            throw new ParseException(e);
        }
    }
}
