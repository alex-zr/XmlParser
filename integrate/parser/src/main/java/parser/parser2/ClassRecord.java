package parser.parser2;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 11.04.12
 */
public class ClassRecord extends Record {
    private String name;
    private List<Record> values;
    private String content;

    public ClassRecord(String className, List<Record> classValues) {
        this.name = className;
        this.values = classValues;
    }

    public ClassRecord(String className, List<Record> classValues, String classContent) {
        this(className, classValues);
        this.content = classContent;
    }

    @Override
    public int getLength() {
        return content.length();
//        int valuesSize = 0;
//        for(Record rec: values) {
//            valuesSize += rec.getLength();
//        }
//        return name.length() + 2 + valuesSize; // 2 - parenteses size
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Record getValue() {
        return null;
    }

    public List<Record> getValues() {
        return values;
    }

    @Override
    public String getContent() {
        return content;
    }
}
