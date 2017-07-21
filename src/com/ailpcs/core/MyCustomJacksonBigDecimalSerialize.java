package com.ailpcs.core;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

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
 * 自定义Jackson 的Bigdecimal 格式化处理类, 用于Jackson: json序列化时BigDecimal字段格式为小数点后保留两位
 *  
 */
@Component
public class MyCustomJacksonBigDecimalSerialize extends JsonSerializer<BigDecimal>{
    //private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
    private static final  DecimalFormat df = new DecimalFormat("#,###,###,##0.00");

    @Override
    public void serialize(BigDecimal bd, JsonGenerator gen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        String formattedBigDecimal = df.format(bd); //dateFormat.format(bd);
        gen.writeString(formattedBigDecimal);
    }
}

