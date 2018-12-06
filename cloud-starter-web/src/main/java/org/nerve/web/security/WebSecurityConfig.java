package org.nerve.web.security;

import org.apache.commons.lang3.StringUtils;
import org.nerve.web.security.captcha.CaptchaFilter;
import org.nerve.web.security.handler.*;
import org.nerve.web.security.rememberme.MongoPersistentTokenRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * com.zeus.web.security
 * Created by zengxm on 2017/8/23.
 */
@ConditionalOnWebApplication
@ConditionalOnProperty(name ="zeus.security",matchIfMissing = true,havingValue = "true")
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AuthConfig authConfig;
	@Autowired
	private UserDetailsService userDetailS;
	@Autowired
	private MongoPersistentTokenRepo mongoPersistentTokenRepo;


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.debug("[AUTH] ------------ START SETUP ------------");
		http.csrf().disable();
		http.headers().contentTypeOptions().disable().frameOptions().disable();
		logger.debug("ENABLE = {}", authConfig.enable);
		if(!authConfig.enable){
			http.anonymous();
			logger.debug("！没开启安全策略，所有请求均不拦截！");
			printEnd();
			return;
		}
		http.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers(authConfig.statics.split(AuthConfig.SPLIT)).permitAll();

		logger.debug("[AUTH] set permitAll for {}", authConfig.statics);

		if(StringUtils.isNotBlank(authConfig.popular)){
			http.authorizeRequests()
					.antMatchers(authConfig.popular.split(AuthConfig.SPLIT)).permitAll();
			logger.debug("[AUTH] set permitAll for popular: {}", authConfig.popular);
		}

		if(authConfig.map != null){
			authConfig.map.forEach((k,v)->{
				try {
					http.authorizeRequests()
							.antMatchers(k).hasAnyAuthority(StringUtils.split(v, AuthConfig.SPLIT));
					logger.debug("[AUTH] register ：{} for anyAuthority: {} ",k,v);
				} catch (Exception e) {
					logger.error("[AUTH] error on register anyAuthority:{}={}",k,v);
				}
			});
		}

		http.authorizeRequests().anyRequest().authenticated();

		logger.debug("[AUTH] remember me : {}", authConfig.rememberMe);
		//开启remember-me
		if(authConfig.rememberMe){
			logger.debug("[AUTH] open RememberMe...");
			http.rememberMe()
					.tokenValiditySeconds(authConfig.expire)
					.tokenRepository(mongoPersistentTokenRepo);
		}

		http
				.logout()
				.logoutSuccessHandler(new ExitSuccessHandler())
				.permitAll();
		
		http.exceptionHandling()
				.accessDeniedHandler(accessDeniedHandler())
				.authenticationEntryPoint(new LoginRequiredEntryPoint());

		/*
		登录设置
		 */
		http.formLogin()
				.loginPage(authConfig.loginPage).permitAll()
				.failureHandler(new LoginFailHandler())
				.successHandler(new LoginSuccessHandler())
		;

		http.formLogin()
				.loginProcessingUrl(authConfig.loginPostPage)
				.loginPage(authConfig.loginPage)
				.failureHandler(new LoginFailHandler())
				.successHandler(new LoginSuccessHandler())
		;
		logger.debug("[AUTH] loginPage={}, loginProcessingUrl={}", authConfig.loginPage, authConfig.loginPostPage);

		if(authConfig.captcha.isEnable()){
			logger.debug("[AUTH] config verification code....");

			CaptchaFilter captchaFilter = new CaptchaFilter(authConfig);
			http.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);
		}


		printEnd();
	}

	private void printEnd(){
		logger.debug("[AUTH] ------------ FINISH SETUP ------------");
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler(){
		return new JsonAccessDeniedHandler();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailS)
				.passwordEncoder(new BCryptPasswordEncoder());
	}
}
