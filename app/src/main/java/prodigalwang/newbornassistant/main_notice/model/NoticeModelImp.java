package prodigalwang.newbornassistant.main_notice.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import prodigalwang.newbornassistant.bean.PageInfo;
import prodigalwang.newbornassistant.bean.SchoolNotice;
import prodigalwang.newbornassistant.main.model.MainDataModelImp;

/**
 * Created by ProdigalWang on 2016/12/14
 */

public class NoticeModelImp extends MainDataModelImp<SchoolNotice> {


    private static final String cache = "home_notice";

    @Override
    protected String getCacheAlias() {
        return cache;
    }

    @Override
    protected PageInfo parseData(String response) {
        Gson gson = new Gson();
        Type type = new TypeToken<PageInfo<SchoolNotice>>() {
        }.getType();

        PageInfo<SchoolNotice> pageInfo = gson.fromJson(response, type);
        return pageInfo;
    }
}
