package ua.nure.vraniuk.usermanagement.db;

import ua.nure.vraniuk.usermanagement.User;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;

public class HsqldbUserDao implements UserDao{
    private static final String CALL_IDENTITY = "call IDENTITY()";
    private static final String CREATE_USER_QUERY = "INSERT INTO users (firstname, lastname, dateofbirth) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_USERS = "SELECT id, firstname, lastname, dateofbirth FROM users";
    private ConnectionFactory connectionFactory;
    private Connection myConnection;

    public HsqldbUserDao(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        try {
            this.myConnection = this.connectionFactory.getConnection(new String(), new String(), new String());
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User createUser(User userToInsert) throws DatabaseException {
        int rowsInserted = 0;

        try(PreparedStatement my_statement = myConnection.prepareStatement(CREATE_USER_QUERY);
                CallableStatement my_callable_statement = myConnection.prepareCall(CALL_IDENTITY)) {

            my_statement.setString(1, userToInsert.getFirstName());
            my_statement.setString(2, userToInsert.getLastName());
            my_statement.setDate(3, new java.sql.Date(userToInsert.getDateOfBirth().getTime()));

            rowsInserted = my_statement.executeUpdate();

            if (rowsInserted != 1) {
                throw new DatabaseException("Number of the inserted rows: " + rowsInserted);
            }

            ResultSet keys = my_callable_statement.executeQuery();

            if (keys.next()) {
                userToInsert.setId(new Long(keys.getLong(1)));
            }
            else {
                throw new DatabaseException("There are no rows in a query result set!");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException();
        }
          catch (DatabaseException e) {
            e.printStackTrace();
            throw e;
        }
        return userToInsert;
    }

    @Override
    public boolean deleteUser(User userToDelete) throws DatabaseException {
        return false;
    }

    @Override
    public User findUser(Long id) throws DatabaseException {
        return null;
    }

    @Override
    public Collection findAll() throws DatabaseException {
        Collection resultUsers = new LinkedList();

        try(Statement get_users_statement = myConnection.createStatement() ) {

            ResultSet usersSet = get_users_statement.executeQuery(SELECT_ALL_USERS);

            while (usersSet.next()) {
                User buffUser = new User();

                buffUser.setId(usersSet.getLong(1));
                buffUser.setFirstName(usersSet.getString(2));
                buffUser.setLastName(usersSet.getString(3));
                buffUser.setDateOfBirth(usersSet.getDate(4));

                ((LinkedList) resultUsers).push(buffUser);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException();
        }
        return resultUsers;
    }

    @Override
    public boolean updateUser(User userToUpdate) throws DatabaseException {
        return false;
    }
}
