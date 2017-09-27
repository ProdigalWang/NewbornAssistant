package prodigalwang.newbornassistant.educational.presenter.et_classtable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import prodigalwang.newbornassistant.bean.Course;
import prodigalwang.newbornassistant.educational.model.et_classtable.EtClassTableCallback;
import prodigalwang.newbornassistant.educational.model.et_classtable.EtClassTableModelImp;
import prodigalwang.newbornassistant.educational.model.et_classtable.EtGetTermCallBack;
import prodigalwang.newbornassistant.educational.model.et_classtable.IEtClassTableModel;
import prodigalwang.newbornassistant.educational.view.et_classtable.IEtClassTableView;
import prodigalwang.newbornassistant.utils.NetUtil;

/**
 * Created by ProdigalWang on 2017/5/24.
 */

public class EtClassTablePImp implements IEtClassTablePresenter {


    private IEtClassTableView mEtClassTableView;
    private IEtClassTableModel iEtClassTableModel;

    public EtClassTablePImp(IEtClassTableView iEtClassTableView) {

        this.mEtClassTableView = iEtClassTableView;
        iEtClassTableModel = new EtClassTableModelImp();
    }

    @Override
    public void loadClassTable() {

        getTermData();
    }

    public void getTermData() {

        if (NetUtil.isConnected()) {

            mEtClassTableView.showProgress("正在获取数据...");
            iEtClassTableModel.getTermFromHtml(new EtGetTermCallBack() {
                @Override
                public void success(LinkedHashMap<String, String> terms) {

                    if (terms != null && terms.size() > 0) {

                        mEtClassTableView.hideProgress();

                        resolveTerm(terms);
                        mEtClassTableView.showTerm(termList, termNoList);

                        //请求当前学期课表
                        getClassTable(termNoList.get(0));
                    }
                }

                @Override
                public void fail(int Err_Code, String msg) {

                    mEtClassTableView.hideProgress();
                    mEtClassTableView.showFailMsg(msg);
                }
            }, null);

        } else {

            mEtClassTableView.hideProgress();
            mEtClassTableView.showFailMsg("当前无网络！");
        }


    }

    @Override
    public void getClassTable(String termNo) {
        HashMap<String, String> params = new HashMap<>();
        params.put("YearTermNO", termNo);

        if (NetUtil.isConnected()) {

            mEtClassTableView.showProgress("正在获取数据...");
            iEtClassTableModel.getClassTableFromHtml(new EtClassTableCallback() {
                @Override
                public void success(List<Course> courses) {

                    mEtClassTableView.hideProgress();

                    resolveClassTable(courses);
                    mEtClassTableView.showClassTable(courses, contents);

                }

                @Override
                public void fail(int Err_Code, String msg) {
                    mEtClassTableView.hideProgress();
                    mEtClassTableView.showFailMsg(msg);
                }
            }, params);

        }
    }

    private List<String> termList;
    private List<String> termNoList;
    private String[][] contents;

    /**
     * 继续处理学期数据，返回View可以直接使用的数据
     *
     * @param terms
     */
    private void resolveTerm(HashMap<String, String> terms) {

        termList = new ArrayList<>();
        termNoList = new ArrayList<>();

        for (String key : terms.keySet()) {
            termList.add(key);
            termNoList.add(terms.get(key));
        }
    }

    /**
     * 处理课表数据，View拿到课表数据和直接显示的数据
     *
     * @param courses
     */
    private void resolveClassTable(List<Course> courses) {
        contents = new String[5][7];

        //先初始化二维数组，后面没被赋值的就为""
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                contents[i][j] = "";
            }
        }
        for (int i = 0; i < courses.size(); i++) {
            Course oneCourse = courses.get(i);
            String part = oneCourse.getPart();
            String day = oneCourse.getDay();

            switch (part) {
                case "0"://1-2节
                    switch (day) {
                        case "0"://周一
                            contents[0][0] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "1":
                            contents[0][1] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "2":
                            contents[0][2] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "3":
                            contents[0][3] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "4":
                            contents[0][4] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "5":
                            contents[0][5] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "6":
                            contents[0][6] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                    }
                    break;
                case "1"://3-4节
                    switch (day) {
                        case "0"://周一
                            contents[1][0] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "1":
                            contents[1][1] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "2":
                            contents[1][2] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "3":
                            contents[1][3] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "4":
                            contents[1][4] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "5":
                            contents[1][5] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "6":
                            contents[1][6] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                    }
                    break;
                case "2"://5-6节
                    switch (day) {
                        case "0"://周一
                            contents[2][0] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "1":
                            contents[2][1] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "2":
                            contents[2][2] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "3":
                            contents[2][3] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "4":
                            contents[2][4] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "5":
                            contents[2][5] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "6":
                            contents[2][6] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                    }
                    break;
                case "3"://7-8节
                    switch (day) {
                        case "0"://周一
                            contents[3][0] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "1":
                            contents[3][1] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "2":
                            contents[3][2] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "3":
                            contents[3][3] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "4":
                            contents[3][4] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "5":
                            contents[3][5] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "6":
                            contents[3][6] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                    }
                    break;
                case "4"://9-10节
                    switch (day) {
                        case "0"://周一
                            contents[4][0] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "1":
                            contents[4][1] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "2":
                            contents[4][2] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "3":
                            contents[4][3] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "4":
                            contents[4][4] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "5":
                            contents[4][5] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                        case "6":
                            contents[4][6] = oneCourse.getName() + "\n" + oneCourse.getClassroom();
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
