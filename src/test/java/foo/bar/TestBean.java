package foo.bar;

import java.util.HashMap;
import java.util.Map;

import com.daumcorp.commdev1.bot.ci.Storable;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

public class TestBean implements Storable {
	public Map<String, String> map = Maps.newHashMap();

	@Override
	public String store() {
		return new Gson().toJson(map);
	}

	@Override
	public void load(String json) {
		@SuppressWarnings("unchecked")
		Map<String, String> loaded = new Gson().fromJson(json, HashMap.class);
		map.putAll(loaded);
	}

}
