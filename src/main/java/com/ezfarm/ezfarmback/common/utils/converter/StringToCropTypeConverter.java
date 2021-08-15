package com.ezfarm.ezfarmback.common.utils.converter;

import com.ezfarm.ezfarmback.farm.domain.enums.CropType;
import org.springframework.core.convert.converter.Converter;

public class StringToCropTypeConverter implements Converter<String, CropType> {

    @Override
    public CropType convert(String source) {
        try {
            return CropType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
