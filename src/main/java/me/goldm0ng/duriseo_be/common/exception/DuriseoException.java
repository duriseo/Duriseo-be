package me.goldm0ng.duriseo_be.common.exception;

import me.goldm0ng.duriseo_be.enums.message.FailMessage;
import lombok.Getter;

@Getter
public class DuriseoException extends RuntimeException {

    private final FailMessage failMessage;

    public DuriseoException(final FailMessage failMessage) {
        super (failMessage.getMessage());
        this.failMessage = failMessage;
    }
}
