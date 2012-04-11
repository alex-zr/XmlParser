package parser.parser;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import parser.configuration.Config;
import parser.parser2.PropertiesParser2Impl;
import parser.parser2.Record;

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
    public void testBuildField() {

    }
    @Ignore
    @Test
    public void testCreateField() {
        factory = new PropertiesParser2Impl(mockConfig);
        Record record = factory.getInstance("opa:opapa", 0);
        assertEquals("opa", record.getName());
        assertEquals("opapa", record.getValue().getContent());
    }
    @Ignore
    @Test
    public void testCreateClass() {
        factory = new PropertiesParser2Impl(mockConfig);
        Record record = factory.getInstance("A()", 0);
        assertEquals("A", record.getName());
        assertEquals("", record.getValue().getContent());
    }
    @Ignore
    @Test
    public void testCreateClassWithField() {
        factory = new PropertiesParser2Impl(mockConfig);
        Record record = factory.getInstance("A(opa:opapa)", 0);
        assertEquals("A", record.getName());
        assertEquals("opa", record.getValue().getName());
        assertEquals("opapa", record.getValue().getValue().getContent());
    }
    @Ignore
    @Test
    public void testCreateClassWithClass() {
        factory = new PropertiesParser2Impl(mockConfig);
        Record record = factory.getInstance("A(opa:A())", 0);
        assertEquals("A", record.getName());
        assertEquals("opa", record.getValue().getName());
        assertEquals("A", record.getValue().getValue().getName());
    }
    @Ignore
    @Test
    public void testCreateClassWithClassAndField() {
        factory = new PropertiesParser2Impl(mockConfig);
        Record record = factory.getInstance("A(opa:A(opa:opapa))", 0);
        assertEquals("A", record.getName());
        assertEquals("opa", record.getValue().getName());
        assertEquals("A", record.getValue().getValue().getName());
        assertEquals("opa", record.getValue().getValue().getValue().getName());
        assertEquals("opapa", record.getValue().getValue().getValue().getValue().getContent());
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
