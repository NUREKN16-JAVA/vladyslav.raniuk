package ua.nure.vraniuk.usermanagement.db;

import ua.nure.vraniuk.usermanagement.User;

import java.util.Collection;

public interface UserDao {
    public User createUser(User userToInsert) throws DatabaseException;
    public boolean deleteUser(User userToDelete) throws DatabaseException;
    public User findUser(Long id) throws DatabaseException;
    public Collection findAll() throws DatabaseException;
    public boolean updateUser(User userToUpdate) throws DatabaseException;
    public void setConnectionFactory(ConnectionFactory connectionFactory);
}
