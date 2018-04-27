package be.gfi.liferay.utils;

import be.gfi.liferay.helpers.Result;
import com.google.common.collect.Sets;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import io.vavr.control.Try;

import java.util.*;
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

    public static Try<Group> deleteSite(final Group site) {
        return Try.of(() ->
                GroupLocalServiceUtil.deleteGroup(site)
        );
    }

    public static List<Group> getAllSites() {
        return GroupLocalServiceUtil.getGroups(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
    }

    public static Set<String> getAvailableLanguageIds(final long groupId) {
        final Try<String[]> availableLanguageIds = Try.of(() ->
                GroupLocalServiceUtil.getGroup(groupId).getAvailableLanguageIds()
        );

        if (availableLanguageIds.isSuccess()) {
            return Sets.newHashSet(availableLanguageIds.get());
        }

        return Collections.emptySet();
    }

    public static Try<Group> getSite(final long groupId) {
        return Try.of(() ->
                GroupLocalServiceUtil.getGroup(groupId)
        );
    }

    public static Try<Group> getSiteByFriendlyUrl(final String friendlyUrl) {
        return getSiteByFilter(group -> group.getFriendlyURL().equals(friendlyUrl));
    }

    public static Try<Group> getSiteByName(final String name) {
        return getSiteByFilter(group -> group.getNameMap().containsValue(name));
    }

    private static Try<Group> getSiteByFilter(final Predicate<Group> siteFilter) {
        return Try.of(
                getAllSites().stream()
                        .filter(siteFilter)
                        .findFirst()::get
        );
    }

    public static Result<Locale> verifyNameMap(final Map<Locale, String> nameMap) {
        return Result.<Locale>builder()
                .success(LocaleUtil.getExistingLocales(nameMap))
                .errors(LocaleUtil.getNonExistingLocales(nameMap))
                .build();
    }

    public static long getDefaultGroupId() {
        final Try<Group> companyGroup = Try.of(() ->
                GroupLocalServiceUtil.getCompanyGroup(PortalUtil.getDefaultCompanyId())
        );

        return companyGroup.isSuccess() ? companyGroup.get().getGroupId() : GroupConstants.DEFAULT_LIVE_GROUP_ID;
    }
}
