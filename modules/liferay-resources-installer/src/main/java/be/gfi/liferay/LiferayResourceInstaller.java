package be.gfi.liferay;

import be.gfi.liferay.management.user.UserManagement;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true)
public class LiferayResourceInstaller {

    private static Logger _logger;

    private final UserManagement userManagement;

    public LiferayResourceInstaller() {
        _logger = LoggerFactory.getLogger(this.getClass().getName());

        _logger.info("Started");

        userManagement = new UserManagement();

        setup();
    }

    private void setup() {
        _logger.info("Setup in progress...");

        setupUserManagement();

        _logger.info("Setup done");
    }

    private void setupUserManagement() {
        userManagement.deleteUsers(UserManagement.DELETE_ALL);
        userManagement.createUsers();
    }
}