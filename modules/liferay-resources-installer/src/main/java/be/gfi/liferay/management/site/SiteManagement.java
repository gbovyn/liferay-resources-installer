package be.gfi.liferay.management.site;

import be.gfi.liferay.helpers.Result;
import be.gfi.liferay.resources.Site;
import be.gfi.liferay.utils.SiteUtil;
import com.liferay.portal.kernel.model.Group;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;

public class SiteManagement {

    private static Logger _logger;

    private final CreateSitesList createSitesList;
    private final DeleteSitesList deleteSitesList;

    public SiteManagement() {
        _logger = LoggerFactory.getLogger(getClass().getName());

        createSitesList = new CreateSitesList();
        deleteSitesList = new DeleteSitesList();
    }

    public static void createBlankSite() {

    }

    public static void createCommunitySite() {

    }

    public static void createIntranetSite() {

    }

    public static void enableStaging(final long groupId) {

    }

    public static void disableStaging(final long groupId) {

    }

    public void createSites() {
        _logger.info("Checking if sites need to be created");

        final List<Site> sitesToCreate = createSitesList.getSites();

        _logger.info("{} sites planned for creation", sitesToCreate.size());

        for (final Site site : sitesToCreate) {
            logInvalidLocalesInNameMap(site);

            final Try<Group> createdGroup = site.addSite();
            if (createdGroup.isSuccess()) {
                _logger.info("Site '{}' ({}) successfully created!", createdGroup.get().getName(), createdGroup.get().getFriendlyURL());
            } else {
                createdGroup.onFailure(
                        ex -> _logger.warn("Site could not be created", ex)
                );
            }
        }
    }

    public void deleteSites() {
        final List<Group> sitesToDelete = deleteSitesList.getSites();
        _logger.info("{} sites planned for deletion", sitesToDelete.size());

        for (final Group site : sitesToDelete) {
            final Try<Group> liferaySite = SiteUtil.deleteSite(site);
            if (liferaySite.isSuccess()) {
                _logger.info("Site {} ({}) successfully deleted!", liferaySite.get().getName(), liferaySite.get().getFriendlyURL());
            } else {
                _logger.warn("Site {} ({}) could not be deleted", site.getName(), site.getFriendlyURL());
            }
        }
    }

    private void logInvalidLocalesInNameMap(final Site site) {
        final Result<Locale> result = SiteUtil.verifyNameMap(site.getNameMap());
        for (final Locale locale : result.getErrors()) {
            _logger.warn("Locale {} does not exist in Liferay", locale);
        }
    }
}
