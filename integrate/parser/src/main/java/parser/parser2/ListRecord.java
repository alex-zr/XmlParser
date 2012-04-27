package parser.parser2;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 11.04.12
 */
public class ListRecord extends ClassRecord {
    public ListRecord(String listName, List<Record> listValues) {
        super(listName, listValues);
    }

    public ListRecord(String listName, List<Record> listValues, String listContent) {
        super(listName, listValues, listContent);
    }
}