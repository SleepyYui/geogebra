package geogebra.common.move.ggtapi.models;

import geogebra.common.main.App;
import geogebra.common.move.ggtapi.models.json.JSONArray;
import geogebra.common.move.ggtapi.models.json.JSONObject;
import geogebra.common.move.ggtapi.models.json.JSONString;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * For Generating a JSON String for specific GeoGebratube API Requests
 * 
 * @author Matthias Meisinger
 */
public class MaterialRequest implements Request
{
	enum Task
	{
		fetch;
	}

	public enum Fields
	{
		id, title, type, description, timestamp, author, author_url, url, url_direct, language, thumbnail, featured, likes,
		width, height, instructions_pre, instructions_post, ggbBase64;
	}

	public enum Filters
	{
		id, title, search, type, description, timestamp, author, author_url, language, featured, likes, inbook;
	}

	public enum Order
	{
		id, title, type, description, timestamp, author, language, featured, likes, relevance;
	}

	public enum Type
	{
		asc, desc;
	}

	private static final String api = "1.1.0";
	private Task task = Task.fetch;

	private Fields[] fields = new Fields[]{Fields.id, Fields.title, Fields.type, Fields.timestamp, 
			Fields.author, Fields.author_url, Fields.url, Fields.url_direct, 
			Fields.thumbnail, Fields.featured, Fields.likes,
			};
	private Filters[] filters = { Filters.search };
	private Map<Filters,String> filterMap = new HashMap<Filters,String>();
	private Order by = Order.relevance;
	private Type type = Type.desc;
	private int limit = GeoGebraTubeAPI.STANDARD_RESULT_QUANTITY;

	private JSONObject requestJSON = new JSONObject();
	private JSONObject apiJSON = new JSONObject();
	private JSONObject taskJSON = new JSONObject();
	private JSONObject fieldsJSON = new JSONObject();
	private JSONArray fieldJSON = new JSONArray();

	private JSONObject filtersJSON = new JSONObject();
	private JSONArray filterJSON = new JSONArray();

	private JSONObject orderJSON = new JSONObject();
	private JSONObject limitJSON = new JSONObject();
	private final AuthenticationModel model;
	private final ClientInfo client;
	private TreeSet<Filters> negFilters = new TreeSet<Filters>();
	/**
	 * Constructor for a Featured Materials Request
	 */
	public MaterialRequest(ClientInfo client)
	{
		this.client = client;
		this.model = client.getModel();
	}

	/**
	 * Constructor for a Search Request
	 * 
	 * @param query search term or #id
	 */
	public MaterialRequest(String query, ClientInfo client)
	{
		this(client);
		this.filterMap.put(Filters.type, "ggb");
		if(query!=null && query.startsWith("#")){
			this.filters = new Filters[] { Filters.id };
			this.filterMap.put(Filters.id, query.substring(1));
			this.by = Order.timestamp;
		}
		else{
			this.filters = new Filters[] { Filters.search };
			this.filterMap.put(Filters.search, query);
		}
	}

	/**
	 * Constructor for a Request by ID
	 * 
	 * @param filters
	 * @param by
	 */
	public MaterialRequest(int id, ClientInfo client)
	{
		this(client);
		this.fields = Fields.values();
		this.by = Order.id;
		this.filters = new Filters[] { Filters.id };
		this.filterMap.put(Filters.type, "ggb");
		this.filterMap.put(Filters.id, id+"");
	}

	public String toJSONString()
	{
		this.apiJSON.put("-api", new JSONString(MaterialRequest.api));
		this.taskJSON.put("-type", new JSONString(this.task.toString()));

		for (int i = 0; i < this.fields.length; i++)
		{
			JSONObject current = new JSONObject();
			current.put("-name", new JSONString(this.fields[i].toString()));
			this.fieldJSON.set(i, current);
		}

		this.fieldsJSON.put("field", this.fieldJSON);

		for (int i = 0; i < this.filters.length; i++)
		{
			JSONObject current = new JSONObject();
			current.put("-name", new JSONString(this.filters[i].toString()));
			if(this.negFilters.contains(filters[i])){
				current.put("-comp", new JSONString("neq"));
			}
			if (this.filterMap.get(this.filters[i]) != null){
				current.put("#text", new JSONString(this.filterMap.get(this.filters[i])));
			}

			this.filterJSON.set(i, current);
		}

		this.filtersJSON.put("field", this.filterJSON);

		this.orderJSON.put("-by", new JSONString(this.by.toString()));
		this.orderJSON.put("-type", new JSONString(this.type.toString()));
		this.limitJSON.put("-num", new JSONString(String.valueOf(this.limit)));

		this.taskJSON.put("fields", this.fieldsJSON);
		this.taskJSON.put("filters", this.filtersJSON);
		this.taskJSON.put("order", this.orderJSON);
		this.taskJSON.put("limit", this.limitJSON);
		if(this.model != null && model.getLoginToken()!=null){
			JSONObject login = new JSONObject();
			login.put("-token", model.getLoginToken());
			this.apiJSON.put("login", login);
		}
		if(this.client != null){
			JSONObject clientJSON = new JSONObject();
			clientJSON.put("-id", client.getId());
			clientJSON.put("-width", client.getWidth()+"");
			clientJSON.put("-height", client.getHeight()+"");
			clientJSON.put("-type", client.getType());
			clientJSON.put("-language", client.getLanguage());
			this.apiJSON.put("client", clientJSON);
		}
		this.apiJSON.put("task", this.taskJSON);
		this.requestJSON.put("request", this.apiJSON);
		App.debug(this.requestJSON.toString());
		return this.requestJSON.toString();
	}

	public static MaterialRequest forUser(int userId, ClientInfo client) {
		MaterialRequest req = new MaterialRequest(client);
		req.filters = new Filters[] { Filters.author_url };
		req.filterMap.put(Filters.type, "link");
		req.negFilters.add(Filters.type);
		req.filterMap.put(Filters.author_url, userId+"");
		req.by = Order.timestamp;
		return req;
	}

	public static MaterialRequest forFeatured(ClientInfo client) {
		MaterialRequest req = new MaterialRequest(client);
		req.filters = new Filters[] { Filters.featured, Filters.type };
		req.filterMap.put(Filters.type, "link");
		req.negFilters.add(Filters.type);
		req.filterMap.put(Filters.featured, "true");
		req.by = Order.timestamp;
		req.type = Type.desc;
		return req;
	}

	public static MaterialRequest forBook(int id, ClientInfo client) {
		MaterialRequest req = new MaterialRequest(client);
		req.filters = new Filters[] { Filters.inbook, Filters.type };
		req.filterMap.put(Filters.type, "link");
		req.negFilters.add(Filters.type);
		req.filterMap.put(Filters.inbook, id+"");
		req.by = Order.timestamp;
		req.type = Type.desc;
		return req;
	}
}
