package be.gfi.liferay.management.site;

import be.gfi.liferay.resources.Site;
import be.gfi.liferay.utils.SiteUtil;
import io.vavr.control.Try;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreateSitesList {

    private static final int DEFAULT_USER_ID = -1;

    List<Site> getSites() {
        return getSitesToCreate();
    }

    private List<Site> getSitesToCreate() {
        return Arrays.asList(
                getTestSite()
        );
    }

    private Site getTestSite() {
        return Site.builder()
                .userId(getTestUserId())
                .nameMap(getTestNameMap())
                .build();
    }

    private long getTestUserId() {
        final Try<Long> defaultUserId = SiteUtil.getDefaultUserId();
        if (defaultUserId.isSuccess()) {
            return defaultUserId.get();
        }
        return DEFAULT_USER_ID;
    }

    private Map<Locale, String> getTestNameMap() {
        return Collections.unmodifiableMap(
                Stream.of(
                        new SimpleEntry<>(new Locale("nl", "BE"), "Test Nederlands"),
                        new SimpleEntry<>(new Locale("en", "US"), "Test English"),
                        new SimpleEntry<>(new Locale("fr", "FR"), "Test Français de France"),
                        new SimpleEntry<>(new Locale("fr", "BE"), "Test Français de Belgique"),
                        new SimpleEntry<>(new Locale("de", "BE"), "Test Allemand")
                ).collect(
                        Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)
                )
        );
    }
}
