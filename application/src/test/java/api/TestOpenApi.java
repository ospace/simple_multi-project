package api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.tistory.ospace.simpleproject.SimpleApplication;

import util.ApiMockMvc;
import util.ApiMockRes;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SimpleApplication.class)
@AutoConfigureMockMvc
@Import({ApiMockMvc.class})
public class TestOpenApi {
	private static final Logger logger = LoggerFactory.getLogger(TestOpenApi.class);
	
	@Test
	public void test1() {
		ApiMockMvc mvc = ApiMockMvc.create();
		
		String foo = "foo";
		ApiMockRes result = mvc.performSafe("/api/foo", foo);
		
		Integer status = result.getInteger("status");
		
		assertThat(status, is(equalTo(0)));
	}
}
