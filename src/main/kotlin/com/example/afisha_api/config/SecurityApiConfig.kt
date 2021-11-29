package com.example.afisha_api.config

import com.example.afisha_api.security.AuthenticationFailureHandlerImpl
import com.example.afisha_api.security.RememberMeServiceImpl
import com.example.afisha_api.settings.SecuritySettings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.RememberMeServices

@Configuration
@EnableWebSecurity
class SecurityApiConfig(
        private val securitySettings: SecuritySettings
) : WebSecurityConfigurerAdapter() {
    @Autowired
    private val userDetailsService: UserDetailsService? = null

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/")
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .and()
                .rememberMe()
                .key("uniqueAndSecret")
                .userDetailsService(userDetailsService)
        http.csrf().disable()
    }

    @Bean
    fun authProvider(): AuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(userDetailsService)
        provider.setPasswordEncoder(passwordEncoder())
        return provider
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authFailureHandler(): AuthenticationFailureHandler {
        return AuthenticationFailureHandlerImpl()
    }

    @Bean
    fun rememberMeServices(): RememberMeServices {
        val services = RememberMeServiceImpl(
                securitySettings.key,
                userDetailsService(),
        )
        services.setAlwaysRemember(true)
        services.setTokenValiditySeconds(Int.MAX_VALUE)
        return services
    }
}