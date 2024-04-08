package test_jdk;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestGeneric {
    private static final Logger logger = LoggerFactory.getLogger(TestGeneric.class);

    @Before
    public void setUp() throws Exception {
    }

    public static <T> T makeInstance(Class<T> type) {
        try {
            return type.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static <R, P> R convert(P param, Class<R> type) {
        R ret = makeInstance(type);
        //convertTo(param, ret.getClass());

        return ret;
    }
	
/*	private static Integer convertTo(String l, Class<Integer> clazz) {
		//r = Integer.parseInt(l);
		return null;
	}*/

    @Test
    public void testSample() throws Exception {
        String val = "10";

        Integer num = convert(val, Integer.class);

        logger.info("convert {} to {}", val, num);
    }

    static class Foo {
        private String name = "FOO";

        public String name() {
            return this.name;
        }
    }

    @Test
    public void testFunctional() {
        Foo f = new Foo();

        callMethod(f, Foo::name);
    }

    public void callMethod(Foo f, Function<Foo, String> fn) {
        logger.info("call method: {}", fn.apply(f));

    }
}
