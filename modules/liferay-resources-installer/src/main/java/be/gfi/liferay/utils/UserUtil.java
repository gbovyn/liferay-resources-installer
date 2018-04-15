package be.gfi.liferay.utils;

import com.google.common.collect.Lists;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.List;

public class UserUtil {

    private static long DEFAULT_COMPANY_ID = PortalUtil.getDefaultCompanyId();

    public static boolean alreadyExists(String screenName, String emailAddress) {
        return UserUtil.getUserByScreenName(screenName) != null
                || UserUtil.getUserByEmailAddress(emailAddress) != null;
    }

    public static boolean isDefaultUser(User user) {
        return user.isDefaultUser();
    }

    public static User getUser(long userId) {
        return UserLocalServiceUtil.fetchUser(userId);
    }

    public static User getUserByScreenName(String screenName) {
        return UserLocalServiceUtil.fetchUserByScreenName(DEFAULT_COMPANY_ID, screenName);
    }

    public static User getUserByEmailAddress(String emailAddress) {
        return UserLocalServiceUtil.fetchUserByEmailAddress(DEFAULT_COMPANY_ID, emailAddress);
    }

    public static List<User> getAllUsers() {
        return UserLocalServiceUtil.getUsers(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
    }

    public static User deleteUser(User user) {
        if (!safeToDelete(user)) {
            return null;
        }

        return deleteUser(user.getUserId());
    }

    public static User deleteUser(long userId) {
        try {
            return UserLocalServiceUtil.deleteUser(userId);
        } catch (PortalException e) {
            return null;
        }
    }

    public static List<User> deleteAllUsers() {
        List<User> deletedUsers = Lists.newArrayList();

        for (User user : getAllUsers()) {
            deletedUsers.add(
                    deleteUser(user.getUserId())
            );
        }

        return deletedUsers;
    }

    public static boolean safeToDelete(long userId) {
        return safeToDelete(getUser(userId));
    }

    public static boolean safeToDelete(User user) {
        return !user.isDefaultUser() && !isUniversalAdministrator(user);
    }

    public static boolean isUniversalAdministrator(User user) {
        return PortalUtil.isOmniadmin(user);
    }
}
