package be.gfi.liferay.utils;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WebContentUtil {

    public static List<Locale> getExistingLocales(Map<Locale, String> nameMap, long groupId) {
        return LocaleUtil.getExistingLocales(nameMap, groupId);
    }

    public static List<Locale> getMissingLocales(Map<Locale, String> nameMap, long groupId) {
        return LocaleUtil.getMissingLocales(nameMap, groupId);
    }

    public static List<Locale> getInvalidLocales(Map<Locale, String> nameMap, long groupId) {
        return LocaleUtil.getInvalidLocales(nameMap, groupId);
    }
}
