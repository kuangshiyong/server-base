package cn.bjzfgcjs.idefense;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import tk.mybatis.spring.annotation.MapperScan;

import java.io.IOException;
import java.util.List;


//@EnableAsync
@EnableScheduling
@EnableRetry
@EnableCaching
@MapperScan("cn.bjzfgcjs.idefense.dao.mapper")
@SpringBootApplication(scanBasePackages = "cn.bjzfgcjs.idefense", exclude = {JacksonAutoConfiguration.class})
public class IdefenseBackendApplication extends WebMvcConfigurationSupport {

	private static final Logger logger = LoggerFactory.getLogger(IdefenseBackendApplication.class);

	@Value("${taskExecutor.timeout:300}")
	private int threadTimeout;

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
		Gson gson = new GsonBuilder()
				.disableHtmlEscaping()
				.create();
		gsonHttpMessageConverter.setGson(gson);

		converters.add(gsonHttpMessageConverter);
	}

	@Bean(destroyMethod = "shutdown")
	public ThreadPoolTaskExecutor defenseScheduler() {
		ThreadPoolTaskExecutor scheduler = new ThreadPoolTaskExecutor();
		scheduler.setCorePoolSize(10);
		scheduler.afterPropertiesSet();

		return scheduler;
	}
//
//	@Bean(destroyMethod="shutdown")
//	RedissonClient redisson(@Value("redisson.yaml") String configFile) throws IOException {
//		Config config = Config.fromYAML(new ClassPathResource(configFile).getInputStream());
//		return Redisson.create(config);
//	}
//
//	@Bean
//	CacheManager cacheManager(RedissonClient redissonClient) throws IOException {
//		return new RedissonSpringCacheManager(redissonClient, "cache-config.json");
//	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	public static void main(String[] args) {
		SpringApplication.run(IdefenseBackendApplication.class, args);
	}
}

