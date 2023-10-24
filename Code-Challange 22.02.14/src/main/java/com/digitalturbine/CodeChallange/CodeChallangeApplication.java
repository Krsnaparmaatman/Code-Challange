package com.digitalturbine.CodeChallange;
import java.io.IOException;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RestController;




@RestController
@SpringBootApplication

public class CodeChallangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeChallangeApplication.class, args);
	}

	@GetMapping
	public ResponseEntity<String> helloWorld() {
		return ResponseEntity.ok("Hello, world!");
	}

	@PostMapping("/process")
	public ResponseEntity<List<String>> processFile(@RequestParam("file") final MultipartFile file,
													@RequestParam(required = false, name = "attribute_key") final String attributeKey,
													@RequestParam(required = false, name = "attribute_value") final String attributeValue) {
		try {
			List<String> primaryIds = new ArrayList<>();
			JSONArray modules = getModulesFromJsonFile(file);
			for (int module=0; module < modules.length(); module++) {
				JSONObject resources = modules.getJSONObject(module).getJSONObject("resources");
				for (String key : resources.keySet()) {
					JSONObject resource = resources.getJSONObject(key);
					JSONObject primary = resource.getJSONObject("primary");
					if (ObjectUtils.isEmpty(attributeKey)) {
						if ("aws_security_group".equals(resource.get("type"))) {
							primaryIds.add(primary.getString("id"));
						}
					} else {
						if ("aws_security_group_rule".equals(resource.get("type"))) {
							JSONObject attributes = primary.getJSONObject("attributes");
							if(attributes.keySet().contains(attributeKey)) {
								String attributeSourceSecurityGroupId = attributes.getString("source_security_group_id");
								if (null != attributeValue && attributeSourceSecurityGroupId.equalsIgnoreCase(attributeValue)) {
									primaryIds.add(primary.getString("id"));
								}
							}
						}
					}
				}
			}
			return ResponseEntity.ok(primaryIds);
		} catch (IOException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	private JSONArray getModulesFromJsonFile(final MultipartFile file) throws IOException {
		JSONObject json = new JSONObject(new String(file.getBytes()));
		return json.getJSONArray("modules");
	}
}
