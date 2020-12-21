package com.jumblar.app.shell;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class StyledPromptProvider implements PromptProvider {

    public AttributedString getPrompt() {
        return new AttributedStringBuilder()
                .append("JUMBLAR:>",
                        AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE)).toAttributedString();
    }
}
