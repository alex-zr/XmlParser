package parser.parser2;

import parser.common.LogicException;
import parser.common.ParseException;
import parser.configuration.Config;
import parser.parser.PropertiesParser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 10.04.12
 */
public class PropertiesParser2Impl implements PropertiesParser {
    private Config conf;
    private Instantiator inst;

    public PropertiesParser2Impl(Config conf, Instantiator inst) {
        if (conf == null) {
            throw new LogicException("conf can't be null");
        }
        if (inst == null) {
            throw new LogicException("inst can't be null");
        }

        this.conf = conf;
        this.inst = inst;
    }

    /**
     * Recursively parse objects from properties line
     *
     * @param propertyLine line with object structure without line name
     * @return line objects list
     */
    public List<Object> parseLineToObjectsList(String propertyLine) {
        if (propertyLine.contains(" ")) {
            throw new ParseException("String " + propertyLine + " contains space character");
        }
        List<Object> objects = new ArrayList<Object>();
        List<Record> records = buildObjectsByContent(propertyLine, 0, propertyLine.length());
        for (Record record : records) {
            try {
                objects.add(inst.instanceObject(record, null));
            } catch (InstantiationException e) {
                // TODO
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO
                e.printStackTrace();
            }
        }

        return objects;
    }

    public List<Record> buildObjectsByContent(String content, int start, int end) {
        List<Record> objects = new ArrayList<Record>();
        while (start < end) {
            Record record = getInstance(content, start, end);
            objects.add(record);
            start += record.getLength();
            start++; // skip comma
        }
        return objects;
    }

    public Record getInstance(String content, int idx, int end) {
        Record record;
        if ((record = createField(content, idx)) != null) {
            return record;
        } else if ((record = createClass(content, idx)) != null) {
            return record;
        } else if (isList(content, idx)) {
            return new ListRecord();
        } else if (isSet(content, idx)) {
            return new SetRecord();
        } else if ((record = createValue(content, idx)) != null) {
            return record;
        } else {
            throw new ParseException("Unknown element");
        }
    }

    private Record createValue(String content, int idx) {
        int classDelimIdx = getClassDelimIdx(content, idx);
        int rightParentesisIdx = getRightParenthesisIdx(content, idx);

        if (classDelimIdx < 0) {
            if (rightParentesisIdx < 0) {
                classDelimIdx = content.length();
            } else {
                classDelimIdx = rightParentesisIdx;
            }
        } else if (rightParentesisIdx < classDelimIdx) {
            classDelimIdx = rightParentesisIdx;
        }

        String contValue = content.substring(idx, classDelimIdx);
        if (contValue.isEmpty()) { // empty content value
            return null;
        }
        return new ValueRecord(contValue);
    }

    private boolean isSet(String content, int idx) {
        return false;  //To change body of created methods use File | Settings | File Templates.
    }

    private boolean isList(String content, int idx) {
        return false;  //To change body of created methods use File | Settings | File Templates.
    }

    private Record createField(String content, int idx) {

        int valueDelimIdx = getValueDelimIdx(content, idx);
        if (!isIdentifier(content, idx, valueDelimIdx)) {
            return null;
        }
        String fieldName = content.substring(idx, valueDelimIdx);
        Record fieldValue = getInstance(content, fieldName.length() + idx + 1, content.length());
        int contLen = fieldName.length() + fieldValue.getLength() + 1; // 1 - value delim
        String contVal = content.substring(idx, idx + contLen);
        FieldRecord fieldRecord = new FieldRecord(fieldName, fieldValue, contVal);

        return fieldRecord;
    }

    private Record createClass(String content, int idx) {
        int leftParIdx = getLeftParenthesisIdx(content, idx);
        if (!isIdentifier(content, idx, leftParIdx)) {
            return null;
        }
        String className = content.substring(idx, leftParIdx);
        List<Record> classValue = new ArrayList<Record>();
        if (!isEmptyClassContent(content, idx + className.length())) {
            String classCont = getClassContent(content, idx + className.length()); // 1 - left parantesis
            int start = idx + className.length() + 1; // 1 - left parentesis
            int end = start + classCont.length(); // without right parentesis
            classValue = buildObjectsByContent(content, start, end);
        }
        String classContent = content.substring(idx, idx + className.length() + getElementsContLength(classValue) + 2); // 2 - parenteses
        ClassRecord classRecord = new ClassRecord(className, classValue, classContent);

        return classRecord;
    }

    /**
     * Get content with outer parenteses
     *
     * @param str
     * @param idx
     * @return content string
     */
    private String getClassContent(String str, int idx) {
        StringBuffer content = new StringBuffer();
        LinkedList<Character> bracketsStack = new LinkedList<Character>();
        Character leftBr = conf.getLeftBracket();
        Character rightBr = conf.getRightBracket();
        for (int i = idx; i < str.length(); i++) {
            char currChar = str.charAt(i);
            if (currChar == conf.getLeftBracket()) {
                bracketsStack.push(currChar);
            } else if (currChar == rightBr && leftBr.equals(bracketsStack.peek())) {
                bracketsStack.pop();
                if (bracketsStack.isEmpty()) {
                    content.append(rightBr);
                    removeOuterChars(content);
                    return content.toString();
                }
            }
            if (i != str.length() - 1) { // is not parentesis last char
                content.append(currChar);
            }
        }
        removeOuterChars(content);
        return content.toString();
    }

    private void removeOuterChars(StringBuffer str) {
        str.deleteCharAt(0);
        str.deleteCharAt(str.length() - 1);
    }

    private int getElementsContLength(List<Record> classValue) {
        int contLen = 0;
        for (Record rec : classValue) {
            contLen += rec.getLength();
        }
        if (contLen != 0) {
            contLen += classValue.size() - 1; // comas count
        }
        return contLen;
    }

    private boolean isEmptyClassContent(String content, int idx) {
        return content.charAt(idx + 1) == conf.getRightBracket();
    }

    private boolean hasParentheses(String str, int idx) {
        return hasLeftParenthesis(str, idx) && hasRightParenthesis(str, idx);
    }

    private boolean hasLeftParenthesis(String str, int idx) {
        return getLeftParenthesisIdx(str, idx) >= 0;
    }

    private int getLeftParenthesisIdx(String str, int idx) {
        return str.indexOf(String.valueOf(conf.getLeftBracket()), idx);
    }

    private boolean hasRightParenthesis(String str, int idx) {
        return getRightParenthesisIdx(str, idx) >= 0;
    }

    private int getValueDelimIdx(String str, int idx) {
        return str.indexOf(String.valueOf(conf.getValueDelimiter()), idx);
    }

    private int getClassDelimIdx(String str, int idx) {
        return str.indexOf(String.valueOf(conf.getClassDelimiter()), idx);
    }

    private int getRightParenthesisIdx(String str, int idx) {
        return str.indexOf(String.valueOf(conf.getRightBracket()), idx);
    }

    private boolean isIdentifier(String str, int start, int end) {
        if (end < 0) {
            return false;
        }
        String wrongIdentifierSymbols = "`~!@#$%^&*()-_=+/|\\\"\'№;:?<>{}[],. ";
        boolean res = true;
        for (char ch : str.substring(start, end).toCharArray()) {
            if (wrongIdentifierSymbols.contains(String.valueOf(ch))) {
                res = false;
            }
        }
        return res;
    }


}
