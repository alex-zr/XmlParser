package parser.parser;

import parser.common.ReflexUtil;
import parser.configuration.Config;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 15.03.12
 */
public class LineParseIterator implements Iterable<Map.Entry<String, String>>, Iterator<Map.Entry<String, String>> {
    
    private int charIdx = 0;
    private String parseString;
    private Config conf;
    private ReflexUtil util;

    public LineParseIterator(ReflexUtil util, Config conf, String parseString) {
        this.util = util;
        this.conf = conf;
        this.parseString = parseString;
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return new LineParseIterator(util, conf, parseString);
    }

    @Override
    public boolean hasNext() {
        return hasStrBrackets(parseString);
    }

    private boolean hasStrBrackets(String str) {
        return str.indexOf(String.valueOf(conf.getLeftBracket()), charIdx) >= 0
                && str.indexOf(String.valueOf(conf.getRightBracket()), charIdx) >= 0;
    }
    
    private boolean hasStringBracketsOrComa(String str) {
        return hasStrBrackets(str) || str.indexOf(String.valueOf(conf.getClassDelimiter()), charIdx) >= 0;
    }

    @Override
    public Map.Entry<String, String> next() {
        StrEntry objectRecord = util.getNextObjectRecord(parseString, charIdx);
        charIdx += objectRecord.length();
        return objectRecord;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
