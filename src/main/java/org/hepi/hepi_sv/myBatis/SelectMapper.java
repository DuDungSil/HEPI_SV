package org.hepi.hepi_sv.myBatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.hepi.hepi_sv.model.vo.EventImage;
import org.hepi.hepi_sv.model.vo.Gym;
import org.hepi.hepi_sv.model.vo.NutrientProfile;
import org.hepi.hepi_sv.model.vo.Product;
import org.hepi.hepi_sv.model.vo.ShopProduct;
import org.hepi.hepi_sv.model.vo.User;

@Mapper
public interface SelectMapper {

    public User selectUser(String USER_ID);
    public Gym selectGym(String USER_ID);
    public List<Product> selectEventProduct();
    public List<Product> selectMyProduct(String USER_ID);
    public List<Product> selectCartProduct(String USER_ID);
    public List<EventImage> selectEventImage();
    public User checkPhone(String PHONE);
    
    // exercise 부분
    public List<String> selectTags();

    public String selectStrength(String PART);
    public List<String> selectBodyDetailParts(String PART);

    public List<NutrientProfile> selectNutrientProfilesByLevel(String LEVEL);

    public List<NutrientProfile> selectNutrientProfilesByPurpose(String PURPOSE);
    
    // nutrient 부분
    public List<String> selectRecommendSource(String NUTRIENT_TYPE, String DIET_TYPE);

    public List<ShopProduct> selectShopProductByType(String NUTRIENT_TYPE, String DIET_TYPE);
}
