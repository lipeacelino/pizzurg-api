package com.pizzurg.api.exception.handler;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ApiError ( LocalDateTime timestamp, Integer code, String status, List<String> errors) {
}
