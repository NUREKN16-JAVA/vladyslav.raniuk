package ua.nure.vraniuk.usermanagement.web;

import ua.nure.vraniuk.usermanagement.User;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BrowseServletTest extends MockServletTestCase{

    private static final String EDIT_BUTTON = "editButton";
    private static final String DETAILS_BUTTON = "detailsButton";
    private static final String DELETE_BUTTON = "deleteButton";

    protected void setUp() throws Exception {
        super.setUp();
        createServlet(BrowseServlet.class);
    }

    public void testBrowse() {
        User user = new User(1000L, "John", "Doe", java.sql.Date.valueOf(LocalDate.now(ZoneId.systemDefault())));
        List list = Collections.singletonList(user);
        getMockUserDao().expectAndReturn("findAll", list);
        doGet();
        Collection collection = (Collection) getWebMockObjectFactory().getMockSession().getAttribute("users");
        assertNotNull("Could not find list of users in session", collection);
        assertEquals(list, collection);
    }

    public void testEdit() {
        User expectedUser = new User(1000L, "John", "Doe", java.sql.Date.valueOf(LocalDate.now(ZoneId.systemDefault())));
        getMockUserDao().expectAndReturn("findUser", new Long(1000), expectedUser);
        addRequestParameter(EDIT_BUTTON, "Edit");
        addRequestParameter("id", "1000");
        doPost();
        User actualUser = (User) getWebMockObjectFactory().getMockSession().getAttribute("user");
        assertNotNull("Could not find user in session", actualUser);
        assertEquals(expectedUser, actualUser);
    }

}
