package ru.netology.sender;

import ru.netology.entity.Country;

public interface LocalizationService {

    String locale(Country country);
}
