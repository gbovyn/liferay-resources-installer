package be.gfi.liferay.resources.webcontent;


import be.gfi.liferay.utils.SiteUtil;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureConstants;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.dynamic.data.mapping.storage.StorageType;
import com.liferay.dynamic.data.mapping.util.DDMUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NonNull;

import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

@Data
@Builder
public class Structure {
    @Default
    private long userId = 0L;
    @Default
    private long groupId = SiteUtil.getDefaultGroupId();
    @Default
    private long parentStructureId = GroupConstants.DEFAULT_PARENT_GROUP_ID;
    @Default
    private long classNameId = PortalUtil.getPortal().getClassNameId(JournalArticle.class);
    @Default
    private String structureKey = StringPool.BLANK;
    @Default
    private Map<Locale, String> nameMap = Collections.emptyMap();
    @Default
    private Map<Locale, String> descriptionMap = Collections.emptyMap();
    @NonNull
    private String json;
    @Default
    private String storageType = StorageType.JSON.getValue();
    @Default
    private int type = DDMStructureConstants.TYPE_DEFAULT;

    /*
     * Service context attributes
     */
    private String uuid;
    private Date createDate;
    private Date modifiedDate;
    private String[] guestPermissions;
    private String[] groupPermissions;


    public Try<DDMStructure> addStructure() {
        final Try<DDMForm> ddmForm = getDDMForm();
        if (ddmForm.isFailure()) {
            return Try.failure(
                    ddmForm.getCause()
            );
        }

        final Try<DDMFormLayout> ddmFormLayout = getDDMFormLayout();
        if (ddmFormLayout.isFailure()) {
            return Try.failure(
                    ddmFormLayout.getCause()
            );
        }

        return Try.of(() ->
                DDMStructureLocalServiceUtil.addStructure(
                        userId,
                        groupId,
                        parentStructureId,
                        classNameId,
                        structureKey,
                        nameMap,
                        descriptionMap,
                        ddmForm.get(),
                        ddmFormLayout.get(),
                        storageType,
                        type,
                        getServiceContext()
                )
        );
    }

    private Try<DDMForm> getDDMForm() {
        return Try.of(() ->
                DDMUtil.getDDMForm(json)
        );
    }

    private Try<DDMFormLayout> getDDMFormLayout() {
        final Try<DDMForm> ddmForm = getDDMForm();
        if (ddmForm.isFailure()) {
            return Try.failure(
                    new IllegalArgumentException(
                            "Could not get valid DDM form", ddmForm.getCause()
                    )
            );
        }

        return Try.of(() ->
                DDMUtil.getDefaultDDMFormLayout(ddmForm.get())
        );
    }

    private ServiceContext getServiceContext() {
        final ServiceContext serviceContext = new ServiceContext();

        serviceContext.setUuid(uuid);
        serviceContext.setCreateDate(createDate);
        serviceContext.setModifiedDate(modifiedDate);
        serviceContext.setGuestPermissions(guestPermissions);
        serviceContext.setGroupPermissions(groupPermissions);

        return serviceContext;
    }
}
