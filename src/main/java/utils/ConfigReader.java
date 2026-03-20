package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class ConfigReader {

    private static  final String path = "src/main/resources/config.properties";

    public static String getProperty(String key)  {

      try(FileInputStream fileInputStream = new FileInputStream(path)){
          Properties properties = new Properties();
          properties.load(Objects.requireNonNull(fileInputStream));
          return properties.getProperty(key);
      } catch (IOException e) {
          throw new RuntimeException("Path is not found");
      }

   }

}
