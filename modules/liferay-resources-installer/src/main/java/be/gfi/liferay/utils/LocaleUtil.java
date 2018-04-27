package be.gfi.liferay.utils;

import com.google.common.collect.Lists;
import com.liferay.portal.kernel.language.LanguageUtil;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LocaleUtil {

    private static final long NO_GROUP_ID = -1L;

//    public static Result<Locale> verifyNameMap(Map<Locale, String> nameMap) {
//        return verifyNameMap(nameMap, NO_GROUP_ID);
//    }
//
//    public static Result<Locale> verifyNameMap(Map<Locale, String> nameMap, long groupId) {
//        return Result.<Locale>builder()
//                .success(getExistingLocales(nameMap, groupId))
//                .errors(getNonExistingLocales(nameMap, groupId))
//                .build();
//    }

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

    private static Locale getLocaleFromLanguageId(String languageId) {
        final String language = languageId.split("_")[0];
        final String country = languageId.split("_")[1];
        return new Locale(language, country);
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

    public static ArrayList<Locale> getExistingLocales(final Map<Locale, String> nameMap) {
        return getExistingLocales(nameMap, NO_GROUP_ID);
    }

    public static ArrayList<Locale> getExistingLocales(final Map<Locale, String> nameMap, final long groupId) {
        return getFilteredLocales(nameMap, (Locale locale) -> isAvailableLocale(locale, groupId));
    }

    public static ArrayList<Locale> getNonExistingLocales(final Map<Locale, String> nameMap) {
        return getNonExistingLocales(nameMap, NO_GROUP_ID);
    }

    private static ArrayList<Locale> getNonExistingLocales(final Map<Locale, String> nameMap, final long groupId) {
        return getFilteredLocales(nameMap, (Locale locale) -> !isAvailableLocale(locale, groupId));
    }

    private static ArrayList<Locale> getFilteredLocales(final Map<Locale, String> nameMap, final Predicate<Locale> localesFilter) {
        return nameMap.entrySet().stream().map(Map.Entry::getKey)
                .filter(localesFilter)
                .collect(Collectors.toCollection(ArrayList::new));
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
