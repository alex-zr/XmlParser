package parser.parser2;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 11.04.12
 */
public class FieldRecord implements Record {
    private String name;
    private Record value;

    public FieldRecord(String name, Record value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int getLength() {
        return name.length() + value.getLength();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Record getValue() {
        return value;
    }

    @Override
    public String getContent() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
