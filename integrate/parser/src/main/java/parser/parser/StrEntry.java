package parser.parser;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 16.03.12
 */
public class StrEntry implements Map.Entry<String, String>{
    private String key;
    private String value;

    public StrEntry() {
    }

    public StrEntry(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String setValue(String value) {
        String old = this.value;
        this.value = value;
        return old;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StrEntry strEntry = (StrEntry) o;

        if (key != null ? !key.equals(strEntry.key) : strEntry.key != null) return false;
        if (value != null ? !value.equals(strEntry.value) : strEntry.value != null) return false;

        return true;
    }
    
    public int length() {
        return key.length() + value.length() + 2; // add two brackets length = 2
    }

    @Override
    public String toString() {
        return "StrEntry{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
