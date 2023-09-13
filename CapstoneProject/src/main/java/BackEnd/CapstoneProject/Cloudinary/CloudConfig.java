package BackEnd.CapstoneProject.Cloudinary;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudConfig {

	@Bean
	public Cloudinary getCloudinary() {

		Map config = new HashMap();
		config.put("cloud_name", "drjfwr7dc");
		config.put("api_key", "498472658559532");
		config.put("api_secret", "dovAb29svN3usgLWuzDDhxEOqyU");
		config.put("secure", true);

		return new Cloudinary(config);
	}

//	@Bean
//	public Cloudinary getCloudinary() {
//		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", "drjfwr7dc", "api_key",
//				"498472658559532", "api_secret", "dovAb29svN3usgLWuzDDhxEOqyU", "secure", true));
//		return cloudinary;
//	}

}
