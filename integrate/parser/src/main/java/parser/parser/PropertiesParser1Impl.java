package parser.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.common.LogicException;
import parser.configuration.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 10.04.12
 */
public class PropertiesParser1Impl implements PropertiesParser {
    final static Logger logger = LoggerFactory.getLogger(PropertiesParser1Impl.class);
    private Config conf;
    private List<Class> classes;
    private ReflexUtil util;

    public PropertiesParser1Impl(ReflexUtil util, Config conf, List<Class> classes) {
        if(conf == null) {
            throw new LogicException("Config can't be null");
        }

        String inputFolder = conf.getInput();
        File folder = new File(inputFolder);

        if(!folder.exists()) {
            throw new LogicException("Directory " + inputFolder + " does not exists");
        }

        if(!folder.isDirectory()) {
            throw new LogicException("Path " + inputFolder + " is not a directory");
        }

        if(util == null) {
            throw new LogicException("util param can't be null");
        }

        if(classes == null) {
            throw new LogicException("classes param can't be null");
        }


        this.util = util;
        this.conf = conf;
        this.classes = classes;
    }

    /**
     * Recursively parse objects from properties line
     *
     * @param propertyLine line with object structure without line name
     * @return line objects list
     */
    public List<Object> parseLineToObjectsList(String propertyLine) {

        List<Object> classesData = new ArrayList<Object>();

        LineParseIterator parseIterator = new LineParseIterator(util, conf, propertyLine);

        for(Map.Entry<String, String> objEntry : parseIterator) {
            Object object = buildObject(objEntry.getKey(), objEntry.getValue());
            classesData.add(object);

            logger.debug(objEntry + " ");
        }

        return classesData;
    }

    /**
     * Recursive build object by class name and object content
     *
     * @param className
     * @param content
     * @return
     */
    private Object buildObject(String className, String content) {
        Map<String, String>  classesMap = getClassesMap(content);
        if(classesMap.isEmpty()) {
            try {
                return util.instantiateObj(classes, className, content);
            } catch (IllegalAccessException e) {
                throw new LogicException(e);
            } catch (InstantiationException e) {
                throw new LogicException(e);
            }
        }

        for(Map.Entry<String, String> clazz : classesMap.entrySet()) {
            buildObject(clazz.getKey(), clazz.getValue());
        }

        return null;
    }

    private Map<String, String> getClassesMap(String content) {
        Map<String, String> classes = new HashMap<String, String>();
        LineParseIterator parseIterator = new LineParseIterator(util, conf, content);
        for(Map.Entry<String, String> o : parseIterator) {
            classes.put(o.getKey(), o.getValue());
        }
        return classes;
    }
}
