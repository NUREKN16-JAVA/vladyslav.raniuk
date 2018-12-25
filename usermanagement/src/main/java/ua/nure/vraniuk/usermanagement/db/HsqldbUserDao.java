package ua.nure.vraniuk.usermanagement.db;

import ua.nure.vraniuk.usermanagement.User;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;

class HsqldbUserDao implements UserDao{
    private static final String CALL_IDENTITY = "call IDENTITY()";
    private static final String CREATE_USER_QUERY = "INSERT INTO users (firstname, lastname, dateofbirth) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String FIND_USER = "SELECT * FROM users WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM users WHERE id = ?";
    private static final String UPDATE_USER = "UPDATE users " +
            "SET id = ?,firstname = ?, lastname = ?, dateofbirth = ? " +
            "WHERE id = ?";

    private ConnectionFactory connectionFactory;

    private Connection myConnection;

    public HsqldbUserDao(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        try {
            setConnection();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public HsqldbUserDao() {
    }

    private void setConnection() throws DatabaseException{
        if(myConnection == null)
            this.myConnection = this.connectionFactory.getConnection(new String(), new String(), new String());
    }

    @Override
    public User create(User userToInsert) throws DatabaseException {
        int rowsInserted = 0;

        try {
            setConnection();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

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

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public boolean deleteUser(User userToDelete) throws DatabaseException {
        int rowsAffected = 0;
        try {
            setConnection();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        try(PreparedStatement my_statement = myConnection.prepareStatement(DELETE_USER)) {

            my_statement.setInt(1, Math.toIntExact(userToDelete.getId()));

            rowsAffected = my_statement.executeUpdate();

            if (rowsAffected != 1) {
                throw new DatabaseException("Number of the deleted rows: " + rowsAffected);
            }
            else return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException();
        }
        catch (DatabaseException e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public User findUser(Long id) throws DatabaseException {
        User userToReturn = null;

        try {
            setConnection();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        try(PreparedStatement get_user_statement = myConnection.prepareStatement(FIND_USER) ) {

            get_user_statement.setInt(1, Math.toIntExact(id));

            ResultSet usersSet = get_user_statement.executeQuery();
            usersSet.next();

            userToReturn = new User();

            userToReturn.setId(usersSet.getLong(1));
            userToReturn.setFirstName(usersSet.getString(2));
            userToReturn.setLastName(usersSet.getString(3));
            userToReturn.setDateOfBirth(usersSet.getDate(4));

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException();
        }


        return userToReturn;
    }

    @Override
    public Collection findAll() throws DatabaseException {
        Collection resultUsers = new LinkedList();

        //check if connection is not null
        try {
            setConnection();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

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
        int rowsAffected = 0;
        try {
            setConnection();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        try(PreparedStatement my_statement = myConnection.prepareStatement(UPDATE_USER)) {

            my_statement.setInt(1, Math.toIntExact(userToUpdate.getId()));
            my_statement.setString(2, userToUpdate.getFirstName() );
            my_statement.setString(3, userToUpdate.getLastName() );
            my_statement.setDate(4, new java.sql.Date(userToUpdate.getDateOfBirth().getTime()));
            my_statement.setInt(5, Math.toIntExact(userToUpdate.getId()));

            rowsAffected = my_statement.executeUpdate();

            if (rowsAffected != 1) {
                throw new DatabaseException("Number of the updated rows: " + rowsAffected);
            }
            else return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException();
        }
        catch (DatabaseException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
