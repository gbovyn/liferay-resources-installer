package be.gfi.liferay.utils;

import com.google.common.collect.Lists;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class LocaleUtil {

    private static final long NO_GROUP_ID = -1L;

    /**
     * Returns the locales available in the group but not added to the name map.
     *
     * @param nameMap
     * @param groupId
     * @return
     */
    public static List<Locale> getMissingLocales(final Map<Locale, String> nameMap, final long groupId) {
        final Set<Locale> availableLocales = getAvailableLocales(SiteUtil.getAvailableLanguageIds(groupId));

        return availableLocales.stream()
                .filter(locale -> !nameMap.keySet().contains(locale))
                .collect(toList());
    }

    public static Set<Locale> getAvailableLocales(final Set<String> languageIds) {
        return languageIds.stream()
                .map(LocaleUtil::getLocaleFromLanguageId)
                .collect(toSet());
    }

    private static Locale getLocaleFromLanguageId(final String languageId) {
        return Locale.forLanguageTag(languageId.replace(StringPool.UNDERLINE, StringPool.DASH));
    }

    /**
     * Returns the locales added to the map but not available in the group.
     *
     * @param nameMap
     * @param groupId
     * @return
     */
    public static List<Locale> getInvalidLocales(final Map<Locale, String> nameMap, final long groupId) {
        return getNonExistingLocales(nameMap, groupId);
    }

    public static List<Locale> getExistingLocales(final Map<Locale, String> nameMap) {
        return getExistingLocales(nameMap, NO_GROUP_ID);
    }

    public static List<Locale> getExistingLocales(final Map<Locale, String> nameMap, final long groupId) {
        return getFilteredLocales(nameMap, locale -> isAvailableLocale(locale, groupId));
    }

    public static List<Locale> getNonExistingLocales(final Map<Locale, String> nameMap) {
        return getNonExistingLocales(nameMap, NO_GROUP_ID);
    }

    private static List<Locale> getNonExistingLocales(final Map<Locale, String> nameMap, final long groupId) {
        return getFilteredLocales(nameMap, locale -> !isAvailableLocale(locale, groupId));
    }

    private static List<Locale> getFilteredLocales(final Map<Locale, String> nameMap, final Predicate<Locale> localesFilter) {
        return nameMap.entrySet().stream()
                .map(Entry::getKey)
                .filter(localesFilter)
                .collect(toList());
    }

    static boolean isAvailableLocale(final Locale locale) {
        return isAvailableLocale(locale, NO_GROUP_ID);
    }

    private static boolean isAvailableLocale(final Locale locale, final long groupId) {
        final List<Locale> availableLocales = getAvailableLocales(groupId);

        return availableLocales.contains(locale);
    }

    private static List<Locale> getAvailableLocales(final long groupId) {
        return Lists.newArrayList(
                groupId != NO_GROUP_ID
                        ? LanguageUtil.getAvailableLocales()
                        : LanguageUtil.getAvailableLocales(groupId)
        );
    }
}
