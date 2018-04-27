package be.gfi.liferay.resources;

import com.google.common.base.Strings;
import com.liferay.announcements.kernel.model.AnnouncementsDelivery;
import com.liferay.portal.kernel.model.Address;
import com.liferay.portal.kernel.model.EmailAddress;
import com.liferay.portal.kernel.model.Phone;
import com.liferay.portal.kernel.model.Website;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@Data
@Builder
public class User {

    @Builder.Default
    private long creatorUserId = 0L;
    @Builder.Default
    private long companyId = PortalUtil.getDefaultCompanyId();
    @Builder.Default
    private boolean autoPassword = false;
    private String password1;
    private String password2;
    @Builder.Default
    private boolean autoScreenName = false;
    @NonNull
    private String screenName;
    @NonNull
    private String emailAddress;
    private long facebookId;
    @Builder.Default
    private String openId = StringPool.BLANK;
    @Builder.Default
    private Locale locale = Locale.US;
    @Builder.Default
    private String firstName = StringPool.BLANK;
    @Builder.Default
    private String middleName = StringPool.BLANK;
    @Builder.Default
    private String lastName = StringPool.BLANK;
    @Builder.Default
    private int prefixId = 0;
    @Builder.Default
    private int suffixId = 0;
    @Builder.Default
    private boolean male = true;
    @Builder.Default
    private int birthdayMonth = Calendar.JANUARY;
    @Builder.Default
    private int birthdayDay = 1;
    @Builder.Default
    private int birthdayYear = 1970;
    @Builder.Default
    private String jobTitle = StringPool.BLANK;
    @Builder.Default
    private long[] groupIds = new long[]{};
    @Builder.Default
    private long[] organizationIds = new long[]{};
    @Builder.Default
    private long[] roleIds = new long[]{};
    @Builder.Default
    private long[] userGroupIds = new long[]{};
    @Singular
    private List<Address> addresses;
    @Singular
    private List<EmailAddress> emailAddresses;
    @Singular
    private List<Phone> phones;
    @Singular
    private List<Website> websites;
    @Singular
    private List<AnnouncementsDelivery> announcementsDelivers;
    @Builder.Default
    private boolean sendEmail = true;

    /*
     * ServiceContext attributes
     */
    @Builder.Default
    private long[] assetCategoryIds = new long[]{};
    @Builder.Default
    private String[] assetTagNames = new String[]{};
    @Builder.Default
    private String uuid = StringPool.BLANK;

    public Try<com.liferay.portal.kernel.model.User> addUser() {
        if (Strings.isNullOrEmpty(password1) && Strings.isNullOrEmpty(password2)) {
            autoPassword = true;
        }

        return Try.of(() ->
                UserLocalServiceUtil.addUser(
                        creatorUserId,
                        companyId,
                        autoPassword,
                        password1,
                        password2,
                        autoScreenName,
                        screenName,
                        emailAddress,
                        facebookId,
                        openId,
                        locale,
                        firstName,
                        middleName,
                        lastName,
                        prefixId,
                        suffixId,
                        male,
                        birthdayMonth,
                        birthdayDay,
                        birthdayYear,
                        jobTitle,
                        groupIds,
                        organizationIds,
                        roleIds,
                        userGroupIds,
                        sendEmail,
                        getServiceContext()
                )
        );
    }

    private ServiceContext getServiceContext() {
        final ServiceContext serviceContext = new ServiceContext();

        serviceContext.setAssetCategoryIds(assetCategoryIds);
        serviceContext.setAssetTagNames(assetTagNames);
        serviceContext.setUuid(uuid);

        return serviceContext;
    }
}
