package be.gfi.liferay.management.user;

import be.gfi.liferay.utils.UserUtil;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class DeleteUsersList {

    List<com.liferay.portal.kernel.model.User> getUsers() {
        return getUsersToDelete();
    }

    private List<com.liferay.portal.kernel.model.User> getUsersToDelete() {
        final List<com.liferay.portal.kernel.model.User> users = Lists.newArrayList();

        users.addAll(
                getUsersByScreenName()
        );

        users.addAll(
                getUsersByEmail()
        );

        return users;
    }

    private List<com.liferay.portal.kernel.model.User> getUsersByScreenName() {
        return getUsersScreenName()
                .stream()
                .map(UserUtil::getUserByScreenName)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<com.liferay.portal.kernel.model.User> getUsersByEmail() {
        final List<com.liferay.portal.kernel.model.User> users = Lists.newArrayList();

        for (int i = 5; i < 10; i++) {
            users.add(
                    UserUtil.getUserByEmailAddress("j" + i + "@doe.com")
            );
        }

        return users;
    }

    private List<String> getUsersScreenName() {
        final List<String> screenNames = Lists.newArrayList();

        for (int i = 0; i < 5; i++) {
            screenNames.add("jdoe" + i);
        }

        return screenNames;
    }
}
