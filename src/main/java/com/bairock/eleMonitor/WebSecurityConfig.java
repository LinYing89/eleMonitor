package com.bairock.eleMonitor;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/login", "/css/**", "/img/**", "/js/**", "/webjars/**").permitAll()
				.antMatchers("/map").hasRole("ADMIN").anyRequest().authenticated().and().formLogin().loginPage("/login")
				.defaultSuccessUrl("/loginSuccess", true).permitAll().and().logout().permitAll();

	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select username, password, true from User where username=?")
				.authoritiesByUsernameQuery("select username, authority from user_authority where username=?")
				.passwordEncoder(passwordEncoder());
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
