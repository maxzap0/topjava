package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            if (!user.getRoles().isEmpty()) {
                batchUpdateRole(user);
            }
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) != 0) {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
            batchUpdateRole(user);
            return user;
        }
        return null;
    }

    private void batchUpdateRole(User user){
        jdbcTemplate.batchUpdate("INSERT INTO user_roles(user_id, role) VALUES (?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, user.getId());
                ps.setString(2, user.getRoles().stream().skip(i).findFirst().get().toString());
            }
            @Override
            public int getBatchSize() {
                return user.getRoles().size();
            }
        });
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        List<Role> roles = jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id=?", new RoleRowMapper(), id);
        User user = DataAccessUtils.singleResult(users);
        assert user != null;
        user.setRoles(roles);
        return user;
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        assert user != null;
        List<Role> roles = jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id=?", new RoleRowMapper(), user.getId());
        user.setRoles(roles);
        return user;
    }

    @Override
    public List<User> getAll() {
        return collectAllUsers(jdbcTemplate.query("SELECT * FROM users left join user_roles ur on users.id = ur.user_id " +
                "ORDER BY name, email", new UserRowMapper()));
    }

    private List<User> collectAllUsers(List<User> users) {
        Map<Integer, User> usersMap = new HashMap<>();
        users.forEach(user -> {
            if (usersMap.containsKey(user.id())) {
                usersMap.get(user.getId()).getRoles().add(user.getRoles().stream().findFirst().orElse(null));
            } else {
                usersMap.put(user.getId(), user);
            }
        });
        return new ArrayList<>(usersMap.values());
    }
}
