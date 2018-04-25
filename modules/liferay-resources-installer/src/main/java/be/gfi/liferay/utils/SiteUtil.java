package be.gfi.liferay.utils;

import be.gfi.liferay.helpers.Result;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import io.vavr.control.Try;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

public class SiteUtil {

    public static long getDefaultCompanyId() {
        return PortalUtil.getDefaultCompanyId();
    }

    public static Try<Long> getDefaultUserId() {
        return Try.of(() ->
                UserLocalServiceUtil.getDefaultUserId(getDefaultCompanyId())
        );
    }

    public static Try<Group> deleteSite(Group site) {
        return Try.of(() ->
                GroupLocalServiceUtil.deleteGroup(site)
        );
    }

    public static List<Group> getAllSites() {
        return GroupLocalServiceUtil.getGroups(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
    }

    public static Try<Group> getSiteByFriendlyUrl(String friendlyUrl) {
        return getSiteByFilter(friendlyUrl, group -> group.getFriendlyURL().equals(friendlyUrl));
    }

    public static Try<Group> getSiteByName(String name) {
        return getSiteByFilter(name, group -> group.getNameMap().containsValue(name));
    }

    private static Try<Group> getSiteByFilter(String name, Predicate<Group> siteFilter) {
        return Try.of(
                getAllSites().stream()
                        .filter(siteFilter)
                        .findFirst()::get
        );
    }

    public static Result<Locale> verifyNameMap(Map<Locale, String> nameMap) {
        return Result.<Locale>builder()
                .success(LocaleUtil.getExistingLocales(nameMap))
                .errors(LocaleUtil.getNonExistingLocales(nameMap))
                .build();
    }

}
