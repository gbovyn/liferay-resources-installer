package be.gfi.liferay.resources;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.StringPool;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

@Data
@Builder
public class Site {

    private static Logger _logger = LoggerFactory.getLogger(Site.class.getName());

    @Default
    private long userId = 0L;
    @Default
    private long parentGroupId = GroupConstants.DEFAULT_PARENT_GROUP_ID;
    @Default
    private long liveGroupId = GroupConstants.DEFAULT_LIVE_GROUP_ID;
    @Default
    private Map<Locale, String> nameMap = Collections.emptyMap();
    @Default
    private Map<Locale, String> descriptionMap = Collections.emptyMap();
    @Default
    private int type = 0;
    @Default
    private boolean manualMembership;
    @Default
    private int membershipRestriction = GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION;
    @Default
    private String friendlyURL = StringPool.BLANK;
    @Default
    private boolean site;
    @Default
    private boolean inheritContent = false;
    @Default
    private boolean active = true;

    /*
     * ServiceContext attributes
     */
    @Default
    private long[] assetCategoryIds = new long[]{};
    @Default
    private String[] assetTagNames = new String[]{};
    @Default
    private String uuid = StringPool.BLANK;

    public Try<Group> addSite() {
        return Try.of(() ->
                GroupLocalServiceUtil.addGroup(
                        userId,
                        parentGroupId,
                        "TODO",
                        0L,
                        liveGroupId,
                        nameMap,
                        descriptionMap,
                        type,
                        manualMembership,
                        membershipRestriction,
                        friendlyURL,
                        site,
                        inheritContent,
                        active,
                        getServiceContext()
                )
        );
    }

    private ServiceContext getServiceContext() {
        final ServiceContext serviceContext = new ServiceContext();

        serviceContext.setAssetCategoryIds(assetCategoryIds);
        serviceContext.setAssetTagNames(assetTagNames);
        serviceContext.setUuid(uuid);

        return serviceContext;
    }
}
