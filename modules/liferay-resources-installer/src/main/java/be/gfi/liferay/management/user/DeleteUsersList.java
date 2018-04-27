package be.gfi.liferay.management.user;

import be.gfi.liferay.utils.UserUtil;
import com.google.common.collect.Lists;
import com.liferay.portal.kernel.model.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class DeleteUsersList {

    List<User> getUsers() {
        return getUsersToDelete();
    }

    private List<User> getUsersToDelete() {
        final List<User> users = Lists.newArrayList();

        users.addAll(
                getUsersByScreenName()
        );

        users.addAll(
                getUsersByEmail()
        );

        return users;
    }

    private List<User> getUsersByScreenName() {
        return getUsersScreenName()
                .stream()
                .map(UserUtil::getUserByScreenName)
                .collect(Collectors.toList());
    }

    private List<User> getUsersByEmail() {
        final List<User> users = IntStream.range(5, 10)
                .mapToObj(i -> UserUtil.getUserByEmailAddress("j" + i + "@doe.com"))
                .collect(Collectors.toList());

        return users;
    }

    private List<String> getUsersScreenName() {
        final List<String> screenNames = IntStream.range(0, 5)
                .mapToObj(i -> "jdoe" + i)
                .collect(Collectors.toList());

        return screenNames;
    }
}
