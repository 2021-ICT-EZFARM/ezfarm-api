package com.ezfarm.ezfarmback.common.exception.dto;

import lombok.Getter;

@Getter
public enum ErrorCode {

    /*
    기본적인 에러
    */
    INTERNAL_SERVER_ERROR(500, "S_001", "서버에 문제가 생겼습니다."),

    INVALID_INPUT_VALUE(400, "C_001", "적절하지 않은 요청 값입니다."),
    INVALID_TYPE_VALUE(400, "C_002", "요청 값의 타입이 잘못되었습니다."),
    METHOD_NOT_ALLOWED(405, "C_003", "적절하지 않은 HTTP 메소드입니다."),

    NON_EXISTENT_USER(404, "US_001", "존재하지 않는 사용자입니다."),

    INVALID_FARM_ID(404, "FA_001", "존재하지 않는 농장입니다."),
    INVALID_FARM_START_DATE(400, "FA_002", "잘못된 농장 생성일을 입력했습니다."),

    FAVORITE_DUPLICATED(400, "FA_001", "이미 즐겨찾기에 등록된 농장입니다."),

    ACCESS_DENIED(403, "AU_001", "콘텐츠에 접근 권한이 없습니다."),
    BAD_LOGIN(400, "AU_002", "잘못된 아이디 또는 패스워드입니다."),
    DUPLICATED_EMAIL(400, "AU_003", "이미 존재하는 이메일입니다.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
