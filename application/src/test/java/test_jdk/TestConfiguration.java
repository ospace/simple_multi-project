package test_jdk;

import java.io.FileInputStream;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tistory.ospace.common.util.CmmUtils;

public class TestConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(TestConfiguration.class);

    @Before
    public void setUp() throws Exception {
    }

    private String propertiesFilename() {
        String path = TestConfiguration.class.getResource("").getPath();
        return path + "/Test.properties";
    }

    @Test
    public void testProperties() throws Exception {
        FileInputStream inStream = new FileInputStream(propertiesFilename());
        Properties prop = new Properties();
        prop.load(inStream);
        logger.info("null->{}", (String) null);
        logger.info("empty(without equal)={}", prop.getProperty("empty"));
        logger.info("empty1(no exist)={}", prop.getProperty("empty1"));
        logger.info("empty2(with emtpy)={}", prop.getProperty("empty2"));

        logger.info("aa.name={}", prop.getProperty("aa.name"));

        String like = prop.getProperty("aa.like");

        logger.info("aa.like={}", like);

        logger.info("aa.like={}", CmmUtils.toJsonString(like.split(",", -1)));

    }
}

