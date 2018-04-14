package be.gfi.liferay;

import be.gfi.liferay.resources.User;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true)
public class LiferayResourceInstaller {
    private static Logger _logger;

    public LiferayResourceInstaller() {
        _logger = LoggerFactory.getLogger(this.getClass().getName());

        _logger.info("Started");

        createUsers();
    }

    private void createUsers() {

        _logger.info("Creating users");

        com.liferay.portal.kernel.model.User user = User.builder()
                .screenName("jdoe")
                .emailAddress("j@doe.com")
                .firstName("John")
                .lastName("Doe")
                .autoPassword(true)
                .sendEmail(true)
                .build()
                .addUser();

        if (user != null) {
            _logger.info("User {} ({}) successfully created!", user.getScreenName(), user.getEmailAddress());
        }
    }
}