package za.ac.cput.repository.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.Confirmation;
import za.ac.cput.query.ConfirmationQuery;
import za.ac.cput.repository.ConfirmationRepository;
import za.ac.cput.rowmapper.ConfirmationRowMapper;

import java.util.Collection;
import java.util.Objects;

import static za.ac.cput.query.ConfirmationQuery.INSERT_CONFIRMATION_QUERY;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/13
 * @Time : 20:48
 **/
@RequiredArgsConstructor
@Repository
@Slf4j
public class ConfirmationRepositoryImp implements ConfirmationRepository<Confirmation> {

    private final NamedParameterJdbcTemplate jdbc;
    private final JavaMailSender javaMailSender;
    @Override
    public Confirmation findByToken(String token) {
        return null;
    }
    @Override
    public Confirmation save(Confirmation confirmation) {
        log.info("Saving a Confirmation: {}", confirmation);
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(confirmation);
            jdbc.update(INSERT_CONFIRMATION_QUERY, parameters, holder);
            confirmation.setId(Objects.requireNonNull(holder.getKey()).longValue());
            sendConfirmationEmail(confirmation);
            return confirmation;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while saving the confirmation. Please try again.");
        }
    }

    @Override
    public Collection<Confirmation> list(String name, int page, int pageSize) {
        return null;
    }

    @Override
    public Confirmation read(Long id) {
        log.info("Fetching a Confirmation with ID: {}", id);
        try {
            SqlParameterSource parameters = new MapSqlParameterSource("id", id);
            return jdbc.queryForObject(ConfirmationQuery.SELECT_CONFIRMATION_BY_ID_QUERY, parameters, new ConfirmationRowMapper());
        } catch (EmptyResultDataAccessException exception) {
            return null;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while fetching the confirmation. Please try again.");
        }
    }
    @Override
    public Confirmation update(Confirmation confirmation) {
        log.info("Updating a Confirmation: {}", confirmation);
        try {
            SqlParameterSource parameters = getSqlParameterSource(confirmation);
            jdbc.update(ConfirmationQuery.UPDATE_CONFIRMATION_QUERY, parameters);
            return confirmation;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while updating the confirmation. Please try again.");
        }
    }
    @Override
    public void delete(Long id) {
        log.info("Deleting a Confirmation with ID: {}", id);
        try {
            SqlParameterSource parameters = new MapSqlParameterSource("id", id);
            jdbc.update(ConfirmationQuery.DELETE_CONFIRMATION_QUERY, parameters);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred while deleting the confirmation. Please try again.");
        }
    }

    private SqlParameterSource getSqlParameterSource(Confirmation confirmation) {
        return new MapSqlParameterSource()
                .addValue("id", confirmation.getId())
                .addValue("token", confirmation.getToken())
                .addValue("createdDate", confirmation.getCreatedDate())
                .addValue("userId", confirmation.getUser().getId());
    }

    @Async
    private void sendConfirmationEmail(Confirmation confirmation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(confirmation.getUser().getEmail());
        message.setSubject("Account Confirmation");
        message.setText("Dear " + confirmation.getUser().getFirstName() + ",\n\n"
                + "Thank you for registering an account with us. Please click on the link below to confirm your account:\n\n"
                + "Confirmation Link: http://example.com/confirm?token=" + confirmation.getToken() + "\n\n"
                + "If you did not register an account, please ignore this email.\n\n"
                + "Best regards,\n"
                + "Your App Team");
        javaMailSender.send(message);
        log.info("Confirmation email sent to: {}", confirmation.getUser().getEmail());
    }
}

