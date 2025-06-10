package io.lolyay.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlParser {

    public static Map<String, String> loadYaml(InputStream filePath) {
        Yaml yaml = new Yaml();
        try {
            return yaml.load(filePath);
        } catch (Exception e) {
            return null;
        }
    }
}