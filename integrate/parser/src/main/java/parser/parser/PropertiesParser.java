package parser.parser;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 01.03.12
 */
public interface PropertiesParser {
    public List<Object> parseLineToObjectsList(String propertyLine);
}
