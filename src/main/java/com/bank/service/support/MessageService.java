package com.bank.service.support;

import com.bank.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

import static java.util.Locale.of;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageSource messageSource;
    private final Locale ptBr = of("pt", "BR");

    public String get(final Message mensagem, final Object... args) {
        if (isNull(mensagem)) {
            return EMPTY;
        }

        return messageSource.getMessage(mensagem.getMessage(), args, ptBr);
    }
}
