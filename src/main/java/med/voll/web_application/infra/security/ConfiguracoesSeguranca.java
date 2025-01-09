package med.voll.web_application.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ConfiguracoesSeguranca {

        @Autowired
        FiltroAlteracaoSenha filtroAlteracaoSenha;

        @Bean
        public SecurityFilterChain filtrosSeguranca(HttpSecurity http) throws Exception {

                return http
                                .authorizeHttpRequests(req -> {
                                        req.requestMatchers("/css/**", "/js/**", "/assets/**", "/", "/index",
                                                        "/home", "/esqueci-minha-senha", "recuperar-conta")
                                                        .permitAll();
                                        // req.requestMatchers("/pacientes/**").hasRole("ATENTEDENTE");
                                        // req.requestMatchers(HttpMethod.GET, "/medicos").hasAnyRole("ATENDENTE",
                                        // "PACIENTE");
                                        // req.requestMatchers("/medicos/**").hasRole("ATENTEDENTE");
                                        // req.requestMatchers(HttpMethod.POST, "/consultas/**").hasAnyRole("ATENDENTE",
                                        // "PACIENTE");
                                        // req.requestMatchers(HttpMethod.PUT, "/consultas/**").hasAnyRole("ATENDENTE",
                                        // "PACIENTE");
                                        req.anyRequest().authenticated();
                                })
                                .addFilterBefore(filtroAlteracaoSenha, UsernamePasswordAuthenticationFilter.class)
                                .formLogin(form -> form.loginPage("/login")
                                                .defaultSuccessUrl("/")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll())
                                .rememberMe(rememberMe -> rememberMe.key("lembrarDeMim")
                                                .alwaysRemember(true))
                                .csrf(Customizer.withDefaults())
                                .build();
        }

        @Bean
        public PasswordEncoder codificadorSenha() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
                return (web) -> web.ignoring().requestMatchers("/h2-console/**");
        }
}
