package be.gfi.liferay.management.user;

import be.gfi.liferay.resources.User;
import be.gfi.liferay.utils.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserManagement {

    private static Logger _logger;

    public static final boolean DELETE_ALL = true;

    private CreateUsersList createUsersList;
    private DeleteUsersList deleteUsersList;

    public UserManagement() {
        _logger = LoggerFactory.getLogger(this.getClass().getName());

        createUsersList = new CreateUsersList();
        deleteUsersList = new DeleteUsersList();
    }

    public void createUsers() {
        _logger.info("Checking if users need to be created");

        List<User> usersToCreate = createUsersList.getUsers();

        _logger.info("{} users planned for creation", usersToCreate.size());

        for (User user : usersToCreate) {
            if (UserUtil.alreadyExists(user.getScreenName(), user.getEmailAddress())) {
                _logger.warn("User {} ({}) already exists. Creation aborted", user.getScreenName(), user.getEmailAddress());
                continue;
            }

            com.liferay.portal.kernel.model.User liferayUser = user.addUser();

            if (liferayUser != null) {
                _logger.info("User {} ({}) successfully created!", liferayUser.getScreenName(), liferayUser.getEmailAddress());
            }
        }
    }

    public void deleteUsers() {
        _logger.info("Checking if users need to be deleted");

        deleteUsers(false);
    }

    public void deleteUsers(boolean deleteAll) {
        List<com.liferay.portal.kernel.model.User> usersToDelete;

        if (deleteAll) {
            _logger.info("Deleting all users");
            usersToDelete = UserUtil.getAllUsers();
        } else {
            usersToDelete = deleteUsersList.getUsers();
        }

        _logger.info("{} users planned for deletion", usersToDelete.size());

        for (com.liferay.portal.kernel.model.User user : usersToDelete) {
            if (user == null) {
                _logger.warn("User is null. Deletion aborted");
                continue;
            }

            if (!UserUtil.alreadyExists(user.getScreenName(), user.getEmailAddress())) {
                _logger.warn("User {} ({}) does not exist. Deletion aborted", user.getScreenName(), user.getEmailAddress());
                continue;
            }

            com.liferay.portal.kernel.model.User liferayUser = UserUtil.deleteUser(user);

            if (liferayUser != null) {
                _logger.info("User {} ({}) successfully deleted!", liferayUser.getScreenName(), liferayUser.getEmailAddress());
            }
        }
    }
}
