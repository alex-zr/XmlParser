import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import parser.configuration.ContextConfiguration;
import parser.loaders.PropertiesFilesLoader;
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
//    private Config config;
//    private JarClassLoader classLoader;
//    private ReflexUtil util;
//    private Instantiator inst;
    private PropertiesFilesLoader prLoader;
//    private PropertiesParser parser;
    private ModelXmlWriter writer;

    private void init() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                ContextConfiguration.class);
//        config = (Config) context.getBean("config");
//        classLoader = (JarClassLoader) context.getBean("classLoader");
//        util = (ReflexUtil) context.getBean("ReflexUtil");
//        inst = (Instantiator) context.getBean("inst");
//        parser = (PropertiesParser) context.getBean("parser");
        prLoader = (PropertiesFilesLoader) context.getBean("loader");
        writer = (ModelXmlWriter) context.getBean("writer");

//        config = new Config();
//        config.load();
//        classLoader = new JarClassLoader(config.getJarPath());
//        util = new ReflexUtil(config);
//        inst = new Instantiator(classLoader.getClassesMap());
//        parser = new PropertiesParser2Impl(config, inst);
//        prLoader = new PropertiesFilesLoader(parser, config);
//        writer = new ModelXmlWriter(config.getOutput());
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
