package com.ezfarm.ezfarmback.common.utils.converter;

import com.ezfarm.ezfarmback.farm.domain.enums.FarmGroup;
import org.springframework.core.convert.converter.Converter;

public class StringToFarmGroupConverter implements Converter<String, FarmGroup> {

    @Override
    public FarmGroup convert(String source) throws IllegalArgumentException {
        try {
            return FarmGroup.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
