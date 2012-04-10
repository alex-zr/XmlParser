package parser.common;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 15.03.12
 */
public class ParseException extends RuntimeException {
    public ParseException(String mess) {
        super(mess);
    }

    public ParseException(String mess, Exception e) {
        super(mess, e);
    }

    public ParseException(Exception e) {
        super(e);
    }
}
