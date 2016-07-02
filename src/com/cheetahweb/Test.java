package com.cheetahweb;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

@Path("/")
public class Test {
	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		return "";
	}

	@GET
	@Path("/getData")
	@Produces(MediaType.TEXT_PLAIN)
	public String getData() {
		JSONObject result = new JSONObject();
		List<Integer> list = new ArrayList<Integer>();
		Random random = new Random();
		list.add(random.nextInt(101));
		list.add(random.nextInt(101));
		result.put("data", list);
		return result.toString();
	}

	@GET
	@Path("/getData1")
	@Produces(MediaType.TEXT_PLAIN)
	public String getData1() {
		JSONObject result = new JSONObject();
		List<Integer> list = new ArrayList<Integer>();

		result.put("data", list);
		return result.toString();
	}
}
