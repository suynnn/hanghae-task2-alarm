package org.hanghae.hanghaetask2alarm.common.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ErrorResponseDto {
    private final int status;
    private final String message;
    private final LocalDateTime time;
    private String stackTrace;
}
