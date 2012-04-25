package parser.parser;

import org.junit.Test;
import parser.parser2.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 25.04.12
 */
public class InstantiatorTest {

    private Instantiator inst;

    @Test
    public void testInstantiateEmptyClass() throws IllegalAccessException, InstantiationException {
        inst = new Instantiator(getClassesMap());
        Record record = new ClassRecord("String", new ArrayList<Record>());
        Object object = inst.instanceObject(record, null);
        assertEquals(object.getClass().getSimpleName(), "String");
    }

    @Test
    public void testInstantiateClassWithIntField() throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        inst = new Instantiator(getClassesMap());
        int value = 12;
        String fieldName = "offset";
        ClassRecord record = new ClassRecord("String", new ArrayList<Record>());
        FieldRecord fRec = new FieldRecord(fieldName, new ValueRecord(String.valueOf(value)));
        record.getValues().add(fRec);
        Object object = inst.instanceObject(record, null);
        assertEquals(object.getClass().getSimpleName(), "String");
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        assertEquals(field.getInt(object), value);

    }
    @Test
    public void testInstantiateClassWithFields() throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        inst = new Instantiator(getClassesMap());
        String testClassName = "FileHandler";
        String value1 = "12";
        int value2 = 12;
        String fieldName1 = "pattern";
        String fieldName2 = "count";
        ClassRecord record = new ClassRecord(testClassName, new ArrayList<Record>());
        FieldRecord fRec1 = new FieldRecord(fieldName1, new ValueRecord(String.valueOf(value1)));
        FieldRecord fRec2 = new FieldRecord(fieldName2, new ValueRecord(String.valueOf(value2)));
        record.getValues().add(fRec1);
        record.getValues().add(fRec2);
        Object object = inst.instanceObject(record, null);
        assertEquals(object.getClass().getSimpleName(), testClassName);
        Field field1 = object.getClass().getDeclaredField(fieldName1);
        Field field2 = object.getClass().getDeclaredField(fieldName2);
        field1.setAccessible(true);
        field2.setAccessible(true);
        assertEquals(field1.get(object), value1);
        assertEquals(field2.getInt(object), value2);

    }

    private Map<String, Class> getClassesMap() {
        Map<String, Class> map = new HashMap<String, Class>();
        map.put("String", String.class);
        map.put("Throwable", Throwable.class);
        map.put("FileHandler", FileHandler.class);


        return map;
    }
}
