package com.omniesoft.commerce.imagestorage.omniecommerceimagestorage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

/**
 * @author Vitalii Martynovskyi
 * @since 18.09.17
 */

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	@Autowired
	@Qualifier("remoteTokenServices")
	private RemoteTokenServices remoteTokenServices;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

		resources.tokenServices(remoteTokenServices);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();
		http
				.authorizeRequests()
				.antMatchers("/statistic/a**").authenticated()
				.and()
				.authorizeRequests()
				.antMatchers(
						"/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
						"/swagger-ui.html", "/webjars/**", "/swagger-resources/configuration/ui", "/swagger-ui.html",
						"/swagger-resources/configuration/security",
						"/server/index**"
				).permitAll()
				.and()
				.authorizeRequests()
				.anyRequest().authenticated();
	}

	@Bean
	public RemoteTokenServices remoteTokenServices(
			final @Value("${security.oauth2.client.accessTokenUri}") String checkTokenUrl,
			final @Value("${security.oauth2.client.clientId}") String clientId,
			final @Value("${security.oauth2.client.clientSecret}") String clientSecret)
	{

		final RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
		remoteTokenServices.setCheckTokenEndpointUrl(checkTokenUrl);
		remoteTokenServices.setClientId(clientId);
		remoteTokenServices.setClientSecret(clientSecret);
		return remoteTokenServices;
	}

}
