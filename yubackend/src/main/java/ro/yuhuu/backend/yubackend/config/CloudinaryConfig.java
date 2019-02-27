package ro.yuhuu.backend.yubackend.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

//    private CloudinaryConfig(){}
//
//    private static Cloudinary INSTANCE = null;
//
//    public static Cloudinary getInstance(){
//
//        if(INSTANCE == null){
//            return new Cloudinary(ObjectUtils.asMap(
//                    "cloud_name", "yuhuubackend",
//                    "api_key", "532862564978818",
//                    "api_secret", "QkgThixo6CmT4_SkFrVT3iu1BMw"));
//        }
//
//        return INSTANCE;
//
//    }

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "yuhuubackend",
                    "api_key", "532862564978818",
                    "api_secret", "QkgThixo6CmT4_SkFrVT3iu1BMw"));
    }

}
