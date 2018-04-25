package be.gfi.liferay.management.webcontent;

import be.gfi.liferay.utils.WebContentUtil;
import com.google.common.collect.Lists;
import com.liferay.dynamic.data.mapping.model.DDMStructure;

import java.util.List;

class DeleteStructuresList {

    List<DDMStructure> getStructures() {
        return getStructuresToDelete();
    }

    private List<DDMStructure> getStructuresToDelete() {
        final List<DDMStructure> structures = Lists.newArrayList();

        structures.addAll(
                getStructuresByStructureKey()
        );

        return structures;
    }

    private List<DDMStructure> getStructuresByStructureKey() {
        final List<DDMStructure> structures = WebContentUtil.getStructureByKey("NEW STRUCTURE");

        structures.addAll(
                WebContentUtil.getStructureByKey("Old structure")
        );

        return structures;
    }
}
