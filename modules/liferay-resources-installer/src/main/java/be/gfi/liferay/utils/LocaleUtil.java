package be.gfi.liferay.utils;

import com.google.common.collect.Lists;
import com.liferay.portal.kernel.language.LanguageUtil;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LocaleUtil {

    private static final long NO_GROUP_ID = -1L;

    private static final String UNDERSCORE = "_";
    private static final String DASH = "-";

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
                .collect(Collectors.toList());
    }

    public static Set<Locale> getAvailableLocales(final Set<String> languageIds) {
        return languageIds.stream()
                .map(LocaleUtil::getLocaleFromLanguageId)
                .collect(Collectors.toSet());
    }

    private static Locale getLocaleFromLanguageId(final String languageId) {
        return Locale.forLanguageTag(languageId.replace(UNDERSCORE, DASH));
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
                .collect(Collectors.toList());
    }

    static boolean isAvailableLocale(final Locale locale) {
        return isAvailableLocale(locale, NO_GROUP_ID);
    }

    private static boolean isAvailableLocale(final Locale locale, final long groupId) {
        final List<Locale> availableLocales = getAvailableLocales(groupId);

        return availableLocales.contains(locale);
    }

    private static ArrayList<Locale> getAvailableLocales(final long groupId) {
        return Lists.newArrayList(
                groupId != NO_GROUP_ID
                        ? LanguageUtil.getAvailableLocales()
                        : LanguageUtil.getAvailableLocales(groupId)
        );
    }
}
