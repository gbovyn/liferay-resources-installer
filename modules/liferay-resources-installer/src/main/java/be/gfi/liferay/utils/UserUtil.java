package be.gfi.liferay.utils;

import be.gfi.liferay.exception.UserNotSafeToDeleteException;
import be.gfi.liferay.helpers.Result;
import com.google.common.collect.Lists;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import io.vavr.control.Try;

import java.util.List;

public class UserUtil {

    private static long DEFAULT_COMPANY_ID = PortalUtil.getDefaultCompanyId();

    public static boolean alreadyExists(final String screenName, final String emailAddress) {
        return UserUtil.getUserByScreenName(screenName) != null
                || UserUtil.getUserByEmailAddress(emailAddress) != null;
    }

    public static List<User> getAllUsers() {
        return UserLocalServiceUtil.getUsers(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
    }

    public static User getUser(final long userId) {
        return UserLocalServiceUtil.fetchUser(userId);
    }

    public static User getUserByScreenName(final String screenName) {
        return UserLocalServiceUtil.fetchUserByScreenName(DEFAULT_COMPANY_ID, screenName);
    }

    public static User getUserByEmailAddress(final String emailAddress) {
        return UserLocalServiceUtil.fetchUserByEmailAddress(DEFAULT_COMPANY_ID, emailAddress);
    }

    public static Try<User> safeDeleteUser(final User user) {
        return safeDeleteUser(user.getUserId());
    }

    public static Try<User> safeDeleteUser(final long userId) {
        if (!safeToDelete(userId)) {
            return Try.failure(new UserNotSafeToDeleteException()); // TODO
        }

        return Try.of(() ->
                UserLocalServiceUtil.deleteUser(userId)
        );
    }

    public static Result<User> deleteAllUsers() {
        List<User> deletedUsers = Lists.newArrayList();
        List<User> notDeletedUsers = Lists.newArrayList();

        for (User user : getAllUsers()) {
            Try<User> userDeleted = safeDeleteUser(user.getUserId());
            if (userDeleted.isSuccess()) {
                deletedUsers.add(
                        userDeleted.get()
                );
            } else {
                notDeletedUsers.add(
                        user
                );
            }
        }

        return Result.<User>builder()
                .success(deletedUsers)
                .errors(notDeletedUsers)
                .build();
    }

    public static boolean safeToDelete(final long userId) {
        return safeToDelete(getUser(userId));
    }

    public static boolean safeToDelete(final User user) {
        return !user.isDefaultUser() && !isUniversalAdministrator(user);
    }

    public static boolean isUniversalAdministrator(final User user) {
        return PortalUtil.isOmniadmin(user);
    }
}
