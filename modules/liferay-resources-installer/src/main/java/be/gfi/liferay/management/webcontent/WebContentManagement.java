package be.gfi.liferay.management.webcontent;

import be.gfi.liferay.resources.webcontent.Structure;
import be.gfi.liferay.utils.WebContentUtil;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;

public class WebContentManagement {

    private static Logger _logger;

    private final CreateStructuresList createStructuresList;
    private final DeleteStructuresList deleteStructuresList;

    public WebContentManagement() {
        _logger = LoggerFactory.getLogger(this.getClass().getName());

        createStructuresList = new CreateStructuresList();
        deleteStructuresList = new DeleteStructuresList();
    }

    public void createStructures() {
        _logger.info("Checking if structures need to be created");

        final List<Structure> structures = createStructuresList.getStructures();

        _logger.info("{} structures planned for creation", structures.size());

        for (Structure structure : structures) {
            logCorrectLocalesInNameMap(structure);
            logMissingLocalesInNameMap(structure);
            logInvalidLocalesInNameMap(structure);

            final Try<DDMStructure> createdStructure = structure.addStructure();
            if (createdStructure.isSuccess()) {
                _logger.info("Structure '{}' successfully created!", structure.getStructureKey());
            } else {
                createdStructure.onFailure(
                        ex -> _logger.warn("Structure couldn't be created", ex)
                );
            }
        }
    }

    public void deleteStructures() {
        _logger.info("Checking if structures need to be deleted");

        List<DDMStructure> structures = deleteStructuresList.getStructures();

        _logger.info("{} structures planned for deletion", structures.size());

        for (DDMStructure structure : structures) {
            final Try<DDMStructure> deletedStructure = WebContentUtil.delete(structure);
            if (deletedStructure.isSuccess()) {
                _logger.info("Structure {} ({}) successfully deleted!", deletedStructure.get().getStructureKey(), deletedStructure.get().getStructureId());
            } else {
                _logger.warn("Structure {} ({}) could not be deleted", structure.getStructureKey(), structure.getStructureId());
            }
        }
    }

    public void createTemplates() {

    }

    public void createWebContent() {

    }

    public void createApplicationDisplayTemplate() {

    }

    private void logCorrectLocalesInNameMap(Structure structure) {
        List<Locale> existingLocales = WebContentUtil.getExistingLocales(structure.getNameMap(), structure.getGroupId());
        existingLocales.forEach(
                locale -> _logger.info("The structure will be available in {}", locale)
        );
    }

    private void logMissingLocalesInNameMap(Structure structure) {
        List<Locale> missingLocales = WebContentUtil.getMissingLocales(structure.getNameMap(), structure.getGroupId());
        missingLocales.forEach(
                locale -> _logger.warn("The structure will not be available in {}", locale)
        );
    }

    private void logInvalidLocalesInNameMap(Structure structure) {
        List<Locale> invalidLocales = WebContentUtil.getInvalidLocales(structure.getNameMap(), structure.getGroupId());
        invalidLocales.forEach(
                locale -> _logger.error("Locale {} does not exist for this Site", locale)
        );
    }
}
