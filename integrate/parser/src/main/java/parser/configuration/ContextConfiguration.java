package parser.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import parser.loaders.JarClassLoader;
import parser.loaders.PropertiesFilesLoader;
import parser.parser2.Instantiator;
import parser.parser2.PropertiesParser2Impl;
import parser.writer.ModelXmlWriter;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 25.04.12
 */
@Configuration
public class ContextConfiguration {
    @Bean
    public Config config() {
        Config config = new Config();
        config.load();
        return config;
    }

    @Bean
    public JarClassLoader classLoader() {
       return new JarClassLoader(config().getJarPath());
    }

    @Bean
    public Instantiator instantiator() {
        return new Instantiator(classLoader().getClassesMap());
    }

    @Bean
    public PropertiesParser2Impl parser() {
        return new PropertiesParser2Impl(config(), instantiator());
    }

    @Bean
    public PropertiesFilesLoader loader() {
        return new PropertiesFilesLoader(parser(), config());
    }

    @Bean
    public ModelXmlWriter writer() {
        return new ModelXmlWriter(config().getOutput());
    }
}
