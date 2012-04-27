package parser.parser;

import org.junit.Before;
import org.junit.Test;
import parser.configuration.Config;
import parser.parser2.*;

import java.util.List;

import static junit.framework.Assert.assertTrue;
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
    private PropertiesParser2Impl parser;
    private Instantiator inst;

    @Before
    public void startUp() {
        mockConfig = getMockConfig();
        inst = new Instantiator(null);
    }

    @Test
    public void testBuildClass() {
        parser = new PropertiesParser2Impl(mockConfig, inst);
        String content = "A()";
        List<Record> records = parser.buildObjectsByContent(content, 0, content.length());
        assertEquals(1, records.size());
        assertEquals(3, records.get(0).getLength());
        assertEquals("A", records.get(0).getName());
    }

    @Test
    public void testBuildTwoClasses() {
        parser = new PropertiesParser2Impl(mockConfig, inst);
        String content = "A(),A()";
        List<Record> records = parser.buildObjectsByContent(content, 0, content.length());
        assertEquals(2, records.size());
        assertEquals("A", records.get(0).getName());
        assertEquals("A", records.get(1).getName());
        assertEquals(3, records.get(0).getLength());
        assertEquals(3, records.get(1).getLength());
    }


    @Test
    public void testCreateField() {
        parser = new PropertiesParser2Impl(mockConfig, inst);
        String content = "opa:opapa";
        List<Record> records = parser.buildObjectsByContent(content, 0, content.length());
        assertEquals("opa", records.get(0).getName());
        assertEquals("opapa", records.get(0).getValue().getContent());
    }

