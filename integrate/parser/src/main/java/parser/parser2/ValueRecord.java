package parser.parser2;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 11.04.12
 */
public class ValueRecord extends Record {
    private String content;

    public ValueRecord(String content) {
        this.content = content;
    }

    @Override
    public int getLength() {
        return content.length();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Record getValue() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getContent() {
        return content;
    }
}
