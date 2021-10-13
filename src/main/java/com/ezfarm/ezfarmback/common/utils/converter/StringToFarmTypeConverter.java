package com.ezfarm.ezfarmback.common.utils.converter;

import com.ezfarm.ezfarmback.farm.domain.enums.FarmType;
import org.springframework.core.convert.converter.Converter;

public class StringToFarmTypeConverter implements Converter<String, FarmType> {

    @Override
    public FarmType convert(String source) throws IllegalArgumentException {
        try {
            return FarmType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
