package socialnetwork.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    public static Properties getProperties() {
        try(InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("config.properties"))
        {
            Properties properties=new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config properties");
        }
    }
}
