package com.config;

import com.util.Constant;
import com.util.Encryption.EncryptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

@Slf4j
public class PropertyConfig implements ApplicationContextInitializer<ConfigurableWebApplicationContext> {
    public void initialize(ConfigurableWebApplicationContext ctx) {
        Map<String, Object> properties = null;
        Map<String, Object> en_properties = new HashMap<>();
        Map<String, Object> db_properties = new HashMap<>();
        Map<String, Object> ented_properties = new HashMap<>();
        Map<String, Object> path_properties = new HashMap<>();
        EncryptionService encryptionService = new EncryptionService();
        try {
            ResourcePropertySource propertySource = new ResourcePropertySource(new ClassPathResource("/config.properties"));
            properties = propertySource.getSource();
            log.info(" ============================ config.properties file DATABASE keys start ============================");
            properties.forEach((key, value) -> {
                /**
                 * PR
                 * @Date 2022-07-26
                 * @Author kimwoosik
                 * @Description
                 * Database USERNAME, PASSWORD, URL Encrypt -> Decrypt
                 * */
                try {
                    if (key.contains("DATABASE." + Constant.DatabaseSetting.DATABASE_SOURCE)) {
                        if (key.contains("USERNAME")) {
                            log.info("DATABASE_USERNAME = {}", encryptionService.decryptAES((String) value));
                            db_properties.put("DATABASE_USERNAME", encryptionService.decryptAES((String) value));
                        } else if (key.contains("PASSWORD")) {
                            log.info("DATABASE_PASSWORD = {}", encryptionService.decryptAES((String) value));
                            db_properties.put("DATABASE_PASSWORD", encryptionService.decryptAES((String) value));
                        } else if (key.contains("URL")) {
                            log.info("DATABASE_URL = {}", encryptionService.decryptAES((String) value));
                            db_properties.put("DATABASE_URL", encryptionService.decryptAES((String) value));
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            ctx.getEnvironment().getPropertySources().addLast(new MapPropertySource("db_props", db_properties));
            log.info(" ============================ config.properties file DATABASE keys end ============================");

            propertySource = new ResourcePropertySource(new ClassPathResource("/config.properties"));
            properties = propertySource.getSource();
            log.info(" ============================ config.properties file ENCRYPTED keys start ============================");
            properties.forEach((key, value) -> {
                /**
                 * PR
                 * @Date 2022-07-26
                 * @Author kimwoosik
                 * @Description
                 * Encrypted Property -> Decrypt
                 * In config.properties
                 * */
                try {
                    if (key.contains("ENCRYPTED")) {
                        log.info("{} = {}", key.substring(key.indexOf("ENCRYPTED") + 10), encryptionService.decryptAES((String) value));
                        ented_properties.put(key.substring(key.indexOf("ENCRYPTED") + 10), encryptionService.decryptAES((String) value));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            ctx.getEnvironment().getPropertySources().addLast(new MapPropertySource("ented_props", ented_properties));
            log.info(" ============================ config.properties file ENCRYPTED keys end ============================");

            propertySource = new ResourcePropertySource(new ClassPathResource("/config.properties"));
            properties = propertySource.getSource();
            log.info(" ============================ config.properties file PATH keys start ============================");
            properties.forEach((key, value) -> {
                /**
                 * PR
                 * @Date 2022-07-27
                 * @Author kimwoosik
                 * @Description
                 * PATH
                 * In config.properties
                 * */
                try {
                    if (key.contains("PATH." + Constant.DatabaseSetting.DATABASE_SOURCE)) {
                        String key_prefix = key.substring(key.indexOf("PATH." + Constant.DatabaseSetting.DATABASE_SOURCE + "."), ("PATH." + Constant.DatabaseSetting.DATABASE_SOURCE + ".").length());
                        log.info("{} = {}", key.substring(key_prefix.length()), value);
                        path_properties.put(key.substring(key_prefix.length()), value);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            ctx.getEnvironment().getPropertySources().addLast(new MapPropertySource("path_props", path_properties));
            log.info(" ============================ config.properties file PATH keys end ============================");

            propertySource = new ResourcePropertySource(new ClassPathResource("/config.properties"));
            ctx.getEnvironment().getPropertySources().addLast(propertySource);
            log.info("Added Config Properties");

            propertySource = new ResourcePropertySource(new ClassPathResource("/key.properties"));
            properties = propertySource.getSource();
            log.info(" ============================ key.properties file keys start ============================");
            properties.forEach((key, value) -> {
                try {
                    log.info(key + " = " + encryptionService.decryptAES((String) value));
                    en_properties.put(key, encryptionService.decryptAES((String) value));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            ctx.getEnvironment().getPropertySources().addLast(new MapPropertySource("props", en_properties));
            log.info(" ============================ key.properties file keys end ============================");
            log.info(" ============================ All  Properties Start ============================");
            final Environment env = ctx.getEnvironment();
            final MutablePropertySources sources = ((AbstractEnvironment) env).getPropertySources();
            StreamSupport.stream(sources.spliterator(), false)
                    .filter(ps -> ps instanceof EnumerablePropertySource)
                    .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                    .flatMap(Arrays::stream)
                    .distinct()
                    .filter(prop -> !(prop.contains("credentials") || prop.contains("password")))
                    .forEach(prop -> log.info("{} = {}", prop, env.getProperty(prop)));
            log.info(" ============================ All  Properties End ============================");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            log.info("PropertyConfig Initialized");
        }
    }
}