/*
  AUTH | hwding
  DATE | Sep 05 2017
  DESC | text stamp remover for PDF files
  MAIL | m@amastigote.com
  GITH | github.com/hwding
 */
package com.amastigote.unstamper.core;

import com.sun.istack.internal.NotNull;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.IOException;
import java.util.Set;

class TextStampRecognizer {

    private static boolean recognizeWithFont(
            @NotNull String[] keywords,
            @NotNull byte[] inputText,
            @NotNull Set<PDFont> pdFonts) {
        String bs = generateByteString(inputText);
        for (PDFont f : pdFonts) {
            if (f == null) continue;
            for (String k : keywords) {
                try {
                    byte[] encodedKeywords = f.encode(k);
                    if (bs.contains(generateByteString(encodedKeywords)))
                        return true;
                } catch (IOException | IllegalArgumentException ignored) {
                }
            }
        }
        return false;
    }

    private static boolean recognizePlain(
            @NotNull String[] keywords,
            @NotNull byte[] inputText
    ) {
        for (String k : keywords) {
            if (new String(inputText).contains(k)) return true;
        }
        return false;
    }

    static boolean recognize(@NotNull String[] keywords,
                             @NotNull byte[] inputText,
                             @NotNull Set<PDFont> pdFonts) {
        return recognizePlain(keywords, inputText) ||
                recognizeWithFont(keywords, inputText, pdFonts);
    }

    private static String generateByteString(@NotNull byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(Byte.toString(b));
        }
        return stringBuilder.toString();
    }
}
