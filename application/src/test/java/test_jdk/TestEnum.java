package test_jdk;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestEnum {
    private static final Logger logger = LoggerFactory.getLogger(TestEnum.class);

    enum Foo {
        FOO("foo");
        public final String code;

        Foo(String code) {
            this.code = code;
        }

        public String toString() {
            return this.code;
        }
    }

    String printFooToString(String code) {
        return code;
    }

    @Test
    public void testEnumString() {
        logger.info("enum FOO = {}", Foo.FOO);
        logger.info("toString: {}", printFooToString("" + Foo.FOO));
    }

}
