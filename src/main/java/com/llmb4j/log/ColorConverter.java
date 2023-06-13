package com.llmb4j.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;
import cn.hutool.core.lang.ansi.AnsiColor;
import cn.hutool.core.lang.ansi.AnsiElement;
import cn.hutool.core.lang.ansi.AnsiStyle;
import cn.hutool.core.util.RandomUtil;
import com.llmb4j.util.LLmConstants;
import org.slf4j.MDC;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LiangTao
 * @date 2023年06月01 15:43
 **/
public class ColorConverter extends CompositeConverter<ILoggingEvent> {

    private static final Map<String, AnsiElement> ELEMENTS;

    static {
        Map<String, AnsiElement> ansiElements = new HashMap<>();
        ansiElements.put("faint", AnsiStyle.FAINT);
        ansiElements.put("bold", AnsiStyle.BOLD);
        ansiElements.put("italic", AnsiStyle.ITALIC);
        ansiElements.put("underline", AnsiStyle.UNDERLINE);
        ansiElements.put("red", AnsiColor.RED);
        ansiElements.put("green", AnsiColor.GREEN);
        ansiElements.put("yellow", AnsiColor.YELLOW);
        ansiElements.put("blue", AnsiColor.BLUE);
        ansiElements.put("magenta", AnsiColor.MAGENTA);
        ansiElements.put("cyan", AnsiColor.CYAN);
        ansiElements.put("white", AnsiColor.WHITE);
        ansiElements.put("bright-red", AnsiColor.BRIGHT_RED);
        ansiElements.put("bright-green", AnsiColor.BRIGHT_GREEN);
        ansiElements.put("bright-yellow", AnsiColor.BRIGHT_YELLOW);
        ansiElements.put("bright-blue", AnsiColor.BRIGHT_BLUE);
        ansiElements.put("bright-magenta", AnsiColor.BRIGHT_MAGENTA);
        ansiElements.put("bright-cyan", AnsiColor.BRIGHT_CYAN);
        ansiElements.put("bright-white", AnsiColor.BRIGHT_WHITE);

        ELEMENTS = Collections.unmodifiableMap(ansiElements);
    }

    private static final Map<Integer, AnsiElement> LEVELS;

    private  AtomicInteger previousRandomColorIndex = new AtomicInteger(1);

    static {
        Map<Integer, AnsiElement> ansiLevels = new HashMap<>();
        ansiLevels.put(Level.ERROR_INTEGER, AnsiColor.RED);
        ansiLevels.put(Level.WARN_INTEGER, AnsiColor.YELLOW);
        LEVELS = Collections.unmodifiableMap(ansiLevels);
    }

    @Override
    protected String transform(ILoggingEvent event, String in) {
        List<AnsiElement> ansiS = Optional.ofNullable(getOptionList()).orElse(Collections.emptyList())
                .stream()
                .filter(item -> ELEMENTS.containsKey(item) || "dynamic".equals(item))
                .map(item -> {
                    if (ELEMENTS.containsKey(item)) {
                        return ELEMENTS.get(item);
                    } else {
                        return Optional.ofNullable(MDC.get(LLmConstants.llmLogTypeKey)).map(LLmLogStyle::fromType).map(style -> style.contentStyles)
                                .orElse(getRandomColor());
                    }
                })
                .toList();
        if (ansiS.isEmpty()) {
            ansiS = List.of(Optional.ofNullable(LEVELS.get(event.getLevel().toInteger())).orElse(AnsiColor.GREEN));
        }
        Object[] elements = new Object[ansiS.size() + 1];
        //将ansiS填充到elements中,最后一个元素是in
        for (int i = 0; i < ansiS.size(); i++) {
            elements[i] = ansiS.get(i);
        }
        elements[ansiS.size()] = in;
        return toAnsiString(elements);
    }

    private AnsiElement getRandomColor() {
        AnsiColor[] colors = AnsiColor.values();
        int randomColorIndex;
        do {
            randomColorIndex = RandomUtil.randomInt(1, 16);
        } while (randomColorIndex == previousRandomColorIndex.get());
        previousRandomColorIndex.set(randomColorIndex);
        return colors[randomColorIndex];
    }

    protected String toAnsiString(Object... elements) {
        return AnsiOutput.toString(elements);
    }


}
