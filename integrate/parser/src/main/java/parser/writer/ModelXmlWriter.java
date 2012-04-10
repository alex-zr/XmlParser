package parser.writer;

import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 01.03.12
 */
public class ModelXmlWriter {
    final static Logger logger = LoggerFactory.getLogger(ModelXmlWriter.class);
    private String outputPath;
    private XStream xstream;
//    private Map<String,Object[]> objMap;

    public ModelXmlWriter(String outputPath) {
        this.outputPath = outputPath;
        this.xstream = xstream = new XStream();
//        this.objMap = objMap;
    }

    public void write(Map<String, List<Object>> objMap) {
        for (Map.Entry<String, List<Object>> line : objMap.entrySet()) {
            alisedObjectsForXStream(line);
            String xml = xstream.toXML(line.getValue());

            logger.info(line.getKey() + ",\n" + xml);
        }
    }

    private void alisedObjectsForXStream(Map.Entry<String, List<Object>> line) {
        for (Object obj : line.getValue()) {
            xstream.alias(obj.getClass().getSimpleName(), obj.getClass());
        }
    }
}
