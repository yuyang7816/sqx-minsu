package com.sqx.app.base.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import jodd.util.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

public class EmptyStringToNullLongDeserializer extends JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.getCurrentToken().isNumeric()) {
            return parser.getLongValue();
        }
        String value = parser.getValueAsString();
        if (value == null || StringUtils.isBlank(value)) {
            return null;
        }
        return Long.valueOf(value);
    }
}
