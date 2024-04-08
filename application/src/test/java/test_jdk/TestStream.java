package test_jdk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tistory.ospace.common.core.Pair;
import com.tistory.ospace.common.util.CmmUtils;

class Foo {
    private int idx;
    private Pair<String, Integer> val;

    public static Foo of(int idx, String val) {
        Foo ret = new Foo();
        ret.setIdx(idx);
        ret.setVal(Pair.of(val, idx << 1));
        return ret;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public Pair<String, Integer> getVal() {
        return val;
    }

    public void setVal(Pair<String, Integer> val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return CmmUtils.toJsonString(this);
    }
}

public class TestStream {
    private static final Logger logger = LoggerFactory.getLogger(TestStream.class);

    @Test
    public void testGroupingBy() {
        List<Foo> foos = new ArrayList<>();

        foos.add(Foo.of(1, "one"));
        foos.add(Foo.of(2, "two"));
        foos.add(Foo.of(1, "two"));


        List<Pair<Pair<String, Integer>, Foo>> fooPairs = foos.stream().map(f -> Pair.of(f.getVal(), f)).collect(Collectors.toList());

        Map<String, List<Foo>> res = null;
        res = fooPairs.stream()
                .collect(Collectors.groupingBy(it -> it.getFirst().getFirst(), Collectors.mapping(it -> it.getSecond(), Collectors.toList())));

        logger.info("result: {}", res);

    }

    List<String> makeStrings() {
        return Arrays.asList("a", "b", "d", "e", "f");
    }

}