//    @Test
//    public void testCreateClass() {
//        parser = new PropertiesParser2Impl(mockConfig);
//        Record record = parser.getInstance("A()", 0);
//        assertEquals("A", record.getName());
//        assertEquals("", record.getValue().getContent());
//    }

    @Test
    public void testCreateClassWithField() {
        parser = new PropertiesParser2Impl(mockConfig, inst);
        String content = "A(opa:opapa)";
        List<Record> records =  parser.buildObjectsByContent(content, 0, content.length());
        ClassRecord record = (ClassRecord)records.get(0);
        assertEquals("A", record.getName());
        assertEquals("opa", record.getValues().get(0).getName());
        assertEquals("opapa", record.getValues().get(0).getValue().getContent());
    }


    @Test
    public void testCreateClassWithClass() {
        parser = new PropertiesParser2Impl(mockConfig, inst);
        String content = "A(opa:A())";
        List<Record> records = parser.buildObjectsByContent(content, 0, content.length());
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
        parser = new PropertiesParser2Impl(mockConfig, inst);
        String content = "A(opa:A(),ops:B())";
        List<Record> records = parser.buildObjectsByContent(content, 0, content.length());
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
        parser = new PropertiesParser2Impl(mockConfig, inst);
        String content = "A(opa:A(opa:opapa))";
        List<Record> records = parser.buildObjectsByContent(content, 0, content.length());
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
        parser = new PropertiesParser2Impl(mockConfig, inst);
        String content = "A(opa:A(opa:opapa,oplya:oplyaplya))";
        List<Record> records = parser.buildObjectsByContent(content, 0, content.length());
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
        parser = new PropertiesParser2Impl(mockConfig, inst);
        String content = "A(opa:A(opa:opapa,oplya:oplyaplya),op:B(op:opap,opl:oplyapl))";
        List<Record> records = parser.buildObjectsByContent(content, 0, content.length());
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

    @Test
    public void testCreateEmptyList() {
        parser = new PropertiesParser2Impl(mockConfig, inst);
        String content = "List[]";
        List<Record> records = parser.buildObjectsByContent(content, 0, content.length());
        ListRecord record = (ListRecord)records.get(0);
        assertEquals("List", record.getName());
        assertEquals(6, record.getLength());
        assertTrue(record.getValues().isEmpty());
    }

    @Test
    public void testCreateTwoEmptyLists() {
        parser = new PropertiesParser2Impl(mockConfig, inst);
        String content = "List[],List[]";
        List<Record> records = parser.buildObjectsByContent(content, 0, content.length());
        ListRecord record1 = (ListRecord)records.get(0);
        assertEquals("List", record1.getName());
        assertEquals(6, record1.getLength());
        assertTrue(record1.getValues().isEmpty());

        ListRecord record2 = (ListRecord)records.get(0);
        assertEquals("List", record2.getName());
        assertEquals(6, record2.getLength());
        assertTrue(record2.getValues().isEmpty());
    }

    @Test
    public void testCreateListWithClass() {
        parser = new PropertiesParser2Impl(mockConfig, inst);
        String content = "List[A()]";
        List<Record> records = parser.buildObjectsByContent(content, 0, content.length());
        ListRecord listRecord = (ListRecord)records.get(0);
        assertEquals("List", listRecord.getName());
        assertEquals(9, listRecord.getLength());
        assertEquals(1, listRecord.getValues().size());

        ClassRecord classRecord = (ClassRecord)listRecord.getValues().get(0);
        assertEquals("A", classRecord.getName());
        assertEquals(3, classRecord.getLength());
        assertTrue(classRecord.getValues().isEmpty());
    }

    @Test
    public void testCreateListWithClassWithField() {
        parser = new PropertiesParser2Impl(mockConfig, inst);
        String classContent = "A(name:value)";
        String listContent = "List[" + classContent + "]";
        List<Record> records = parser.buildObjectsByContent(listContent, 0, listContent.length());
        ListRecord listRecord = (ListRecord)records.get(0);
        assertEquals("List", listRecord.getName());
        assertEquals(listContent.length(), listRecord.getLength());
        assertEquals(1, listRecord.getValues().size());

        ClassRecord classRecord = (ClassRecord)listRecord.getValues().get(0);
        assertEquals("A", classRecord.getName());
        assertEquals(classContent.length(), classRecord.getLength());
        assertEquals(1, classRecord.getValues().size());

        FieldRecord fieldRecord = (FieldRecord)classRecord.getValues().get(0);
        assertEquals("name", fieldRecord.getName());
        assertEquals("value", fieldRecord.getValue().getContent());
    }

    @Test
    public void testCreateListWith2ClassesWith2Fields() {
        parser = new PropertiesParser2Impl(mockConfig, inst);
        String classContent1 = "A(name1:value1)";
        String classContent2 = "Class(name2:value2)";
        String listContent = "List[" + classContent1 + "," + classContent2 + "]";
        List<Record> records = parser.buildObjectsByContent(listContent, 0, listContent.length());

        ListRecord listRecord = (ListRecord)records.get(0);
        assertEquals("List", listRecord.getName());
        assertEquals(listContent.length(), listRecord.getLength());
        assertEquals(2, listRecord.getValues().size());

        ClassRecord classRecord1 = (ClassRecord)listRecord.getValues().get(0);
        assertEquals("A", classRecord1.getName());
        assertEquals(classContent1.length(), classRecord1.getLength());
        assertEquals(1, classRecord1.getValues().size());

        FieldRecord fieldRecord1 = (FieldRecord)classRecord1.getValues().get(0);
        assertEquals("name1", fieldRecord1.getName());
        assertEquals("value1", fieldRecord1.getValue().getContent());

        ClassRecord classRecord2 = (ClassRecord)listRecord.getValues().get(1);
        assertEquals("Class", classRecord2.getName());
        assertEquals(classContent2.length(), classRecord2.getLength());
        assertEquals(1, classRecord2.getValues().size());

        FieldRecord fieldRecord2 = (FieldRecord)classRecord2.getValues().get(0);
        assertEquals("name2", fieldRecord2.getName());
        assertEquals("value2", fieldRecord2.getValue().getContent());
    }

    private Config getMockConfig() {
        Config mockedConfig = mock(Config.class);
        when(mockedConfig.getClassDelimiter()).thenReturn(',');
        when(mockedConfig.getValueDelimiter()).thenReturn(':');
        when(mockedConfig.getLeftBracket()).thenReturn('(');
        when(mockedConfig.getRightBracket()).thenReturn(')');
        when(mockedConfig.getLeftColBracket()).thenReturn('[');
        when(mockedConfig.getRightColBracket()).thenReturn(']');

        return mockedConfig;
    }
}
