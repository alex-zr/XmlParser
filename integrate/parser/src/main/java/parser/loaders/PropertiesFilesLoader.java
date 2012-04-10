package parser.loaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.common.LogicException;
import parser.configuration.Config;
import parser.parser.PropertiesParser;
import parser.parser.ReflexUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 10.04.12
 */
public class PropertiesFilesLoader {
    final static Logger logger = LoggerFactory.getLogger(PropertiesFilesLoader.class);
    private final String PROPERTIES_EXTENTION = "properties";
    private File resourceFolder;
    private PropertiesParser parser;

    public PropertiesFilesLoader(PropertiesParser parser, Config conf) {
        String inputFolder = conf.getInput();
        File folder = new File(inputFolder);

        if(!folder.exists()) {
            throw new LogicException("Directory " + inputFolder + " does not exists");
        }

        if(!folder.isDirectory()) {
            throw new LogicException("Path " + inputFolder + " is not a directory");
        }

        if(parser == null) {
            throw new LogicException("parser param can't be null");
        }

        this.parser = parser;
        this.resourceFolder = folder;
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
            fileRecord.put(singlePropName, parser.parseLineToObjectsList(classesListStr));
        }
        return fileRecord;
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
