package parser.parser2;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 11.04.12
 */
public class FieldRecord extends Record {
    private String name;
    private Record value;
    private String content;

    public FieldRecord(String name, Record value) {
        this.name = name;
        this.value = value;
    }

    public FieldRecord(String name, Record value, String content) {
        this(name, value);
        this.content = content;
    }

    @Override
    public int getLength() {
        return name.length() + value.getLength() + 1; // 1 - value delimiter
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
        return content;
    }
}
