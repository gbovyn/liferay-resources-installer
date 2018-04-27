package be.gfi.liferay.management.user;

import be.gfi.liferay.resources.User;
import be.gfi.liferay.utils.UserUtil;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserManagement {

    public static final boolean DELETE_ALL = true;
    private static Logger _logger;
    private final CreateUsersList createUsersList;
    private final DeleteUsersList deleteUsersList;

    public UserManagement() {
        _logger = LoggerFactory.getLogger(this.getClass().getName());

        createUsersList = new CreateUsersList();
        deleteUsersList = new DeleteUsersList();
    }

    public void createUsers() {
        _logger.info("Checking if users need to be created");

        final List<User> usersToCreate = createUsersList.getUsers();

        _logger.info("{} users planned for creation", usersToCreate.size());

        for (User user : usersToCreate) {
            if (UserUtil.alreadyExists(user.getScreenName(), user.getEmailAddress())) {
                _logger.warn("User {} ({}) already exists. Creation aborted", user.getScreenName(), user.getEmailAddress());
                continue;
            }

            final Try<com.liferay.portal.kernel.model.User> liferayUser = user.addUser();
            if (liferayUser.isSuccess()) {
                _logger.info("User {} ({}) successfully created!", liferayUser.get().getScreenName(), liferayUser.get().getEmailAddress());
            } else {
                _logger.warn("User {} could not be created", user.getScreenName());
            }
        }
    }

    public void deleteUsers() {
        _logger.info("Checking if users need to be deleted");
        deleteUsers(false);
    }

    public void deleteUsers(final boolean deleteAll) {
        final List<com.liferay.portal.kernel.model.User> usersToDelete;

        if (deleteAll) {
            _logger.info("Deleting all users");
            usersToDelete = UserUtil.getAllUsers();
        } else {
            usersToDelete = deleteUsersList.getUsers();
        }

        _logger.info("{} users planned for deletion", usersToDelete.size());

        for (com.liferay.portal.kernel.model.User user : usersToDelete) {
            if (!UserUtil.alreadyExists(user.getScreenName(), user.getEmailAddress())) {
                _logger.warn("User {} ({}) does not exist. Deletion aborted", user.getScreenName(), user.getEmailAddress());
                continue;
            }

            final Try<com.liferay.portal.kernel.model.User> liferayUser = UserUtil.safeDeleteUser(user);
            if (liferayUser.isSuccess()) {
                _logger.info("User {} ({}) successfully deleted!", liferayUser.get().getScreenName(), liferayUser.get().getEmailAddress());
            } else {
                _logger.warn("User {} ({}) could not be deleted", user.getScreenName(), user.getEmailAddress());
            }
        }
    }
}
