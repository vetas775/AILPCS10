package com.ailpcs.core;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/** 
 * 
 * 创建人：MichaelTsui
 * 创建时间：2017-07-21
 * @version 1.0
 * 描述:
 * 自定义Jackson 的Date 格式化处理类, 用于Jackson: json序列化时Date字段格式
 *  
 */
@Component
public class MyCustomJacksonDateSerialize extends JsonSerializer<Date>{
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
    @Override
    public void serialize(Date bd, JsonGenerator gen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        String formattedBigDecimal = dateFormat.format(bd);
        gen.writeString(formattedBigDecimal);
    }
}
