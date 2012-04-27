package parser.types;

import parser.common.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 19.03.12
 */
public class ObjectByTypeFactory {
    private static final String STRING_TYPE = "String";
    private static final String INT_TYPE = "int";

    public static Type getType(String name) {
        if(name.contains(STRING_TYPE)) {
            return new StringType();
        } else if(name.contains(INT_TYPE)) {
            return new IntType();
        }
        throw new ParseException("No type found " + name);
    }

    public static Object getValue(String name, String content) {
        if(name.equals(INT_TYPE)) {
            IntType iType = new IntType();
            return iType.getValue(content);
        } else if(name.contains(STRING_TYPE)) {
            return content;
        }
        return null;
    }
}
