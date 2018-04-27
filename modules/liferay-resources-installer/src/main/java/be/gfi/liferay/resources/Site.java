package be.gfi.liferay.resources;

import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.StringPool;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Data
@Builder
public class Site {

    private static Logger _logger = LoggerFactory.getLogger(Site.class.getName());

    @Builder.Default
    private long userId = 0L;
    @Builder.Default
    private long parentGroupId = GroupConstants.DEFAULT_PARENT_GROUP_ID;
    @Builder.Default
    private long liveGroupId = GroupConstants.DEFAULT_LIVE_GROUP_ID;
    @Builder.Default
    private Map<Locale, String> nameMap = new HashMap<>();
    @Builder.Default
    private Map<Locale, String> descriptionMap = new HashMap<>();
    @Builder.Default
    private int type = 0;
    @Builder.Default
    private boolean manualMembership;
    @Builder.Default
    private int membershipRestriction = GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION;
    @Builder.Default
    private String friendlyURL = StringPool.BLANK;
    @Builder.Default
    private boolean site;
    @Builder.Default
    private boolean inheritContent = false;
    @Builder.Default
    private boolean active = true;

    /*
     * ServiceContext attributes
     */
    @Builder.Default
    private long[] assetCategoryIds = new long[]{};
    @Builder.Default
    private String[] assetTagNames = new String[]{};
    @Builder.Default
    private String uuid = StringPool.BLANK;

    public Try<com.liferay.portal.kernel.model.Group> addSite() {
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
