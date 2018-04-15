package be.gfi.liferay.management.user;

import be.gfi.liferay.resources.User;

import java.util.Arrays;
import java.util.List;

class CreateUsersList {

    List<User> getUsers() {
        return getUsersToCreate();
    }

    private List<User> getUsersToCreate() {
        return Arrays.asList(
                User.builder()
                        .screenName("jdoe8")
                        .emailAddress("j8@doe.com")
                        .firstName("John")
                        .lastName("Doe")
                        .build(),

                User.builder()
                        .screenName("jdoe9")
                        .emailAddress("j9@doe.com")
                        .firstName("John")
                        .lastName("Doe")
                        .build()
        );
    }
}
