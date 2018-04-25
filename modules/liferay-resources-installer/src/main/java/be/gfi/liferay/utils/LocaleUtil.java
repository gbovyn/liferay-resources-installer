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
    public static List<Locale> getMissingLocales(Map<Locale, String> nameMap, long groupId) {
        Set<Locale> availableLocales = getAvailableLocales(groupId);

        availableLocales.removeIf(nameMap::containsKey);

        return Lists.newArrayList(availableLocales);
    }

    /**
     * Returns the locales added to the map but not available in the group.
     *
     * @param nameMap
     * @param groupId
     * @return
     */
    public static List<Locale> getInvalidLocales(Map<Locale, String> nameMap, long groupId) {
        return getNonExistingLocales(nameMap, groupId);
    }

    public static ArrayList<Locale> getExistingLocales(Map<Locale, String> nameMap) {
        return getExistingLocales(nameMap, NO_GROUP_ID);
    }

    public static ArrayList<Locale> getExistingLocales(Map<Locale, String> nameMap, long groupId) {
        return getFilteredLocales(nameMap, (Locale locale) -> isAvailableLocale(locale, groupId));
    }

    public static ArrayList<Locale> getNonExistingLocales(Map<Locale, String> nameMap) {
        return getNonExistingLocales(nameMap, NO_GROUP_ID);
    }

    private static ArrayList<Locale> getNonExistingLocales(Map<Locale, String> nameMap, long groupId) {
        return getFilteredLocales(nameMap, (Locale locale) -> !isAvailableLocale(locale, groupId));
    }

    private static ArrayList<Locale> getFilteredLocales(Map<Locale, String> nameMap, Predicate<Locale> localesFilter) {
        return nameMap.entrySet().stream().map(Map.Entry::getKey)
                .filter(localesFilter)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    static boolean isAvailableLocale(Locale locale) {
        return isAvailableLocale(locale, NO_GROUP_ID);
    }

    private static boolean isAvailableLocale(Locale locale, long groupId) {
        Set<Locale> availableLocales = getAvailableLocales(groupId);

        return availableLocales.contains(locale);
    }

    private static Set<Locale> getAvailableLocales(long groupId) {
        return groupId != NO_GROUP_ID
                ? LanguageUtil.getAvailableLocales()
                : LanguageUtil.getAvailableLocales(groupId);
    }
}
