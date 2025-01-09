package med.voll.web_application.domain.consulta;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import med.voll.web_application.domain.medico.Medico;
import med.voll.web_application.domain.paciente.Paciente;

@Entity
@Table(name = "consultas")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    private LocalDateTime data;
    @Deprecated
    public Consulta(){}

    public Consulta(Medico medico, Paciente paciente, DadosAgendamentoConsulta dados) {
        modificarDados(medico, paciente, dados);
    }

    public void modificarDados(Medico medico, Paciente paciente, DadosAgendamentoConsulta dados) {
        this.medico = medico;
        this.paciente = paciente;
        this.data = dados.data();
    }
    public Long getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public LocalDateTime getData() {
        return data;
    }

}