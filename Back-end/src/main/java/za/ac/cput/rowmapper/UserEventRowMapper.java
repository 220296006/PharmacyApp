package za.ac.cput.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import za.ac.cput.model.Event;
import za.ac.cput.model.User;
import za.ac.cput.model.UserEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/05/22
 * @Time : 00:56
 **/
public class UserEventRowMapper implements RowMapper<UserEvent> {
    @Override
    public UserEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserEvent userEvent = new UserEvent();
        userEvent.setId(rs.getLong("id"));
        userEvent.setDevice(rs.getString("device"));
        userEvent.setIpAddress(rs.getString("ip_address"));
        userEvent.setCreatedAt(String.valueOf(rs.getTimestamp("created_at").toLocalDateTime()));

        User user = new User();
        user.setId(rs.getLong("user_id"));
        userEvent.setUser(user);

        Event event = new Event();
        event.setId(rs.getLong("event_id"));
        event.setType(rs.getString("type"));
        event.setDescription(rs.getString("description"));
        userEvent.setEvent(event);
        return userEvent;
    }
}