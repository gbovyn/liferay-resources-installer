package be.gfi.liferay.utils;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.net.URL;

public class ResourcesUtil {

    private static final String FOLDER = "/data/";

    public static Try<String> getContent(final String filename) {
        final Option<URL> url = Option.of(
                Resources.getResource(FOLDER.concat(filename))
        );

        if (!url.isDefined()) {
            return Try.failure(
                    new IllegalArgumentException(
                            String.format("Resource '%s' could not be found", filename)
                    )
            );
        }

        return Try.of(() ->
                Resources.toString(url.get(), Charsets.UTF_8)
        );
    }
}
