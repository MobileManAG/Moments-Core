/*******************************************************************************
 * Copyright 2015 MobileMan GmbH
 * www.mobileman.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.mobileman.moments.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mobileman.moments.core.security.UserSecurityService;

@Configuration
@Order(1)
@EnableWebSecurity(debug = false)
@EnableSpringDataWebSupport
public class MultiHttpSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
		http.csrf().disable()

		.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				.and().authorizeRequests().antMatchers("/api/noauth/**")
				.permitAll().and().authorizeRequests()
				.antMatchers("/api/auth/**").authenticated().and()
				.authorizeRequests().antMatchers("/manage/**").authenticated()
				.and().authorizeRequests().antMatchers("/**").permitAll()
				// .authenticated()
				.and().httpBasic().realmName("auth");

	}

	@Configuration
	@Order(2)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable()

			.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

					.and()

					.authorizeRequests().antMatchers("/api/auth/**")
					.authenticated()

					.and().httpBasic();
		}
	}

	@Configuration
	@Order(3)
	public static class FormLoginWebSecurityConfigurerAdapter extends
			WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().anyRequest().authenticated().and()
					.formLogin();
		}
	}

	@Configuration
	protected static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {

		@Autowired
		private UserSecurityService userDetailsService;

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		}

		@Bean
		public PasswordEncoder passwordEncoder() {
			return new MomentsPasswordEncoder(userDetailsService);
		}
	}

	static class MomentsPasswordEncoder implements PasswordEncoder {

		private PasswordEncoder encoder = new BCryptPasswordEncoder();

		private UserSecurityService userSecurityService;
		
		public MomentsPasswordEncoder(UserDetailsService userDetailsService) {
			this.userSecurityService = (UserSecurityService) userDetailsService;
		}
		
		@Override
		public String encode(CharSequence rawPassword) {
			return encoder.encode(rawPassword);
		}

		@Override
		public boolean matches(CharSequence rawPassword, String encodedPassword) {
			if (UserSecurityService.PASSWORD_VERIFIED_ID.equals(encodedPassword)) {
				return true;
			}
			
			String[] epass = UserSecurityService.splitPassword(encodedPassword);
			if(epass[0]==null){
				boolean check = rawPassword.equals(encodedPassword); 
				return check;
			}else{
				boolean check = encoder.matches(rawPassword, epass[2]);
				if(check){
					userSecurityService.tokenAuthentized(epass[0], epass[1]);
				}
				return check;
			}
		}

	}
}
