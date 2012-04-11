package parser.parser2;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 11.04.12
 */
public interface Record {
    int getLength();
    String getName();
    Record getValue();
    String getContent();
}
