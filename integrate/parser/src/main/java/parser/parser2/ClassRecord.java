package parser.parser2;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 11.04.12
 */
public class ClassRecord implements Record {
    private String name;
    private List<Record> values;

    public ClassRecord(String className, List<Record> classValues) {
        this.name = className;
        this.values = classValues;
    }

    @Override
    public int getLength() {
        int valuesSize = 0;
        for(Record rec: values) {
            valuesSize += rec.getLength();
        }
        return name.length() + valuesSize;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Record getValue() {
        return values.get(0);
    }

    @Override
    public String getContent() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
