package parser.parser;

import org.junit.Before;
import org.junit.Test;
import parser.configuration.Config;
import parser.parser2.ClassRecord;
import parser.parser2.FieldRecord;
import parser.parser2.PropertiesParser2Impl;
import parser.parser2.Record;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 11.04.12
 */
public class PropertiesParser2ImplTest {

    private Config mockConfig;
    private PropertiesParser2Impl factory;

    @Before
    public void startUp() {
        mockConfig = getMockConfig();
    }

    @Test
    public void testBuildClass() {
        factory = new PropertiesParser2Impl(mockConfig);
        String content = "A()";
        List<Record> records = factory.buildObjectsByContent(content, 0, content.length());
        assertEquals(1, records.size());
        assertEquals(3, records.get(0).getLength());
        assertEquals("A", records.get(0).getName());
    }

    @Test
    public void testBuildTwoClasses() {
        factory = new PropertiesParser2Impl(mockConfig);
        String content = "A(),A()";
        List<Record> records = factory.buildObjectsByContent(content, 0, content.length());
        assertEquals(2, records.size());
        assertEquals("A", records.get(0).getName());
        assertEquals("A", records.get(1).getName());
        assertEquals(3, records.get(0).getLength());
        assertEquals(3, records.get(1).getLength());
    }


    @Test
    public void testCreateField() {
        factory = new PropertiesParser2Impl(mockConfig);
        String content = "opa:opapa";
        List<Record> records = factory.buildObjectsByContent(content, 0, content.length());
        assertEquals("opa", records.get(0).getName());
        assertEquals("opapa", records.get(0).getValue().getContent());
    }

//    @Test
//    public void testCreateClass() {
//        factory = new PropertiesParser2Impl(mockConfig);
//        Record record = factory.getInstance("A()", 0);
//        assertEquals("A", record.getName());
//        assertEquals("", record.getValue().getContent());
//    }

    @Test
    public void testCreateClassWithField() {
        factory = new PropertiesParser2Impl(mockConfig);
        String content = "A(opa:opapa)";
        List<Record> records =  factory.buildObjectsByContent(content, 0, content.length());
        ClassRecord record = (ClassRecord)records.get(0);
        assertEquals("A", record.getName());
        assertEquals("opa", record.getValues().get(0).getName());
        assertEquals("opapa", record.getValues().get(0).getValue().getContent());
    }


    @Test
    public void testCreateClassWithClass() {
        factory = new PropertiesParser2Impl(mockConfig);
        String content = "A(opa:A())";
        List<Record> records = factory.buildObjectsByContent(content, 0, content.length());
        ClassRecord record = (ClassRecord)records.get(0);
        assertEquals("A", record.getName());
        FieldRecord record1 = (FieldRecord)record.getValues().get(0);
        assertEquals("opa", record1.getName());
        assertEquals("A", record1.getValue().getName());
        ClassRecord record2 = (ClassRecord) record1.getValue();
        assertEquals(0, record2.getValues().size());
    }


    @Test
    public void testCreateClassWith2Classes() {
        factory = new PropertiesParser2Impl(mockConfig);
        String content = "A(opa:A(),ops:B())";
        List<Record> records = factory.buildObjectsByContent(content, 0, content.length());
        ClassRecord record1 = (ClassRecord)records.get(0);
        assertEquals("A", record1.getName());
        FieldRecord record11 = (FieldRecord)record1.getValues().get(0);
        assertEquals("opa", record11.getName());
        assertEquals("A", record11.getValue().getName());
        FieldRecord record12 = (FieldRecord) record1.getValues().get(1);
        assertEquals("ops", record12.getName());
        assertEquals("B", record12.getValue().getName());

    }

    @Test
    public void testCreateClassWithClassAndField() {
        factory = new PropertiesParser2Impl(mockConfig);
        String content = "A(opa:A(opa:opapa))";
        List<Record> records = factory.buildObjectsByContent(content, 0, content.length());
        ClassRecord record = (ClassRecord)records.get(0);
        assertEquals("A", record.getName());
        FieldRecord record1 = (FieldRecord)record.getValues().get(0);
        assertEquals("opa", record1.getName());
        assertEquals("A", record1.getValue().getName());
        ClassRecord record2 = (ClassRecord) record1.getValue();
        FieldRecord record3 = (FieldRecord)record2.getValues().get(0);
        assertEquals("opa", record3.getName());
        assertEquals("opapa", record3.getValue().getContent());
    }

    @Test
    public void testCreateClassWithClassAnd2Fields() {
        factory = new PropertiesParser2Impl(mockConfig);
        String content = "A(opa:A(opa:opapa,oplya:oplyaplya))";
        List<Record> records = factory.buildObjectsByContent(content, 0, content.length());
        ClassRecord record = (ClassRecord)records.get(0);
        assertEquals("A", record.getName());
        FieldRecord record1 = (FieldRecord)record.getValues().get(0);
        assertEquals("opa", record1.getName());
        assertEquals("A", record1.getValue().getName());
        ClassRecord record2 = (ClassRecord) record1.getValue();
        FieldRecord record3 = (FieldRecord)record2.getValues().get(0);
        assertEquals("opa", record3.getName());
        assertEquals("opapa", record3.getValue().getContent());
        FieldRecord record4 = (FieldRecord)record2.getValues().get(1);
        assertEquals("oplya", record4.getName());
        assertEquals("oplyaplya", record4.getValue().getContent());
    }

    @Test
    public void testCreate2ClassesWithClassAnd2Fields() {
        factory = new PropertiesParser2Impl(mockConfig);
        String content = "A(opa:A(opa:opapa,oplya:oplyaplya),op:B(op:opap,opl:oplyapl))";
        List<Record> records = factory.buildObjectsByContent(content, 0, content.length());
        ClassRecord record = (ClassRecord)records.get(0);
        assertEquals("A", record.getName());

        FieldRecord record1 = (FieldRecord)record.getValues().get(0);
        assertEquals("opa", record1.getName());
        assertEquals("A", record1.getValue().getName());
        ClassRecord record2 = (ClassRecord) record1.getValue();
        FieldRecord record3 = (FieldRecord)record2.getValues().get(0);
        assertEquals("opa", record3.getName());
        assertEquals("opapa", record3.getValue().getContent());
        FieldRecord record4 = (FieldRecord)record2.getValues().get(1);
        assertEquals("oplya", record4.getName());
        assertEquals("oplyaplya", record4.getValue().getContent());

        FieldRecord record11 = (FieldRecord)record.getValues().get(1);
        assertEquals("op", record11.getName());
        assertEquals("B", record11.getValue().getName());
        ClassRecord record21 = (ClassRecord) record11.getValue();
        FieldRecord record31 = (FieldRecord)record21.getValues().get(0);
        assertEquals("op", record31.getName());
        assertEquals("opap", record31.getValue().getContent());
        FieldRecord record41 = (FieldRecord)record21.getValues().get(1);
        assertEquals("opl", record41.getName());
        assertEquals("oplyapl", record41.getValue().getContent());
    }

    private Config getMockConfig() {
        Config mockedConfig = mock(Config.class);
        when(mockedConfig.getClassDelimiter()).thenReturn(',');
        when(mockedConfig.getValueDelimiter()).thenReturn(':');
        when(mockedConfig.getLeftBracket()).thenReturn('(');
        when(mockedConfig.getRightBracket()).thenReturn(')');

        return mockedConfig;
    }
}
