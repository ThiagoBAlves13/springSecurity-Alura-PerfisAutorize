package med.voll.web_application.domain.medico;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "medicos")
public class Medico {

    @Id
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String crm;
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    @Deprecated
    public Medico(){}

    public Medico(Long id, DadosCadastroMedico dados) {  
        this.id = id;   
        atualizarDados(dados);
    }

    public void atualizarDados(DadosCadastroMedico dados) {    
        
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.crm = dados.crm();
        this.especialidade = dados.especialidade();
    }

}
