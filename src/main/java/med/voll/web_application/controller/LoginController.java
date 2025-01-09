package med.voll.web_application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import med.voll.web_application.domain.RegraDeNegocioException;
import med.voll.web_application.domain.usuario.DadosAlteracaoSenha;
import med.voll.web_application.domain.usuario.Usuario;
import med.voll.web_application.domain.usuario.UsuarioService;

@Controller
public class LoginController {

    public final static String FORMULARIO_ALTRACAO_SENHA = "autenticacao/formulario-alteracao-senha";

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/login")
    public String carregaPaginaLogin() {
        return "autenticacao/login";
    }

    @GetMapping("/alterar-senha")
    public String carregaPaginaAlteracao() {
        return FORMULARIO_ALTRACAO_SENHA;
    }

    @PostMapping("/alterar-senha")
    public String alaterarSenha(@Valid @ModelAttribute("dados") DadosAlteracaoSenha dados,
            @AuthenticationPrincipal Usuario usuarioLogado,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("dados", dados);
            return FORMULARIO_ALTRACAO_SENHA;
        }
        try {
            usuarioService.alterarSenha(dados, usuarioLogado);
            return "redirect:home";
        } catch (RegraDeNegocioException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("dados", dados);
            return FORMULARIO_ALTRACAO_SENHA;
        }
        
    }
}
