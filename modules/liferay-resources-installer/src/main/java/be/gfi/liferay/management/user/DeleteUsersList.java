package be.gfi.liferay.management.user;

import be.gfi.liferay.utils.UserUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class DeleteUsersList {

    List<com.liferay.portal.kernel.model.User> getUsers() {
        return getUsersToDelete();
    }

    private List<com.liferay.portal.kernel.model.User> getUsersToDelete() {
        List<com.liferay.portal.kernel.model.User> users = new ArrayList<>();

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
        List<com.liferay.portal.kernel.model.User> users = new ArrayList<>();

        for (int i = 5; i < 10; i++) {
            users.add(
                    UserUtil.getUserByEmailAddress("j" + i + "@doe.com")
            );
        }

        return users;
    }

    private List<String> getUsersScreenName() {
        List<String> screenNames = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            screenNames.add("jdoe" + i);
        }

        return screenNames;
    }
}
