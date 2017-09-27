package prodigalwang.newbornassistant.main_image.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import prodigalwang.newbornassistant.bean.PageInfo;
import prodigalwang.newbornassistant.bean.SchoolImage;
import prodigalwang.newbornassistant.main.model.MainDataModelImp;

/**
 * Created by ProdigalWang on 2016/12/12
 */

public class ImageModelImpl extends MainDataModelImp<SchoolImage> {


    private static final String cache="home_image";

    @Override
    protected String getCacheAlias() {
        return cache;
    }

    @Override
    protected PageInfo parseData(String response) {
        Gson gson = new Gson();
        Type type = new TypeToken<PageInfo<SchoolImage>>() {
        }.getType();

        PageInfo<SchoolImage> pageInfo = gson.fromJson(response, type);
        return pageInfo;
    }
}
