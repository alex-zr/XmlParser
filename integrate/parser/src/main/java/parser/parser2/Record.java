package parser.parser2;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 11.04.12
 */
public abstract class Record {
    public abstract int getLength();
    public abstract String getName();
    public abstract Record getValue();
    public abstract String getContent();

    @Override
    public String toString() {
        return getContent();
    }
}
