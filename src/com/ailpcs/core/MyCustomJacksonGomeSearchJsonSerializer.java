package com.ailpcs.core;
import java.io.IOException;  
import java.util.ArrayList;  
import java.util.List;  
  
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
 * 自定义null处理类, 用于Jackson中自定义null替代值
 * --有些特殊情况,我们不能将null字段直接去除,而是需要给予一个默认的值,则可以设置如下配置 , 其中GomeSearchJsonSerializer
 * 是我们自定义的null处理类, 如下:
 * <property name="serializerProvider">  
 *     <bean class="com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.Impl">  
 *         <property name="nullValueSerializer">  
 *             <bean class="com.ailpcs.core.MyCustomJacksonGomeSearchJsonSerializer"></bean>  
 *         </property>  
 *     </bean>  
 * </property>  
 * 
 */

public class MyCustomJacksonGomeSearchJsonSerializer extends JsonSerializer<Object> {  
   @Override  
   public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)  
           throws IOException, JsonProcessingException {  
       jgen.writeString(""); //MichaelTsui: 值为null的字段序列化时处理为空串("") 
       //jgen.writeObject(new JsonNullDef());  //MichaelTsui: JsonNullDef类在json序列化后是长这个样子: {def: []}
   }  
}  


class JsonNullDef{  
    private List<String> def = new ArrayList<String>();  
    public List<String> getDef() {  
        return def;  
    }  
    public void setDef(List<String> def) {  
        this.def = def;  
    }  
}  