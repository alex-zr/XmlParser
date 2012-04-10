package parser.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.common.LogicException;
import parser.common.ReflexUtil;
import parser.configuration.Config;
import parser.model.NestedSetTree;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 01.03.12
 */
public class PropertiesParser {
    final static Logger logger = LoggerFactory.getLogger(PropertiesParser.class);
    private final String PROPERTIES_EXTENTION = "properties";
    private Config conf;
    private File resourceFolder;
    private List<NestedSetTree<Class>> classForest;
    private ReflexUtil util;

    public PropertiesParser(ReflexUtil util, Config conf, List<NestedSetTree<Class>> forest) {
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

        if(forest == null) {
            throw new LogicException("forest param can't be null");
        }
        
        if(conf == null) {
            throw new LogicException("Config can't be null");
        }

        this.util = util;
        this.conf = conf;
        this.resourceFolder = folder;
        this.classForest = forest;
    }

    /**
     * Read property files from path and create Map of the property files
     * (file name, map of the line objects)
     *
     * @return Map of the property files
     */
    public Map<String, Map<String, List<Object>>> readPropertieFilesMap() {
        Map<String, Map<String, List<Object>>> propsFileList = new HashMap<String, Map<String, List<Object>>>();
        List<File> filesFromInput = readFilesFromFolder(PROPERTIES_EXTENTION);
        for(File file : filesFromInput) {
            propsFileList.put(file.getName(), parsePropertyFile(file));
        }
        return propsFileList;
    }

    /**
     * Parse line in properties file, convert it to line name + objects list
     *
     * @param file - properties file
     * @return Map for file (line name, list of line objects)
     */
    private Map<String, List<Object>> parsePropertyFile(File file) {
        Properties props = new Properties();
        Map<String, List<Object>> fileRecord = new HashMap<String, List<Object>>();

        try {
            props.load(new FileInputStream(file));
        } catch (IOException e) {
            throw new LogicException("Properties file [" + file.getAbsolutePath() + "] not found, " + e);
        }

        Enumeration<?> names = props.propertyNames();
        
        while(names.hasMoreElements()) {
            String singlePropName = (String)names.nextElement();
            String classesListStr = props.getProperty(singlePropName);
            fileRecord.put(singlePropName, parseLineToObjectsList(classesListStr));
        }
        return fileRecord;
    }

    /**
     * Recursively parse objects from properties line
     *
     * @param propertyLine line with object structure without line name
     * @return line objects list
     */
    private List<Object> parseLineToObjectsList(String propertyLine) {

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
        Map<String, String>  classes = getClassesMap(content);
        if(classes.isEmpty()) {
            try {
                return util.instantiateObj(classForest, className, content);
            } catch (IllegalAccessException e) {
                throw new LogicException(e);
            } catch (InstantiationException e) {
                throw new LogicException(e);
            }
        }

        for(Map.Entry<String, String> clazz : classes.entrySet()) {
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

    private List<File> readFilesFromFolder(String ext) {
        List<File> fileNames = new ArrayList<File>();

        File[] files = resourceFolder.listFiles();

        for(File file : files) {
            if(file.getName().contains(ext)) {
                fileNames.add(file);
            }
        }
        return fileNames;
    }
}
