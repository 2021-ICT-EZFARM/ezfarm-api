package com.ezfarm.ezfarmback.common.advice;

import com.ezfarm.ezfarmback.common.exception.CustomException;
import com.ezfarm.ezfarmback.common.exception.dto.ErrorCode;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/test")
public class GlobalExceptionTestController {

    @GetMapping
    public void test1(@Valid @RequestBody TestDto testDto) {
    }

    @GetMapping("/{path}")
    public void test2(@PathVariable Long path) {
    }

    @PostMapping
    public void test3() {
        throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
    }
}
