package med.voll.web_application.domain.usuario;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import med.voll.web_application.domain.RegraDeNegocioException;
import med.voll.web_application.domain.usuario.email.EmailService;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encriptador;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("O usuário não foi encontrado!"));
    }

    public Long salvarUsuario(String nome, String email, Perfil perfil) {

        String primeiraSenha = UUID.randomUUID().toString().substring(0, 8);
        System.out.println("Senha gerada: " + primeiraSenha);
        String senhaCriptografada = encriptador.encode(primeiraSenha);
        Usuario usuario = usuarioRepository.save(new Usuario(nome, email, senhaCriptografada, perfil));
        return usuario.getId();
    }

    public void excluir(Long id) {
        usuarioRepository.deleteById(id);
    }

    public void alterarSenha(DadosAlteracaoSenha dados, Usuario usuarioLogado) {
        if (!encriptador.matches(dados.senhaAtual(), usuarioLogado.getPassword()))
            throw new RegraDeNegocioException("Senha digitada não confere com a senha atual!");

        verificaSenha(dados.novaSenha(), dados.novaSenhaConfirmacao());

        usuarioLogado.alterarSenha(criptografaSenha(dados.novaSenha()));
        usuarioLogado.setSenhaAlterada(true);
        usuarioRepository.save(usuarioLogado);
    }

    public void enviarToken(String email) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado!"));
        String token = UUID.randomUUID().toString();
        usuario.setToken(token);
        usuario.setExpiracaoToken(LocalDateTime.now().plusMinutes(15));
        usuarioRepository.save(usuario);
        emailService.enviarEmailSenha(usuario);
    }

    public void recuperarConta(String codigo, DadosRecuperacaoConta dados) {
        Usuario usuario = usuarioRepository.findByTokenIgnoreCase(codigo)
                .orElseThrow(() -> new RegraDeNegocioException("Link inválido!"));

        if (usuario.getExpiracaoToken().isBefore(LocalDateTime.now()))
            throw new RegraDeNegocioException("Link expirado!");

        verificaSenha(dados.novaSenha(), dados.novaSenhaConfirmacao());

        usuario.alterarSenha(criptografaSenha(dados.novaSenha()));

        usuario.setExpiracaoToken(null);
        usuario.setToken(null);
        
        usuarioRepository.save(usuario);
    }

    private void verificaSenha(String senha, String senhaConfirmacao) {
        if (!senha.equals(senhaConfirmacao))
            throw new RegraDeNegocioException("Senha de confirmação não confere!");
    }

    
    private String criptografaSenha(String novaSenha) {
        return encriptador.encode(novaSenha);
    }
}
