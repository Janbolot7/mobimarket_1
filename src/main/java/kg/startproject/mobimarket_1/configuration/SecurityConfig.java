package kg.startproject.mobimarket_1.configuration;

import kg.startproject.mobimarket_1.repository.UserRepository;
import kg.startproject.mobimarket_1.service.ServiceImplemintation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private UserServiceImpl userService;
    private JwtRequestFilter jwtRequestFilter;
    private AuthenticationProvider authenticationProvider;

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Autowired
    public void setJwtRequestFilter(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("HEAD", "POST", "GET", "PUT", "OPTIONS", "DELETE", "PATCH")
                        .allowedOrigins("*")
                        .allowedHeaders("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization")
                        .maxAge(3600);
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().and() //disable()
                .authorizeRequests()
                .antMatchers(
                        "/api/registration",
                        "/api/authentication",
                        "/api/checkAvailability",

                        "/user/updateUser",
                        "/user/findAll",
                        //"/user/deleteUser",
                        "/user/fullInfoOfUser2",
                        "/user/userProducts/{userId}",
                        "/user/favorite-products/{userId}",
                        "/user/{userId}/favorite-products/{productId}",
                        "/user/update-phone-number/{userId}",
                        "/user/send-verification-code",
                        "/user/verify-phone-number",

                        "/product/saveProduct",
                        "/product/updateProduct/{productId}",
                        "/product/deleteProduct/{id}",
                        "/product/findAllProducts",
                        "/product/findSingleProduct",
                        "/product/{productId}/like/increment",
                        "/product/{productId}/like/decrement",

                        "/swagger-ui/",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/swagger-resources",
                        "/swagger-resources/*",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/webjars/**")
                .permitAll()

                .antMatchers(HttpMethod.GET, "/product/**").hasAnyRole("USER")
                .antMatchers(HttpMethod.POST, "/product/**").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/product/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/product/**").hasRole("USER")

                .antMatchers(HttpMethod.PUT,"/user/update").hasAnyRole("USER")

                .anyRequest()
                .authenticated()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
