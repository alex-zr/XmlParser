package parser.acceptance;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import parser.configuration.Config;
import parser.parser2.ClassRecord;
import parser.parser2.Instantiator;
import parser.parser2.PropertiesParser2Impl;
import parser.parser2.Record;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 25.04.12
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@RunWith(SpringConcordionRunner.class)
public class PropertiesParser2ImplTest {
    private Config mockConfig;
    private PropertiesParser2Impl parser;
    private Instantiator inst;

    public void startUp() {
        mockConfig = getMockConfig();
        inst = new Instantiator(null);
        parser = new PropertiesParser2Impl(mockConfig, inst);
    }

    public List<Record> parse(String content) {
        return parser.buildObjectsByContent(content, 0, content.length());
    }

    public Record getFirst(List<Record> list) {
        return list.get(0);
    }

    public Record getSecond(List<Record> list) {
        return list.get(1);
    }

    public Record getThird(List<Record> list) {
        return list.get(2);
    }

    public List<Record> getValues(ClassRecord record) {
        return record.getValues();
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
