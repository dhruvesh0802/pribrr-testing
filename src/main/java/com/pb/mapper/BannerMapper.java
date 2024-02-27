package com.pb.mapper;

import com.pb.config.GlobalConfiguration;
import com.pb.dto.BannerDTO;
import com.pb.model.BannerEntity;
import com.pb.service.AWSS3Service;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BannerMapper implements OrikaMapperFactoryConfigurer {

    @Autowired
    private GlobalConfiguration globalConfiguration;
    @Autowired
    private AWSS3Service awss3Service;
    
    @Override
    public void configure(MapperFactory orikaMapperFactory) {
            orikaMapperFactory.classMap(BannerEntity.class, BannerDTO.class)
                    .customize(new CustomMapper<BannerEntity, BannerDTO>() {
                        @Override
                        public void mapAtoB(BannerEntity bannerEntity, BannerDTO bannerDTO, MappingContext context) {
                            GlobalConfiguration.AmazonS3 amazonS3 = globalConfiguration.getAmazonS3();
                            String imagePath = amazonS3.getS3BaseUrl() + amazonS3.getUploadFolderName() + amazonS3.getBannerImages() + bannerEntity.getBannerPath();
                            bannerDTO.setBannerPath(imagePath);
                        }
                    })
                    .byDefault()
                    .register();
    }
}
