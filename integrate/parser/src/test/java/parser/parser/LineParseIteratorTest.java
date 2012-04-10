package parser.parser;

import org.junit.Before;
import org.junit.Test;
import parser.common.ReflexUtil;
import parser.configuration.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 16.03.12
 */
public class LineParseIteratorTest {
    private LineParseIterator itr;
    private Config mockConfig;
    private ReflexUtil util;

    @Before
    public void startUp() {
        mockConfig = getMockConfig();
        util = new ReflexUtil(mockConfig);
    }

    @Test
    public void testPositiveSingle() {
        String parseString = "Address(city:Kiev)";
        StrEntry expEntry = new StrEntry("Address", "city:Kiev");
        itr = new LineParseIterator(util, mockConfig, parseString);

        assertTrue(itr.hasNext());
        assertEquals(expEntry, itr.next());
    }

    @Test
    public void testPositivePair() {
        String parseString = "Student(name:Ivan),Group(name:cs-52)";
        StrEntry expEntry1 = new StrEntry("Student", "name:Ivan");
        StrEntry expEntry2 = new StrEntry("Group", "name:cs-52");
        itr = new LineParseIterator(util, mockConfig, parseString);

        assertTrue(itr.hasNext());
        assertEquals(expEntry1, itr.next());
        assertTrue(itr.hasNext());
        assertEquals(expEntry2, itr.next());
    }

    public void testPositiveNested() {
        // TODO
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
