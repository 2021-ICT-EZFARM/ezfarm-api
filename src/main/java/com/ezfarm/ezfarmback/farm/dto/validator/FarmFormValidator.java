package com.ezfarm.ezfarmback.farm.dto.validator;

import com.ezfarm.ezfarmback.farm.dto.FarmForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class FarmFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(FarmForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        FarmForm farmForm = (FarmForm) target;
        if (farmForm.getStartDate().isBefore(farmForm.getUser().getCreatedDate())) {
            errors.rejectValue("startDate", "invalid.startDate",
                    new Object[]{farmForm.getStartDate()}, "농가 재배 시작 일자가 잘못됬습니다.");
        }
    }
}
