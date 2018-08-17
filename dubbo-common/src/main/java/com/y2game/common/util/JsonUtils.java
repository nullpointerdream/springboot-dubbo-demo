package com.y2game.common.util;

import com.google.gson.*;
import com.y2game.common.exception.JsonException;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

public final class JsonUtils {

	private static ThreadLocal<Gson> local = new ThreadLocal<Gson>();

	private static Gson getGson() {
		if (local.get() == null) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
				public Date deserialize(JsonElement p1, Type p2,JsonDeserializationContext p3) {
					return new Date(p1.getAsLong());
				}
			}).registerTypeAdapter(Date.class, new JsonSerializer<Date>(){
				public JsonElement serialize(Date arg0, Type arg1,JsonSerializationContext arg2) {
					return new JsonPrimitive(arg0.getTime());
				}
			});
			Gson gson = gsonBuilder.create();
			local.set(gson);
			return gson;
		} else {
			return local.get();
		}
	}

	public static <T> T fromJson(String json, Class<T> cls) {
		try {
			return getGson().fromJson(json, cls);
		} catch (Exception e) {
			throw JsonException.newInstance();
		}
	}

	public static <T> T fromJson(String json,  Type typeOfT) {
		try {
			return getGson().fromJson(json, typeOfT);
		} catch (Exception e) {
			throw JsonException.newInstance(); 
		}
	}

	public static String toJson(Object obj) {
		try {
			return getGson().toJson(obj);
		} catch (Exception e) {
			throw JsonException.newInstance(); 
		}
	}
	/**
	 * map 转化为 bean
	 * @param clazz
	 * @param map
	 * @return
	 */
	public static Object mapToBean(Class clazz,Map map){
		Object object = null;
		try {
			object = clazz.newInstance();
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);

			PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
			for(PropertyDescriptor descriptor : descriptors){
				String propertyName = descriptor.getName();
				if(map.containsKey(propertyName)){
					Object value = map.get(propertyName);
					Object[] args = new Object[1];
					args[0] = value;
					descriptor.getWriteMethod().invoke(object, args);
				}
			}

		} catch (Exception e) {
			throw JsonException.newInstance();
		}
		return object;
	}
}