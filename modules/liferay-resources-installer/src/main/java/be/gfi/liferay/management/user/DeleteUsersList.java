package be.gfi.liferay.management.user;

import be.gfi.liferay.utils.UserUtil;
import com.google.common.collect.Lists;
import com.liferay.portal.kernel.model.User;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

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
                .collect(toList());
    }

    private List<User> getUsersByEmail() {
        final List<User> users = IntStream.range(5, 10)
                .mapToObj(i -> UserUtil.getUserByEmailAddress("j" + i + "@doe.com"))
                .collect(toList());

        return users;
    }

    private List<String> getUsersScreenName() {
        final List<String> screenNames = IntStream.range(0, 5)
                .mapToObj(i -> "jdoe" + i)
                .collect(toList());

        return screenNames;
    }
}
