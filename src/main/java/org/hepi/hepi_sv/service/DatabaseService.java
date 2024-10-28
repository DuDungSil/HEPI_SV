package org.hepi.hepi_sv.service;

import java.util.ArrayList;
import java.util.List;

import org.hepi.hepi_sv.errorHandler.ErrorHandler;
import org.hepi.hepi_sv.model.dto.Exercise.ExerciseRequest;
import org.hepi.hepi_sv.model.dto.Exercise.ExerciseResult;
import org.hepi.hepi_sv.model.dto.Nutrient.NutrientRequest;
import org.hepi.hepi_sv.model.dto.Nutrient.NutrientResult;
import org.hepi.hepi_sv.model.vo.EventImage;
import org.hepi.hepi_sv.model.vo.Gym;
import org.hepi.hepi_sv.model.vo.NutrientProfile;
import org.hepi.hepi_sv.model.vo.Product;
import org.hepi.hepi_sv.model.vo.ShopProduct;
import org.hepi.hepi_sv.model.vo.User;
import org.hepi.hepi_sv.myBatis.InsertMapper;
import org.hepi.hepi_sv.myBatis.SelectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    @Autowired
    private SelectMapper selectMapper;

    @Autowired
    private InsertMapper insertMapper;

    public User selectUser(String id) {
        return selectMapper.selectUser(id);
    }

    public Gym selectGym(String id) {
        return selectMapper.selectGym(id);
    }
    public List<EventImage> selectEventImage() {
        List<EventImage> list = new ArrayList<>();
        selectMapper.selectEventImage().forEach(e -> {
            list.add(e);
        });
        return list;
    }

    public List<Product> selectEventProduct() {
        List<Product> list = new ArrayList<>();
        selectMapper.selectEventProduct().forEach(e -> {
            list.add(e);
        });
        return list;
    }

    public List<Product> selectMyProduct(String id) {
        List<Product> list = new ArrayList<>();
        selectMapper.selectMyProduct(id).forEach(e -> {
            list.add(e);
        });
        return list;
    }

    public List<Product> selectCartProduct(String id) {
        List<Product> list = new ArrayList<>();
        selectMapper.selectCartProduct(id).forEach(e -> {
            list.add(e);
        });
        return list;
    }

    public void insertUser(User user) {
        try {
            insertMapper.insertUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }

    public User checkPhone(String phone) {
        try {
            return selectMapper.checkPhone(phone);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }

    // exercise 부분
    public List<String> getTags(){
        try {
            return selectMapper.selectTags();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }

    public String selectStrength(String part) {
        try {
            return selectMapper.selectStrength(part);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }

    public List<String> selectBodyDetailParts(String part) {
        try {
            return selectMapper.selectBodyDetailParts(part);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }

    public List<NutrientProfile> selectNutrientProfilesByLevel(int level) {
        try {
            return selectMapper.selectNutrientProfilesByLevel(Integer.toString(level));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }

    public List<NutrientProfile> selectNutrientProfilesByPurpose(String purpose) {
        try {
            return selectMapper.selectNutrientProfilesByPurpose(purpose);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }
    
    public void insertExerRequest(ExerciseRequest request) {
        try {
            insertMapper.insertExerRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }

    public void insertExerResult(ExerciseResult result, int id) {
        try {
            insertMapper.insertExerResult(result, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }

    // nutrient 부분
    public List<String> selectRecommendSource(String nutrient_type, String diet_type) {
        try {
            return selectMapper.selectRecommendSource(nutrient_type, diet_type);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }

    public List<ShopProduct> selectRecommendProductByType(String nutrient_type, String diet_type) {
        try {
            return selectMapper.selectShopProductByType(nutrient_type, diet_type);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }

    public void insertNutriRequest(NutrientRequest request) {
        try {
            insertMapper.insertNutriRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }

    public void insertNutriResult(NutrientResult result, int id) {
        try {
            insertMapper.insertNutriResult(result, id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorHandler("오류가 발생했습니다");
        }
    }
}
