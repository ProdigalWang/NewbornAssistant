package prodigalwang.newbornassistant.educational.view.et_classtable;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.base.BaseActivity;
import prodigalwang.newbornassistant.bean.Course;
import prodigalwang.newbornassistant.educational.presenter.et_classtable.EtClassTablePImp;
import prodigalwang.newbornassistant.educational.presenter.et_classtable.IEtClassTablePresenter;
import prodigalwang.newbornassistant.educational.view.et_classtable.ui.AbsGridAdapter;
import prodigalwang.newbornassistant.utils.LogUtil;

public class ClassTableActivity extends BaseActivity implements IEtClassTableView {

    @Override
    protected boolean isSwipeBack() {
        return false;
    }

    @Override
    protected boolean hasLoadingLayout() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_class_table;
    }


    @BindView(R.id.switchTermNo)
    Spinner spinner;
    @BindView(R.id.courceDetail)
    GridView detailCource;

    private AbsGridAdapter gridAdapter;

    private List<String> termNoList;
    private ArrayAdapter<String> spinnerAdapter;

    private IEtClassTablePresenter iEtClassTablePresenter;

    @Override
    protected void initView() {

        iEtClassTablePresenter = new EtClassTablePImp(this);
        iEtClassTablePresenter.loadClassTable();
    }


    @Override
    public void showTerm(List<String> term, List<String> termNo) {

        termNoList = termNo;

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, term);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //获取选择的学期的课表数据
                String no=termNoList.get(position);
                iEtClassTablePresenter.getClassTable(no);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showClassTable(List<Course> courses, String[][] courseText) {

        gridAdapter = new AbsGridAdapter(this);
        gridAdapter.setContent(courseText, 5, 7);
        detailCource.setAdapter(gridAdapter);
    }
}
