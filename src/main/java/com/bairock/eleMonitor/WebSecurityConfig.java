package com.bairock.eleMonitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bairock.eleMonitor.service.MyCustomUserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//	@Autowired
//	private DataSource dataSource;
	
	@Autowired
	private MyCustomUserService myCustomUserService;
	
//	@Autowired
//	private MyFilterSecurityInterceptor myFilterSecurityInterceptor;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/map").hasRole("MAP")
				.antMatchers("/login", "/css/**", "/img/**", "/js/**", "/webjars/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin().loginPage("/login").defaultSuccessUrl("/loginSuccess", true).permitAll()
				.and().rememberMe().key("eleMonitorKey");
//				.and().csrf().disable();
//		http.authorizeRequests().antMatchers("/login", "/css/**", "/img/**", "/js/**", "/webjars/**").permitAll()
//		.antMatchers("/map").hasRole("MAP").anyRequest().authenticated().and().formLogin().loginPage("/login")
//		.defaultSuccessUrl("/loginSuccess", true).permitAll().and().rememberMe().key("eleMonitorKey").and().csrf().disable().logout().permitAll();
		
//		http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.jdbcAuthentication().dataSource(dataSource)
//				.usersByUsernameQuery("select username, password, true from User where username=?")
//				.authoritiesByUsernameQuery("select username, authority from user_authority where username=?")
//				.passwordEncoder(passwordEncoder());
		
		auth.userDetailsService(myCustomUserService); //user Details Service验证
	}
	
	//忽略静态资源的拦截
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/static/**");
    }

//	@Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//             User
//                .withUsername("admin")
//                .password("admin")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }

//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("admin")
//				.password(new BCryptPasswordEncoder().encode("admin")).roles("USER", "ADMIN");
//	}

}
