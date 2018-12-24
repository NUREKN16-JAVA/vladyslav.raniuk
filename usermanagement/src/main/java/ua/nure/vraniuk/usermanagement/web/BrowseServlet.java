package ua.nure.vraniuk.usermanagement.web;

import ua.nure.vraniuk.usermanagement.User;
import ua.nure.vraniuk.usermanagement.db.DaoFactory;
import ua.nure.vraniuk.usermanagement.db.DatabaseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class BrowseServlet extends HttpServlet {

    private static final String BROWSE_JSP = "/browse.jsp";

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        browse(req, resp);
    }

    private void browse(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        try {
            Collection<User> users = DaoFactory.getInstance().getUserDao().findAll();
            req.getSession(true).setAttribute("users", users);
            req.getRequestDispatcher(BROWSE_JSP).forward(req, resp);
        } catch (DatabaseException e) {
            throw new ServletException(e);
        }
    }
}
