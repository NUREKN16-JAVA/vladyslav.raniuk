package ua.nure.vraniuk.usermanagement.web;

import ua.nure.vraniuk.usermanagement.User;
import ua.nure.vraniuk.usermanagement.db.DaoFactory;
import ua.nure.vraniuk.usermanagement.db.DatabaseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class EditServlet extends HttpServlet {

    public static final String OK_BUTTON = "okButton";
    public static final String CANCEL_BUTTON = "cancelButton";

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Objects.nonNull(req.getParameter(OK_BUTTON))) {
            doOk(req, resp);
        } else if (Objects.nonNull(req.getParameter(CANCEL_BUTTON))) {
            doCancel(req, resp);
        } else {
            showPage(req, resp);
        }
    }

    protected void showPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException{
        try {
            req.getRequestDispatcher(BrowseServlet.EDIT_JSP).forward(req, resp);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletException(e.getMessage());
        }
    }

    private void doOk(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException{
        try {
            User userToAlter = getUser(req);
            processUser(userToAlter);
        } catch (DatabaseException e) {
            e.printStackTrace();
            throw new ServletException(e);
        } catch (ValidationException e) {
            e.printStackTrace();
            req.setAttribute("error", e.getMessage());
            showPage(req, resp);
            throw new ServletException(e);
        }
        req.getRequestDispatcher(BrowseServlet.BROWSE_JSP).forward(req, resp);
    }

    private void doCancel(HttpServletRequest req, HttpServletResponse resp) throws ServletException{
        try {
            req.getRequestDispatcher(BrowseServlet.BROWSE_JSP).forward(req, resp);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletException(e.getMessage());
        }
    }

    protected void processUser(User userToProcess) throws DatabaseException {
        DaoFactory.getInstance().getUserDao().updateUser(userToProcess);
    }

    private User getUser(HttpServletRequest req) throws ValidationException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        User userToAlter = new User();

        Long id = null;
        if(Objects.isNull(id))
            throw new ValidationException("Invalid id.");
        id = Long.parseLong(req.getParameter("id"));

        String firstName = new String();
        if(Objects.isNull(firstName))
            throw new ValidationException("Invalid firstName.");
        firstName = req.getParameter("firstName");

        String lastName = new String();
        if(Objects.isNull(lastName))
            throw new ValidationException("Invalid lastName.");
        lastName = req.getParameter("lastName");

        Date date = null;
        try {
            date = formatter.parse(req.getParameter("date"));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ValidationException("Invalid date.");
        }
        if(Objects.isNull(date))
            throw new ValidationException("Invalid date.");

        userToAlter.setId(id);
        userToAlter.setFirstName(firstName);
        userToAlter.setLastName(lastName);
        userToAlter.setDateOfBirth(date);

        return userToAlter;
    }
}
