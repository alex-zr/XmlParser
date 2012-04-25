package parser.types;

import java.lang.reflect.Field;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 19.03.12
 */
public abstract class Type {
    public abstract void setValue(Object obj, Field field, String value);

    protected String capitalizeFirst(String name) {
        char firstCapitalized = Character.toUpperCase(name.charAt(0));
        return firstCapitalized + name.substring(1);
    }
}
