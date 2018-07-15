package com.i5i58.util;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;

@Component
public class JsonUtils {
	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final ObjectMapper mapper;

	/**
	 * 单例化 Jackson Object Mapper.
	 * 
	 * @return
	 */
	public ObjectMapper getMapper() {
		return mapper;
	}

	/**
	 * 加载初始化.
	 */
	static {
		// 默认日期格式化
		SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

		mapper = new ObjectMapper();
		mapper.setDateFormat(dateFormat);
		// 空值格式化
		mapper.getSerializerProvider().setNullValueSerializer(new NullSerializer());
		// HTML Unescape
		// 注解扫描
		mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
			private static final long serialVersionUID = 930214715071728663L;

			@Override
			public Object findSerializer(Annotated a) {
				if (a instanceof AnnotatedMethod) {
					AnnotatedElement m = a.getAnnotated();
					DateTimeFormat an = m.getAnnotation(DateTimeFormat.class);
					if (null != an && !DEFAULT_DATE_FORMAT.equals(an.pattern())) {
						return new JsonDateSerializer(an.pattern());
					}
				}
				return super.findSerializer(a);
			}
		});
	}

	/**
	 * 对象转换为JSON
	 * 
	 * @param obj
	 * @return
	 */
	public String toJson(Object obj) throws IOException {
		return mapper.writeValueAsString(obj);

	}

	/**
	 * JSON转换为对象
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public <T> T toObject(String json, Class<T> clazz) throws IOException {
		return mapper.readValue(json, clazz);
	}

	/**
	 * JSON数组转换为ArrayList
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public <T> List<T> toList(String json, Class<T> clazz) throws IOException {
		TypeFactory t = TypeFactory.defaultInstance();
		return mapper.readValue(json, t.constructCollectionType(ArrayList.class, clazz));
	}

	public String appendField(String json, String key, String value) {
		return json.substring(0, json.length() - 1) + ",\"" + key + "\":\"" + value + "\"}";
	}

	public String appendFields(String json, Map<String, Object> fields) {
		StringBuilder ret = new StringBuilder();
		ret.append(json.substring(0, json.length() - 1));
		for (Map.Entry<String, Object> entry : fields.entrySet()) {
			ret.append(",\"");
			ret.append(entry.getKey());
			ret.append("\":\"");
			ret.append(entry.getValue());
			ret.append("\"");
		}
		ret.append("}");
		return ret.toString();
	}

	public String createFromField(String key, String value) {
		return "{\"" + key + "\":\"" + value + "\"}";
	}

	/**
	 * 自定义日期JSON格式化
	 * <p>
	 * 
	 * </p>
	 * 
	 * @author: idong
	 * @date: 2015年6月9日 上午9:30:39
	 * @version: V1.0
	 */
	private static class JsonDateSerializer extends JsonSerializer<Date> {
		private SimpleDateFormat dateFormat;

		public JsonDateSerializer(String format) {
			dateFormat = new SimpleDateFormat(format);
		}

		@Override
		public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException {
			String value = dateFormat.format(date);
			gen.writeString(value);
		}
	}

	/**
	 * 自定义空值JSON格式化
	 * <p>
	 * 
	 * </p>
	 * 
	 * @author: idong
	 * @date: 2015年6月9日 上午9:30:58
	 * @version: V1.0
	 */
	private static class NullSerializer extends JsonSerializer<Object> {
		@Override
		public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
			jgen.writeString("");
		}
	}

}
