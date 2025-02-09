package com.ucan.helpdesk.service;

import com.ucan.helpdesk.enums.Prioridade;
import com.ucan.helpdesk.enums.StatusTicket;
import com.ucan.helpdesk.enums.TipoUsuarioSuporte;
import com.ucan.helpdesk.model.*;
import com.ucan.helpdesk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private RegraPrioridadeRepository regraPrioridadeRepository;

    @Autowired
    private SLARepository slaRepository;

    @Autowired
    private UsuarioSuporteRepository usuarioSuporteRepository;

    @Autowired
    private SLAService slaService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public List<Ticket> listarTodos() {return ticketRepository.findAll();}

    public Optional<Ticket> buscarPorId(Long id) {return ticketRepository.findById(id);}

    public List<Ticket> buscarPorStatus(String status) {return ticketRepository.findByStatus(status);}

    public List<Ticket> buscarPorPrioridade(Prioridade prioridade) {return ticketRepository.findByPrioridade(prioridade);}

    public void criarTicket(Ticket ticket) {
        List<RegraPrioridade> regras = regraPrioridadeRepository.findByPalavraChaveContainingIgnoreCase(ticket.getDescricao());
        Categoria categoria = null;
        Prioridade prioridade = Prioridade.MEDIA;

        if (!regras.isEmpty()) {
            RegraPrioridade regraSelecionada = regras.get(0);
            categoria = categoriaRepository.findById(Long.parseLong(regraSelecionada.getCategoriaId()))
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
            prioridade = regraSelecionada.getPrioridade();
        } else {
            categoria = categoriaRepository.findByNome("Outros").orElseThrow(() -> new RuntimeException("Categoria 'Outros' não encontrada"));
        }

        ticket.setFkCategoria(categoria);
        ticket.setPrioridade(Prioridade.valueOf(prioridade.name()));
        ticket.setStatus(StatusTicket.ABERTO);
        ticket.setDataHoraCriacao(new Date());
        ticketRepository.save(ticket);
    }

    // Abrir um novo ticket
    public Ticket novoTicket(String descricao) {
        if (descricao == null || descricao.isEmpty()) {throw new IllegalArgumentException("Descrição do ticket é obrigatória");}

        // Passo 3: Obter colaborador logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long idlUsuarioLogado = colaboradorRepository.findByEmail(authentication.getName()).getPkUsuario();

        Colaborador solicitante = colaboradorRepository.findById(idlUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));

        Ticket ticket = new Ticket();
        ticket.setDescricao(descricao);
        ticket.setStatus(StatusTicket.ENVIADO);
        ticket.setFkColaborador(solicitante);
        ticket.setDataHoraCriacao(new Date());

        kafkaProducerService.enviarTicketCriado(ticket);
        return ticket;
    }

    // Triagem automática de tickets
    public void triagemAutomatica(Ticket ticket) {
        List<UsuarioSuporte> tecnicosDisponiveis = usuarioSuporteRepository.findTecnicosDisponiveis();

        for (UsuarioSuporte tecnico : tecnicosDisponiveis) {
            if (tecnico.getEspecialidades().contains(ticket.getFkCategoria().getNome())) {
                ticket.setFkTecnicoResponsavel(tecnico);
                ticket.setStatus(StatusTicket.EM_ANDAMENTO);

                // Registrar log no Kafka
                kafkaTemplate.send("ticket-atualizado", "Ticket atribuído ao técnico: " + tecnico.getNome());
                ticketRepository.save(ticket);
                break;
            }
        }
    }
    @Scheduled(fixedRate = 60000) // Executar a cada minuto
    public void triagemAutomaticaProgramada() {
        List<Ticket> ticketsAbertos = ticketRepository.findByStatus(StatusTicket.ABERTO.name());

        for (Ticket ticket : ticketsAbertos) {
            String especialidadeNecessaria = ticket.getFkCategoria().getNome();

            List<UsuarioSuporte> tecnicosDisponiveis = usuarioSuporteRepository.findTecnicosDisponiveis();
            for (UsuarioSuporte tecnico : tecnicosDisponiveis) {
                if (tecnico.getEspecialidades().contains(especialidadeNecessaria)) {
                    ticket.setStatus(StatusTicket.EM_ANDAMENTO);
                    ticket.setFkTecnicoResponsavel(tecnico);

                    ticketRepository.save(ticket);

                    kafkaTemplate.send("ticket-atualizado", "Ticket atribuído ao técnico: " + tecnico.getNome());
                    break;
                }
            }
        }
    }

    // Escalonamento de tickets
    public void escalonarTicket(Ticket ticket) {
        if (StatusTicket.ESCALONADO.equals(ticket.getStatus())) {
            throw new RuntimeException("Ticket já está escalonado");
        }

        Optional<UsuarioSuporte> supervisor = usuarioSuporteRepository.findByTipo(TipoUsuarioSuporte.SUPERVISOR).stream().findFirst();

        if (supervisor.isPresent()) {
            ticket.setFkTecnicoResponsavel(supervisor.get());
            ticket.setStatus(StatusTicket.ESCALONADO);

            // Registrar log no Kafka
            kafkaTemplate.send("ticket-atualizado", "Ticket escalonado para o supervisor: " + supervisor.get().getNome());

            ticketRepository.save(ticket);
        } else {
            throw new RuntimeException("Nenhum supervisor disponível");
        }
    }

    @Scheduled(fixedRate = 60000) // Executar a cada minuto
    public void escalonarTicketsAutomaticamente() {
        List<Ticket> ticketsEmAndamento = ticketRepository.findByStatus(StatusTicket.EM_ANDAMENTO.name());

        for (Ticket ticket : ticketsEmAndamento) {
            if (slaService.SLAViolado(ticket)) {
                List<UsuarioSuporte> supervisores = usuarioSuporteRepository.findByTipo(TipoUsuarioSuporte.SUPERVISOR);

                if (!supervisores.isEmpty()) {
                    UsuarioSuporte supervisor = supervisores.get(0); // Atribuir ao primeiro supervisor disponível
                    ticket.setStatus(StatusTicket.ESCALONADO);
                    ticket.setFkTecnicoResponsavel(supervisor);

                    ticketRepository.save(ticket);

                    kafkaTemplate.send("ticket-atualizado", "Ticket escalonado para supervisor: " + supervisor.getNome());
                }
            }
        }
    }

    @Scheduled(fixedRate = 60000) // Executar a cada minuto
    public void monitorarSLAs() {
        List<Ticket> ticketsAbertos = ticketRepository.findByStatus(StatusTicket.ABERTO.name());

        for (Ticket ticket : ticketsAbertos) {
            SLA sla = slaRepository.findByFkCategoria(ticket.getFkCategoria())
                    .orElseThrow(() -> new RuntimeException("SLA não encontrado"));

            Duration tempoDecorrido = Duration.between(ticket.getDataHoraCriacao().toInstant(), LocalDateTime.now());
            long minutosDecorridos = tempoDecorrido.toMinutes();

            if (minutosDecorridos > sla.getTempoMaximoResposta()) {
                kafkaTemplate.send("sla-violado", "SLA violado para o ticket: " + ticket.getDescricao());
            }
        }
    }

    // Fechamento de ticket
    public Ticket fecharTicket(Long ticketId, String feedback) {
        return ticketRepository.findById(ticketId)
                .map(ticket -> {
                    ticket.setStatus(StatusTicket.RESOLVIDO);
                    ticket.setFeedback(feedback);

                    // Registrar log no Kafka
                    kafkaTemplate.send("ticket-closed", "Ticket fechado: " + ticket.getDescricao());

                    return ticketRepository.save(ticket);
                })
                .orElseThrow(() -> new RuntimeException("Ticket não encontrado"));
    }

    public void excluir(Long id) {
        ticketRepository.deleteById(id);
    }
}