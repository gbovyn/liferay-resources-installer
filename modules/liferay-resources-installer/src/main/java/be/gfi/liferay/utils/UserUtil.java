package be.gfi.liferay.utils;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;

public class UserUtil {

    private static long DEFAULT_COMPANY_ID = PortalUtil.getDefaultCompanyId();

    public static com.liferay.portal.kernel.model.User getUser(long userId) {
        return UserLocalServiceUtil.fetchUser(userId);
    }

    public static com.liferay.portal.kernel.model.User getUserByScreenName(String screenName) {
        return UserLocalServiceUtil.fetchUserByScreenName(DEFAULT_COMPANY_ID, screenName);
    }

    public static com.liferay.portal.kernel.model.User getUserByEmailAddress(String emailAddress) {
        return UserLocalServiceUtil.fetchUserByEmailAddress(DEFAULT_COMPANY_ID, emailAddress);
    }

    public static com.liferay.portal.kernel.model.User deleteUser(long userId) {
        try {
            return UserLocalServiceUtil.deleteUser(userId);
        } catch (PortalException e) {
            return null;
        }
    }
}
