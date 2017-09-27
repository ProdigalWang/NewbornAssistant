package prodigalwang.newbornassistant.educational.model.et_classtable;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;
import prodigalwang.newbornassistant.app.AppConfig;
import prodigalwang.newbornassistant.bean.Course;
import prodigalwang.newbornassistant.cache.CacheManager;
import prodigalwang.newbornassistant.utils.LogUtil;
import prodigalwang.newbornassistant.utils.ThreadPoolUtil;
import prodigalwang.newbornassistant.utils.Urls;

/**
 * Created by ProdigalWang on 2017/5/24.
 */

public class EtClassTableModelImp implements IEtClassTableModel {


    private List<Course> courses;//保存课表
    private LinkedHashMap<String, String> terms; //保存学期和学期代号

    @Override
    public void getTermFromHtml(final EtGetTermCallBack getTermCallBack, HashMap<String, String> params) {


        OkHttpUtils.get().url(Urls.SCHOOL_TERM)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

                getTermCallBack.fail(1001, "请求出错！");
            }

            @Override
            public void onResponse(String response, int id) {

                resolveTerm(response);

                if (terms != null && terms.size() > 0) {
                    getTermCallBack.success(terms);

                } else {
                    getTermCallBack.fail(1001, "获取学期失败！");
                }

            }
        });

    }

    @Override
    public void getClassTableFromHtml(final EtClassTableCallback etClassTableCallback, HashMap<String, String> params) {

        OkHttpUtils.post()
                .url(Urls.SCHOOL_CLASS_TABLE).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

                etClassTableCallback.fail(1000, "请求出错！");
            }

            @Override
            public void onResponse(String response, int id) {

                resolveClassTable(response);

                if (courses != null && courses.size() > 1) {

                    etClassTableCallback.success(courses);
                } else if (courses != null && courses.size() == 1) {

                    etClassTableCallback.fail(1000, "您该学期没有课程！");
                } else if (courses == null || courses.size() < 1) {
                    etClassTableCallback.fail(1000, "解析课表出错!");
                }
            }
        });
    }

    /**
     * 提取学期列表数据
     * @param data
     */
    private void resolveTerm(String data) {
        try {
            Document document = Jsoup.parse(data);
            //获取option标签
            Elements options = document.getElementsByTag("option");

            //用LinkedHashMap可以保证读取数据的顺序，HashMap不保证
             terms = new LinkedHashMap<>(options.size());

            //<option value="11">2016-2017学年第一学期</option>
            //提取出value的值和对应的学期数
            for (int i = 0; i < options.size(); i++) {

                terms.put(options.get(i).text(), options.get(i).attr("value"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 提取完整的课表数据
     * @param data
     */
    private void resolveClassTable(String data) {

        courses = new ArrayList<>();
        try {
            Document document = Jsoup.parse(data);
            Elements scripts = document.getElementsByTag("script");

           // LogUtil.d("scripts size is : " + scripts.size());

            if (scripts.size() == 1) {

                courses.add(new Course());
                return;
            }
            //第一层循环筛选出html中的课表数据，并进行切割为每一个字段
            for (int k = 1; k < scripts.size(); k++) {
                //从第二个script标签获取课程信息，第一个是没用的
                String str = scripts.get(k).data().trim();

              //  LogUtil.d("classTable str is" + str);

                //InsertSchedule("TABLE2#1","57198","计算机图形学","丁革媛","1-12","整周","3-A203","");
                str = str.substring(str.indexOf('\"') + 1);
                str = str.substring(0, str.lastIndexOf('\"'));
                str = str.replace("\"", "");

             //   LogUtil.d("resolved classTable str is" + str);

                String[] bean = str.split(",");

                Course oneCourse = new Course();

                //"TABLE2#1","57198","计算机图形学","丁革媛","1-12","整周","3-A203",""
                for (int i = 0; i < bean.length; i++) {
                    switch (i) {
                        case 0:
                            resolveCourseDay(bean[i],oneCourse);
                            break;
                        case 1:
                            oneCourse.setNumber(bean[i]);
                            break;
                        case 2:
                            oneCourse.setName(bean[i]);
                            break;
                        case 3:
                            oneCourse.setTeacher(bean[i]);
                            break;
                        case 4:
                            oneCourse.setTime(bean[i]);
                            break;
                        case 5:
                            oneCourse.setDuration(bean[i]);
                            break;
                        case 6:
                            oneCourse.setClassroom(bean[i]);
                            break;
                        default:
                            break;
                    }
                }

                courses.add(oneCourse);

                //保存课表数据到手机文件
                ThreadPoolUtil.getThreadpool().execute(new saveClassTable());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 进一步从数据中解析这节课是周几的第几节课
     * @param str  字符串
     * @param course 保存数据的对象
     */
    private void resolveCourseDay(String str,Course course) {
        //TABLE2#1
        str = str.substring(5);//去掉TABLE
        String[] data = str.split("#");

        switch (data[0]){
            case "1":
                course.setPart("0");//1-2节
                break;
            case "2":
                course.setPart("1");
                break;
            case "3":
                course.setPart("2");
                break;
            case "4":
                course.setPart("3");
                break;
            case "5":
                course.setPart("4");
                break;
            default:
                break;
        }

        switch (data[1]){
            case "1":
                course.setDay("0");//星期一
                break;
            case "2":
                course.setDay("1");
                break;
            case "3":
                course.setDay("2");
                break;
            case "4":
                course.setDay("3");
                break;
            case "5":
                course.setDay("4");
                break;
            case "6":
                course.setDay("5");
                break;
            case "7":
                course.setDay("6");
                break;
            default:
                break;
        }
    }

    /**
     * 课表数据保存到本地文件中
     */
    private class saveClassTable implements Runnable {

        @Override
        public void run() {
            if (courses != null) {

                CacheManager.saveData(courses, AppConfig.getInstance().creatClassTableFolder(), AppConfig.getInstance().classTableName);

            }
        }
    }
}
