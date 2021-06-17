package com.ezfarm.ezfarmback.common.advice;


import com.ezfarm.ezfarmback.common.CommonApiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("예외 처리 테스트")
public class GlobalExceptionHandlerTest extends CommonApiTest {

//    @DisplayName("적절하지 않은 요청 값이 왔을 때 예외 처리를 한다.")
//    @Test
//    void handleMethodArgumentNotValidException() throws Exception {
//        //given
//        TestDto testDto = new TestDto(null, 1);
//
//        //when, then
//        mockMvc.perform(get("/test")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(testDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.code").value("C_001"))
//                .andDo(print());
//    }

    @DisplayName("잘못된 타입의 요청 값이 왔을 때 예외 처리를 한다.")
    @Test
    void methodArgumentTypeMismatchException() throws Exception {
        mockMvc.perform(get("/test/path"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("C_002"))
                .andDo(print());
    }

    @DisplayName("적절하지 않은 HTTP 메소드 요청이 왔을 때 예외 처리를 한다.")
    @Test
    void handleHttpRequestMethodNotSupportedException() throws Exception {
        mockMvc.perform(delete("/test"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.code").value("C_003"))
                .andDo(print());
    }

    @DisplayName("커스텀 예외를 처리한다.")
    @Test
    void handleCustomException() throws Exception {
        mockMvc.perform(post("/test"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("C_004"))
                .andDo(print());
    }
}
