package com.controletcc.service;

import com.controletcc.error.BusinessException;
import com.controletcc.model.entity.Professor;
import com.controletcc.model.entity.ProjetoTcc;
import com.controletcc.util.ErrorUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendSolicitacaoMembroBanca(@NonNull Professor professor, @NonNull ProjetoTcc projetoTcc) throws BusinessException {
        var sendTo = professor.getEmail();
        var subject = "Solicitação de participação a banca";
        String htmlTemplate = """
                <h3>Solicitação de participação a banca</h3>
                <p>Prezado(a) professor(a),</p>
                <p>Informamos que você foi convidado a participar de uma banca de TCC (Trabalho de Conclusão de curso).</p>
                <p>Informações sobre o TCC:</p>
                <ul>
                    <li>Tema: %s</li>
                    <li>Orientador(a): %s</li>
                    <li>Aluno(s): %s</li>
                </ul>
                <p>Desta forma, é necessário que você confirme sua participação. Para isso, basta entrar no sistema de Controle de TCCs e confirmar a solicitação.</p>
                <p style="color: rgba(0, 0, 0, 0.466);"><em>Este e-mail foi enviado pelo sistema, favor não responder.</em></p>
                """.formatted(projetoTcc.getTema(), projetoTcc.getProfessorOrientador().getNome(), projetoTcc.getAlunosNome());

        sendMail(sendTo, subject, htmlTemplate);
    }

    public void sendSolicitacaoMembroBancaRemovida(@NonNull Professor professor, @NonNull ProjetoTcc projetoTcc) throws BusinessException {
        var sendTo = professor.getEmail();
        var subject = "Solicitação de participação a banca - REMOVIDA";
        String htmlTemplate = """
                <h3>Solicitação de participação a banca - REMOVIDA</h3>
                <p>Prezado(a) professor(a),</p>
                <p>Informamos que a solicitação para participar da banca de TCC (Trabalho de Conclusão de curso) foi removida.</p>
                <ul>
                    <li>Tema: %s</li>
                    <li>Orientador(a): %s</li>
                    <li>Aluno(s): %s</li>
                </ul>
                <p style="color: rgba(0, 0, 0, 0.466);"><em>Este e-mail foi enviado pelo sistema, favor não responder.</em></p>
                """.formatted(projetoTcc.getTema(), projetoTcc.getProfessorOrientador().getNome(), projetoTcc.getAlunosNome());

        sendMail(sendTo, subject, htmlTemplate);
    }

    public void sendMail(@NonNull String sendTo, @NonNull String subject, @NonNull String htmlTemplate) throws BusinessException {
        try {
            MimeMessage mail = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mail);
            helper.setTo(sendTo);
            helper.setSubject(subject);
            helper.setText(htmlTemplate, true);
            mailSender.send(mail);
        } catch (MessagingException e) {
            ErrorUtil.error(log, e, "Erro ao enviar o e-mail.");
        }
    }

}
