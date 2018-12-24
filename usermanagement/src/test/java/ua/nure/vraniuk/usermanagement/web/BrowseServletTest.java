package ua.nure.vraniuk.usermanagement.web;

import ua.nure.vraniuk.usermanagement.User;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BrowseServletTest extends MockServletTestCase{

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
}
