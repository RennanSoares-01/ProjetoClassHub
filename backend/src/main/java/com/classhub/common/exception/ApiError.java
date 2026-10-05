package com.classhub.common.exception;

import java.time.Instant;

/** Formato padronizado de erro retornado pela API (ver .ai/standards.md). */
public record ApiError(Instant timestamp, int status, String error, String message, String path) {
}
