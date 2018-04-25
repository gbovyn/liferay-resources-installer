package be.gfi.liferay.management.site;

import be.gfi.liferay.utils.SiteUtil;
import com.google.common.collect.Lists;
import com.liferay.portal.kernel.model.Group;
import io.vavr.control.Try;

import java.util.Collections;
import java.util.List;

class DeleteSitesList {

    List<Group> getSites() {
        return getSitesToDelete();
    }

    private List<Group> getSitesToDelete() {
        List<com.liferay.portal.kernel.model.Group> sites = Lists.newArrayList();

        sites.addAll(getSitesByName());

        Try<Group> site = getSiteByFriendlyUrl();
        site.onSuccess(
                sites::add
        );

        return sites;
    }

    private List<Group> getSitesByName() {
        Try<Group> site = SiteUtil.getSiteByName("Test");
        if (site.isSuccess()) {
            return Collections.singletonList(site.get());
        }

        return Collections.emptyList();
    }

    private Try<Group> getSiteByFriendlyUrl() {
        return SiteUtil.getSiteByFriendlyUrl("/test-english");
    }
}
