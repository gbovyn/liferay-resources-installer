package be.gfi.liferay;

import be.gfi.liferay.management.site.SiteManagement;
import be.gfi.liferay.management.user.UserManagement;
import be.gfi.liferay.management.webcontent.WebContentManagement;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true)
public class LiferayResourceInstaller {

    private static Logger _logger;

    private final UserManagement userManagement;
    private final SiteManagement siteManagement;
    private final WebContentManagement webContentManagement;

    public LiferayResourceInstaller() {
        _logger = LoggerFactory.getLogger(getClass().getName());

        _logger.info("Started");

        userManagement = new UserManagement();
        siteManagement = new SiteManagement();
        webContentManagement = new WebContentManagement();

        setup();
    }

    private void setup() {
        _logger.info("Setup in progress...");

        clearDbCache();

        //setupUserManagement();
        //setupSiteManagement();
        setupWebContentManagement();

        _logger.info("Setup done");
    }

    private void setupUserManagement() {
        userManagement.deleteUsers(UserManagement.DELETE_ALL);
        userManagement.createUsers();
    }

    private void setupSiteManagement() {
        siteManagement.deleteSites();
        siteManagement.createSites();
    }

    private void setupWebContentManagement() {
        webContentManagement.deleteStructures();
        webContentManagement.createStructures();
    }

    private void clearDbCache() {
        CacheRegistryUtil.clear();

        _logger.info("All DB caches cleared");
    }
}