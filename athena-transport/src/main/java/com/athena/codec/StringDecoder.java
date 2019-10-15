package com.athena.codec;

import com.athena.config.AthenaConfig;
import java.nio.charset.Charset;

/**
 * Decodes from a byte array to string.
 *
 * @author mukong
 */
public class StringDecoder implements Decoder<String> {

    @Override
    public boolean canDecode(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);
    }

    @Override
    public String decode(byte[] bytes) throws Exception {
        return decode(bytes, Charset.forName(AthenaConfig.charset()));
    }

    @Override
    public String decode(byte[] bytes, Charset charset) {
        if (bytes == null || bytes.length <= 0) {
            throw new IllegalArgumentException("Bad byte array");
        }
        return new String(bytes, charset);
    }
}
