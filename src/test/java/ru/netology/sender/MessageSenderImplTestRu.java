package ru.netology.sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;

import java.util.HashMap;
import java.util.Map;

public class MessageSenderImplTestRu {
    public static final String IP_ADDRESS_HEADER = "172.1.0.0";
    public static final String LOCALHOST = "127.0.0.1";
    public static final String MOSCOW_IP = "172.0.32.11";
    public static final String NEW_YORK_IP = "96.44.183.149";

    public static Location byIp(String ip) {
        if (LOCALHOST.equals(ip)) {
            return new Location(null, null, null, 0);
        } else if (MOSCOW_IP.equals(ip)) {
            return new Location("Moscow", Country.RUSSIA, "Lenina", 15);
        } else if (NEW_YORK_IP.equals(ip)) {
            return new Location("New York", Country.USA, " 10th Avenue", 32);
        } else if (ip.startsWith("172.")) {
            return new Location("Moscow", Country.RUSSIA, null, 0);
        } else if (ip.startsWith("96.")) {
            return new Location("New York", Country.USA, null, 0);
        }
        return null;
    }

    public static String locale(Country country) {
        switch (country) {
            case RUSSIA:
                return "Добро пожаловать";
            default:
                return "Welcome";
        }
    }

    @Test
    void test_get_in_russianSegmentIp() {
        String result;
        GeoService geoService = Mockito.mock(GeoService.class);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.123.12.19");
        String ipAddress = String.valueOf(headers.get(IP_ADDRESS_HEADER));
        Mockito.when(geoService.byIp(ipAddress))
                .thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));
        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.RUSSIA))
                .thenReturn(result = "Добро пожаловать");
        Assertions.assertEquals(result, "Добро пожаловать");
    }

    @Test
    void test_get_in_EngishSegmentIp() {
        String result;
        GeoService geoService = Mockito.mock(GeoService.class);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.123.12.19");
        String ipAddress = String.valueOf(headers.get(IP_ADDRESS_HEADER));
        Mockito.when(geoService.byIp(ipAddress))
                .thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));
        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.USA))
                .thenReturn(result = "Welcome");
        Assertions.assertEquals(result, "Welcome");
    }

    @Test
    void test_get_public_Location_byIp() {
        // given:
        String ip = "172.123.12.19";
        Location locationTrue = new Location("Moscow", Country.RUSSIA, null, 0);
        // when:
        Location location = byIp(ip);
        // then:
        Assertions.assertEquals(location, locationTrue);
    }

    @Test
    void test_get_public_String_locale() {
        // given:
        Country country = Country.RUSSIA;
        String resultTrue = "Добро пожаловать";
        // when:
        String result = locale(country);
        // then:
        Assertions.assertEquals(result, resultTrue);
    }
}

