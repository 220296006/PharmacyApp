package za.ac.cput.service.implementation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import za.ac.cput.exception.ApiException;
import za.ac.cput.model.User;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.service.UserService;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class UserServiceImp implements UserService {
    private static final String COUNT_USER_EMAIL_QUERY ="";
    private static final String INSERT_USER_QUERY = "" ;
    private final UserRepository userRepository;
    private final NamedParameterJdbcTemplate jdbc;
    @Override
    public User save(User user) {
        log.info("Save A User");
        // Check if email is unique
        if(getEmailCount(user.getEmail().trim().toLowerCase()) > 0) throw new ApiException("Email already in use. Please use different email and try again");
        try{
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbc.update(INSERT_USER_QUERY, parameters, holder);
            user.setId(Objects.requireNonNull(holder.getKey()).longValue());
        } catch (EmptyResultDataAccessException exception){

        } catch (Exception exception){

        }


        return null;
    }
    @Override
    public Collection<User> list(int page, int pageSize) {
        return null;
    }

    @Override
    public User read(Long aLong) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public boolean delete(Long aLong) {
        return false;
    }

    private Integer getEmailCount(String email){
        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email", email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(User user) {
        return null;
    }










}
