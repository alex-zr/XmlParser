package parser.configuration;

import parser.common.LogicException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 01.03.12
 */
public class Config {
    private final String CONF_FILE_NAME = "../config.properties";
    private final String INPUT_PATH_PARAM_NAME = "input.path";
    private final String OUTPUT_PATH_PARAM_NAME = "output.path";
    private final String MODEL_JAR_PARAM_NAME = "jar.name";
    private final String CLASS_DELIMITER_PARAM_NAME = "class.delimiter";
    private final String LEFT_BRACKET_PARAM_NAME = "left.bracket";
    private final String RIGHT_BRACKET_PARAM_NAME = "right.bracket";
    private final String VALUE_DELIMITER_PARAM_NAME = "value.delimiter";

    private String input;
    private String output;
    private String jarPath;
    private char classDelimiter;
    private char leftBracket;
    private char rightBracket;
    private char valueDelimiter;

    private static boolean initiated = false;

    public void load() {
        Properties prop = new Properties();

        try {
            InputStream resourceAsStream = this.getClass().getResourceAsStream(CONF_FILE_NAME);
            prop.load(resourceAsStream);

            input = prop.getProperty(INPUT_PATH_PARAM_NAME);
            output = prop.getProperty(OUTPUT_PATH_PARAM_NAME);
            jarPath = prop.getProperty(MODEL_JAR_PARAM_NAME);
            String classDelimiterStr = prop.getProperty(CLASS_DELIMITER_PARAM_NAME);
            String leftBracketStr = prop.getProperty(LEFT_BRACKET_PARAM_NAME);
            String rightBracketStr = prop.getProperty(RIGHT_BRACKET_PARAM_NAME);
            String valueDelimiterStr = prop.getProperty(VALUE_DELIMITER_PARAM_NAME);

            if(input == null || input.isEmpty()) {
                throw new LogicException(INPUT_PATH_PARAM_NAME + " parameter is absent in config");
            }
            if(output == null || output.isEmpty()) {
                throw new LogicException(OUTPUT_PATH_PARAM_NAME + " parameter is absent in config");
            }

            if(jarPath == null || jarPath.isEmpty()) {
                throw new LogicException(MODEL_JAR_PARAM_NAME + "parameter is absent in config");
            }
            
            if(classDelimiterStr == null || classDelimiterStr.isEmpty()) {
                throw new LogicException(CLASS_DELIMITER_PARAM_NAME + "parameter is absent in config");
            } else {
                classDelimiter = classDelimiterStr.charAt(0);
            }
            
            if(leftBracketStr == null || leftBracketStr.isEmpty()) {
                throw new LogicException(LEFT_BRACKET_PARAM_NAME + "parameter is absent in config");
            } else {
                leftBracket = leftBracketStr.charAt(0);
            }

            if(rightBracketStr == null || rightBracketStr.isEmpty()) {
                throw new LogicException(RIGHT_BRACKET_PARAM_NAME + "parameter is absent in config");
            } else {
                rightBracket = rightBracketStr.charAt(0);
            }

            if(valueDelimiterStr == null || valueDelimiterStr.isEmpty()) {
                throw new LogicException(VALUE_DELIMITER_PARAM_NAME + "parameter is absent in config");
            } else {
                valueDelimiter = valueDelimiterStr.charAt(0);
            }

            initiated = true;
        } catch (IOException e) {
            throw new LogicException("Read configuration file error [" + CONF_FILE_NAME + "] " + e);
        }
    }

    public String getInput() {
        ensureInit();
        return input;
    }

    public String getOutput() {
        ensureInit();
        return output;
    }

    public String getJarPath() {
        ensureInit();
        return jarPath;
    }

    public char getClassDelimiter() {
        return classDelimiter;
    }

    public char getLeftBracket() {
        ensureInit();
        return leftBracket;
    }

    public char getRightBracket() {
        ensureInit();
        return rightBracket;
    }

    public char getValueDelimiter() {
        return valueDelimiter;
    }

    private void ensureInit() {
        if(!initiated) {
            throw new IllegalStateException("Configuration is not loaded");
        }
    }

    @Override
    public String toString() {
        return "Config{" +
                "input='" + input + '\'' +
                ", output='" + output + '\'' +
                ", jarPath='" + jarPath + '\'' +
                '}';
    }
}
