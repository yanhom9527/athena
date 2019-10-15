package com.athena.codec;

import com.athena.config.AthenaConfig;
import java.nio.charset.Charset;

/**
 * Encode a string to a byte array.
 *
 * @author mukong
 */
public class StringEncoder implements Encoder<String> {

    @Override
    public boolean canEncode(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);
    }



    @Override
    public byte[] encode(String string, Charset charset) {
        return string.getBytes(charset);
    }

    @Override
    public byte[] encode(String s) {
        return encode(s, Charset.forName(AthenaConfig.charset()));
    }
}
