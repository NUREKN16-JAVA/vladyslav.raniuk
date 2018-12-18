package ua.nure.vraniuk.usermanagement.db;

public class DaoFactoryImpl extends DaoFactory {

    @Override
    public UserDao getUserDao() {
        try {
            Class clazz = Class.forName(properties.getProperty(USER_DAO));
            UserDao userDao = (UserDao) clazz.newInstance();
            userDao.setConnectionFactory(getConnectionFactory());
            return userDao;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}