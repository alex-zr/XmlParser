package parser.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.configuration.Config;
import parser.model.NestedSetTree;
import parser.parser.StrEntry;
import parser.types.Type;
import parser.types.TypeFactory;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 16.03.12
 */
public class ReflexUtil {

    final static Logger logger = LoggerFactory.getLogger(ReflexUtil.class);
    private Config conf;

    public ReflexUtil(Config conf) {
        this.conf = conf;
    }

    public StrEntry buildClassNameAndContent(String parseString, int charIdx) {
        int leftBrIndex = parseString.indexOf(conf.getLeftBracket(), charIdx);
        String className = parseString.substring(charIdx, leftBrIndex).trim();
        charIdx += className.length(); // add class name size
        String classContent = getClassContent(parseString, charIdx);
        charIdx += classContent.length();
        charIdx += 2; // add both brackets size "()"
        return new StrEntry(className, classContent);
    }

    public String getClassContent(String parseString, int charIdx) {
        StringBuffer content = new StringBuffer();
        LinkedList<Character> bracketsStack = new LinkedList<Character>();

        for(int i = charIdx; i < parseString.length(); i++) {
            char currChar = parseString.charAt(i);
            if(currChar == conf.getLeftBracket()) {
                bracketsStack.push(currChar);
            } else if(currChar == conf.getRightBracket() && bracketsStack.peek() == conf.getLeftBracket()) {
                bracketsStack.pop();
                if(bracketsStack.isEmpty()) {
                    return content.toString();
                }
            } else {
                content.append(currChar);
            }
        }
        return content.toString();
    }

    public StrEntry getNextObjectRecord(String parseString, int charIdx) {
        if(isIndexInBounds(parseString, charIdx)) {
            if(charIdx == 0) { // First class
                if(Character.isAlphabetic(parseString.charAt(charIdx))) {
                    StrEntry strEntry = buildClassNameAndContent(parseString, charIdx);
                    charIdx += strEntry.length();
                    return strEntry;
                } else {
                    throw new ParseException("Class name have to begin from alphabetic character");
                }
            } else if(parseString.charAt(charIdx) == conf.getClassDelimiter()) { // Next class, comma separated
                charIdx++; // skip comma sign
                StrEntry strEntry = buildClassNameAndContent(parseString, charIdx);
                charIdx += strEntry.length();
                return strEntry;
            } else {
                throw new ParseException("Classes name have to separated by " + conf.getClassDelimiter() + " character");
            }
        }
        return null ;
    }

    private boolean isIndexInBounds(String parseString, int charIdx) {
        return charIdx < parseString.length();
    }

    public Object instantiateObj(List<NestedSetTree<Class>> classForest, String className, String content) throws IllegalAccessException, InstantiationException {
        for(NestedSetTree<Class> classTree : classForest) {
            if(classTree.getRoot().getName().contains(className)) {
                Object obj = classTree.getRoot().newInstance();
                fillBySimpleContent(obj, content);
                return obj;
            }
        }
        throw new InstantiationException("Can't find " + className + " class");
    }
    
    private boolean fillBySimpleContent(Object obj, String simpleContent) {
        String[] strings = simpleContent.split(String.valueOf(conf.getClassDelimiter()));
        for(String fieldStr : strings) {
            int valueDelimIdx = fieldStr.indexOf(conf.getValueDelimiter());
            String fieldName;
            try {
                fieldName = fieldStr.substring(0, valueDelimIdx);
            } catch (StringIndexOutOfBoundsException e) {
                throw new ParseException("Value delimiter '" + conf.getValueDelimiter() + "' in '" + fieldStr + "' not found", e);
            }
            String value = fieldStr.substring(valueDelimIdx + 1); //skip value delimiter
            try {
                Field field = obj.getClass().getDeclaredField(fieldName);
                Type fieldType = TypeFactory.getType(field.getType().getName());
                fieldType.setValue(obj, fieldName, value);
//                if(fieldType.getName().contains("boolean")) {
//                    field.setBoolean(obj, Boolean.parseBoolean(value));
//                } else if(fieldType.getName().contains("int")) {
//                    //TODO implement
//                } else if(fieldType.getName().contains("String")) {
//                    field.set(obj, value);
//                }
            } catch (NoSuchFieldException e) {
                throw new ParseException("Field " + fieldName + " not found", e);
            } /*catch (IllegalAccessException e) {
                throw new ParseException("Can't access to " + obj.getClass().getName() + " object", e);
            }*/
        }
        return true;
    }
}
