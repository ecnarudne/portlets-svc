package models;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import play.libs.Json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PortletTest {

	@Test
	public void testFromJson() throws JsonProcessingException, IOException {
		Portlet portlet = new Portlet("n", new User("pro", "id"), "url", Arrays.asList(new Sector("s")));
		JsonNode node = Json.toJson(portlet);
		String text = node.textValue();
		System.out.println(text);

		//String text = "{\"name\":\"n\"}";
		JsonNode deserializedNode = new ObjectMapper().readTree(text);
		Portlet deserializedPortlet = Portlet.fromJson(deserializedNode);
		assertEquals("n", deserializedPortlet.getName());
	}
}
