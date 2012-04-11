package parser.parser2;

import parser.common.LogicException;
import parser.common.ParseException;
import parser.configuration.Config;
import parser.parser.PropertiesParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 10.04.12
 */
public class PropertiesParser2Impl implements PropertiesParser {
    private Config conf;

    public PropertiesParser2Impl(Config conf) {
        if (conf == null) {
            throw new LogicException("conf can't be null");
        }

        this.conf = conf;
    }

    /**
     * Recursively parse objects from properties line
     *
     * @param propertyLine line with object structure without line name
     * @return line objects list
     */
    public List<Object> parseLineToObjectsList(String propertyLine) {
        List<Object> objects = new ArrayList<Object>();
        List<Record> records = buildObjectsByContent(propertyLine, 0);
        for(Record record : records) {
            objects.add(buildObject(record));
        }
        return objects;
    }

    public List<Record> buildObjectsByContent(String content, int idx) {
        List<Record> objects = new ArrayList<Record>();
        while (idx < content.length()) {
            Record record = getInstance(content, idx);
            objects.add(record);
            idx += record.getLength();
        }
        return objects;
    }

    public Record getInstance(String content, int idx) {
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
        int valueDelimIdx = getValueDelimIdx(content, idx);
        if (classDelimIdx < 0) {
            if(rightParentesisIdx < 0) {
                classDelimIdx = content.length();
            } else {
                classDelimIdx = rightParentesisIdx;
            }
        }

        return new ValueRecord(content.substring(idx, classDelimIdx));//content.substring(idx, getClassDelimIdx(content, idx));
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
        Record fieldValue = getInstance(content, fieldName.length() + idx + 1);
        FieldRecord fieldRecord = new FieldRecord(fieldName, fieldValue);

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
            classValue = buildObjectsByContent(content, className.length() + idx + 1);
        }
        ClassRecord classRecord = new ClassRecord(className, classValue);

        return classRecord;
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
        String wrongIdentifierSymbols = "`~!@#$%^&*()-_=+/|\\\"\'№;:?<>{}[],.";
        boolean res = true;
        for (char ch : str.substring(start, end).toCharArray()) {
            if (wrongIdentifierSymbols.contains(String.valueOf(ch))) {
                res = false;
            }
        }
        return res;
    }

    /**
     * Recursive build object by class name and object content
     *
     * @param content
     * @return
     */
    private Object buildObject(Record content) {
        return null;
    }
}
