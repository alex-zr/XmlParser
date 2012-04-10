package parser.common;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 01.03.12
 */
public class LogicException extends RuntimeException {
    public LogicException(String mess) {
        super(mess);
    }

    public LogicException(String mess, Exception e) {
        super(mess, e);
    }

    public LogicException(Exception e) {
        super(e);
    }
}
