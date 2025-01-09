package med.voll.web_application.infra.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.web_application.domain.usuario.Usuario;

@Component
public class FiltroAlteracaoSenha extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (request.getRequestURI().contains(".css") || request.getRequestURI().contains(".png")) {
            chain.doFilter(request, response);
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !request.getRequestURI().equals("/alterar-senha")){
            Usuario usuario = (Usuario) auth.getPrincipal();
            if (!usuario.getSenhaAlterada()){
                response.sendRedirect("/alterar-senha");
                return;
            }
        }

        chain.doFilter(request, response);
    }

}
