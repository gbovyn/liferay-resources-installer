package be.gfi.liferay.management.webcontent;

import be.gfi.liferay.resources.webcontent.Structure;
import be.gfi.liferay.utils.ResourcesUtil;
import be.gfi.liferay.utils.SiteUtil;
import com.google.common.collect.ImmutableMap;
import io.vavr.control.Try;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

class CreateStructuresList {

    private static final int DEFAULT_USER_ID = -1;

    List<Structure> getStructures() {
        return getStructuresToCreate();
    }

    private List<Structure> getStructuresToCreate() {
        return Arrays.asList(
                getTestStructure()
        );
    }

    private Structure getTestStructure() {
        final Try<String> exampleJson = ResourcesUtil.getContent("structures/example.json");

        final Map<Locale, String> nameMap = ImmutableMap.<Locale, String>builder()
                .put(new Locale("en", "US"), "New structure")
                .put(new Locale("nl", "BE"), "Nieuw structuur")
                .build();

        return Structure.builder()
                .userId(getTestUserId())
                .structureKey("New structure")
                .nameMap(nameMap)
                .json(exampleJson.get())
                .groupId(36531)
                .build();
    }

    private long getTestUserId() {
        final Try<Long> defaultUserId = SiteUtil.getDefaultUserId();
        if (defaultUserId.isSuccess()) {
            return defaultUserId.get();
        }

        return DEFAULT_USER_ID;
    }
}
