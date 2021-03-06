package br.com.casadocodigo.loja.conf;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.google.common.cache.CacheBuilder;

import br.com.casadocodigo.loja.controllers.HomeController;
import br.com.casadocodigo.loja.daos.ProdutoDAO;
import br.com.casadocodigo.loja.infra.FileSaver;
import br.com.casadocodigo.loja.models.CarrinhoCompras;

@EnableWebMvc
@EnableCaching
@ComponentScan(basePackageClasses= {HomeController.class, ProdutoDAO.class, FileSaver.class, 
									CarrinhoCompras.class})
public class AppWebConfiguration extends WebMvcConfigurerAdapter{

	/**
	 * seta o principal internalViewResolver e define prefixo/sufixo dos jsp ...
	 * retornados pelos controllers.
	 */
	@Bean
	public InternalResourceViewResolver internalResourceViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		resolver.setExposedContextBeanNames("carrinhoCompras");
		
		return resolver;
	}
	
	/**
	 * Seta caminho das mensagens de validacao;
	 * message.properties
	 */
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = 
				new ReloadableResourceBundleMessageSource();
		
		messageSource.setBasename("/WEB-INF/messages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(1);
		
		return messageSource;
	}
	
	/**
	 * formata as datas dos objetos no controller;
	 */
	@Bean
	public FormattingConversionService mvcConversionService() {
		DefaultFormattingConversionService conversionService = 
				new DefaultFormattingConversionService();
		DateFormatterRegistrar registrar = new DateFormatterRegistrar();
		
		registrar.setFormatter(new DateFormatter("dd/MM/yyyy"));
		registrar.registerFormatters(conversionService);
		
		return conversionService;
	}
	
	/**
	 * necessario para upload de arquivo = multipart
	 */
	@Bean
	public MultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}
	
	/**
	 * necessario para consumo de api rest via post
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public CacheManager cacheManager() {
		CacheBuilder<Object, Object> builder = CacheBuilder
				.newBuilder()
				.maximumSize(100)
				.expireAfterAccess(5, TimeUnit.MINUTES);
		
		GuavaCacheManager manager = new GuavaCacheManager();
		manager.setCacheBuilder(builder);
		
		return manager;
	}
	
	/**
	 * resolve os formatos de requisicao
	 * voce precisa criar o seu viewResolver. 
	 * ex: jsonViewResolver
	 */
	@Bean
	public ViewResolver contentNegotiationViewResolver(ContentNegotiationManager manager) {
		List<ViewResolver> viewResolvers = new ArrayList<>();
		viewResolvers.add(internalResourceViewResolver());
		viewResolvers.add(new JsonViewResolver());
		
		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
		resolver.setViewResolvers(viewResolvers);
		resolver.setContentNegotiationManager(manager);
		
		return resolver;
	}
	
	
	/**
	 * armazena um cookie com o locale solicitado.
	 * 
	 */
	@Bean
	public LocaleResolver localeResolver() {
		return new CookieLocaleResolver();
	}
	
	//TODO: armazenar dados sensiveis em properties.
	@Bean
	public MailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		mailSender.setHost("smtp.gmail.com");
		mailSender.setUsername("teste@gmail.com");
		mailSender.setPassword("teste123");
		mailSender.setPort(587);
		
		Properties properties = new Properties();
		
		properties.put("mail.smtp.auth", true);
		properties.put("mail.smtp.starttls.enable", true);
		
		mailSender.setJavaMailProperties(properties);
		
		return mailSender;
	}
	
	/**
	 * servlet que vai capturar requisicoes default. 
	 * Mandando isso direto para o tomcat.
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	/**
	 * identifica mudanca no locate
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LocaleChangeInterceptor());
	}
	
	
}
