import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.configuration.Config;
import parser.loaders.JarClassLoader;
import parser.loaders.PropertiesFilesLoader;
import parser.parser.PropertiesParser;
import parser.parser.ReflexUtil;
import parser.parser2.Instantiator;
import parser.parser2.PropertiesParser2Impl;
import parser.writer.ModelXmlWriter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 14.03.12
 */
public class Main {
    final static Logger logger = LoggerFactory.getLogger(Main.class);
    private JarClassLoader classLoader;
    private Instantiator inst;
    private PropertiesFilesLoader prLoader;
    private Config config;
    private PropertiesParser parser;
    private ModelXmlWriter writer;
    private ReflexUtil util;

    private void init() {
        config = new Config();
        config.load();
        classLoader = new JarClassLoader(config.getJarPath());
        util = new ReflexUtil(config);
        inst = new Instantiator(classLoader.getClassesMap());
        parser = new PropertiesParser2Impl(config, inst);
        prLoader = new PropertiesFilesLoader(parser, config);
        writer = new ModelXmlWriter(config.getOutput());
    }

    public static void main(String[] args) {
        //TODO check input params, if absent getById from properties
        Main main = new Main();
        main.init();
        Map<String, Map<String, List<Object>>> objects = main.prLoader.readPropertieFilesMap();

        Set<String> fileNames = objects.keySet();

        logger.info(objects.toString());

        for(String name : fileNames) {
            main.writer.write(objects.get(name));
        }
    }
}
