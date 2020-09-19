package ru.netology.sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import java.util.HashMap;
import java.util.Map;

public class MessageSenderImplTestRu {
    public static final String IP_ADDRESS_HEADER = "172.1.0.0";
    public static final String LOCALHOST = "127.0.0.1";
    public static final String MOSCOW_IP = "172.0.32.11";
    public static final String NEW_YORK_IP = "96.44.183.149";

    @Test
    void test_get_in_russianSegmentIp() {
        String result;
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.123.12.19");

        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp(Mockito.any()))
                .thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Mockito.any()))
                .thenReturn("Добро пожаловать");

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        result = messageSender.send(headers);

        Assertions.assertEquals(result, "Добро пожаловать");
    }

    @Test
    void test_get_in_EngishSegmentIp() {
        String result;
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "162.123.12.19");

        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp(Mockito.any()))
                .thenReturn(new Location("New York", Country.USA, null, 0));

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Mockito.any()))
                .thenReturn("Welcome");

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        result = messageSender.send(headers);

        Assertions.assertEquals(result, "Welcome");
    }

    @Test
    void test_get_public_Location_byIp() {
        // given:
        String ip = "172.123.12.19";
        GeoService geoService = new GeoServiceImpl();
        Location locationTrue = new Location("Moscow", Country.RUSSIA, null, 0);
        // when:
        Location location = geoService.byIp(ip);
        // then:
        Assertions.assertEquals(location.getCountry(), locationTrue.getCountry());
    }

    @Test
    void test_get_public_String_locale() {
        // given:
        Country country = Country.RUSSIA;
        LocalizationService localizationService = new LocalizationServiceImpl();
        String resultTrue = "Добро пожаловать";
        // when:
        String result = localizationService.locale(country);
        // then:
        Assertions.assertEquals(result, resultTrue);
    }
}

