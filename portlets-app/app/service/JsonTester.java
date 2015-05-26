package service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import play.libs.Json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@CorsComposition.Cors
public class JsonTester {	
	
	public static JsonNode getJsonObject(String jsonFilePath){
		ObjectMapper mapper = new ObjectMapper();
		List<Object> list = null;
		
		try {
			list  = mapper.readValue(new File(jsonFilePath), new TypeReference<List<Object>>(){});
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Json.toJson(list);
	}
}
