package cn.bjzfgcjs.idefense;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.List;


//@EnableAsync
@EnableScheduling
@EnableRetry
@MapperScan("cn.bjzfgcjs.idefense.dao.mapper")
@SpringBootApplication(scanBasePackages = "cn.bjzfgcjs.idefense", exclude = {JacksonAutoConfiguration.class})
public class VoiceManagerApplication extends WebMvcConfigurationSupport {

	private static final Logger logger = LoggerFactory.getLogger(VoiceManagerApplication.class);

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

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	public static void main(String[] args) {
		SpringApplication.run(VoiceManagerApplication.class, args);
	}
}

