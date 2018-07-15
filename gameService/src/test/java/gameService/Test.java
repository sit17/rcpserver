package gameService;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.i5i58.config.SqlserverConfig;
import com.i5i58.util.JsonUtils;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Test {

	static JsonUtils jsonUtils = new JsonUtils();

	public static void main(String[] args) {
		SqlserverConfig.init();

		// try {
		// String aaa ="asdfasdf{1111}skjxdfh{2222}xddf{3333}sdfs";
		// Map<String, String> map = new HashMap<String, String>();
		// map.put("key1", aaa);
		// map.put("key2", aaa);
		// String json = jsonUtils.toJson(map);
		// System.out.println(json);
		// @SuppressWarnings("unchecked")
		// Map<String, String> bbb =
		// (Map<String,String>)jsonUtils.toObject(json, HashMap.class);
		// for(Entry<String, String> kv : bbb.entrySet())
		// {
		// System.out.println(kv.getKey()+":"+kv.getValue());
		// }
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
}
