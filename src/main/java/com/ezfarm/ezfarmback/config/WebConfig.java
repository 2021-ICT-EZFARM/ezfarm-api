package com.ezfarm.ezfarmback.config;

import com.ezfarm.ezfarmback.common.utils.converter.StringToCropTypeConverter;
import com.ezfarm.ezfarmback.common.utils.converter.StringToFarmGroupConverter;
import com.ezfarm.ezfarmback.common.utils.converter.StringToFarmTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToFarmTypeConverter());
        registry.addConverter(new StringToFarmGroupConverter());
        registry.addConverter(new StringToCropTypeConverter());
    }
}
