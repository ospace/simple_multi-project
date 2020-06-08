package com.tistory.ospace.simpleproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableAutoConfiguration/*(exclude = {
	WebMvcAutoConfiguration.class, DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class,
	HibernateJpaAutoConfiguration.class, JndiConnectionFactoryAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class,
	MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, MongoRepositoriesAutoConfiguration.class
})*/
/*
 * ComponentScan의 basePackages를 사용해서 빈객체를 등록하려고하지만, 제대로 Autowired가 동작하지 않음.
 * SimpleApplication을 configuration으로 이동하고 ComponentScan사용해서 다른 위치에 빈객체를 등록하려고하지만 되지 않고 있음.
 */
//@ComponentScan(basePackages = {"com.tistory.ospace.simpleproject"}, includeFilters = { @Filter(Controller.class), @Filter(ControllerAdvice.class) })
public class SimpleApplication extends SpringBootServletInitializer {
    private static Class<?> app = SimpleApplication.class;

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(app);
		springApplication.addListeners(new ApplicationPidFileWriter("app.pid"));
		springApplication.run(args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(app);
    }
}
