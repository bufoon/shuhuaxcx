package com.rttx.protocol.rmq.support;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import java.io.IOException;
import java.nio.charset.Charset;

public class FastJsonMessageConverter extends AbstractJsonMessageConverter {

    @Override
    protected Message createMessage(Object object, MessageProperties messageProperties) {
        byte[] bytes;
        try {
            String jsonString = JSON.toJSONString(object);
            bytes = jsonString.getBytes(getDefaultCharset());
        }
        catch (IOException e) {
            throw new MessageConversionException(
                    "Failed to convert Message content", e);
        }
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        messageProperties.setContentEncoding(getDefaultCharset());
        int length = 0;
        if (bytes != null){
            length = bytes.length;
        }
        messageProperties.setContentLength(length);

        return new Message(bytes, messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        return null;
    }

    public <T> T fromMessage(Message message, T t) throws MessageConversionException {

        String json = new String(message.getBody(), Charset.defaultCharset());

        return (T) JSON.parseObject(json, t.getClass());
    }
}
