package be.gfi.liferay.utils;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import io.vavr.control.Try;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class WebContentUtil {

    public static List<Locale> getExistingLocales(final Map<Locale, String> nameMap, final long groupId) {
        return LocaleUtil.getExistingLocales(nameMap, groupId);
    }

    public static List<Locale> getMissingLocales(final Map<Locale, String> nameMap, final long groupId) {
        return LocaleUtil.getMissingLocales(nameMap, groupId);
    }

    public static List<Locale> getInvalidLocales(final Map<Locale, String> nameMap, final long groupId) {
        return LocaleUtil.getInvalidLocales(nameMap, groupId);
    }

    public static List<DDMStructure> getAllStructures() {
        return DDMStructureLocalServiceUtil.getDDMStructures(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
    }

    public static List<DDMStructure> getStructureByKey(final String structureKey) {
        return getAllStructures().stream()
                .filter(
                        ddmStructure -> ddmStructure.getStructureKey().equals(structureKey.toUpperCase())
                )
                .collect(Collectors.toList());
    }

    public static Try<DDMStructure> delete(final DDMStructure structure) {
        return Try.of(() ->
                DDMStructureLocalServiceUtil.deleteDDMStructure(structure.getStructureId())
        );
    }
}
