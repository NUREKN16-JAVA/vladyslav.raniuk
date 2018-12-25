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
import java.util.Objects;

public class BrowseServlet extends HttpServlet {

    private static final String BROWSE_JSP = "/browse.jsp";
    private static final String EDIT_JSP = "/edit.jsp";

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Objects.nonNull(req.getParameter("addButton"))) {
            add(req, resp);
        } else if (Objects.nonNull(req.getParameter("editButton"))) {
            edit(req, resp);
        } else if (Objects.nonNull(req.getParameter("deleteButton"))) {
            delete(req, resp);
        } else if (Objects.nonNull(req.getParameter("detailsButton"))) {
            details(req, resp);
        } else {
            browse(req, resp);
        }
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

    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
    }

    private void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String id = req.getParameter("id");
        if (Objects.isNull(id) || id.length() == 0) {
            req.setAttribute("error", "You must choose at least one line to edit!");
            req.getRequestDispatcher(BROWSE_JSP).forward(req, resp);
            return;
        }
        try {
            User user = DaoFactory.getInstance().getUserDao().findUser(new Long(id));
            req.getSession().setAttribute("user", user);
        } catch (DatabaseException e) {
            req.setAttribute("error", "ERROR: " + e.getMessage());
            req.getRequestDispatcher("/browse.jsp").forward(req, resp);
            return;
        }
        resp.sendRedirect("edit");
    }

    private void details(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
    }
}
