package prodigalwang.newbornassistant.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import prodigalwang.newbornassistant.bean.StuInfo;
import prodigalwang.newbornassistant.utils.LogUtil;

/**
 * Created by ProdigalWang on 2017/3/13.
 * 保存登录教务系统的数据库信息
 */

public class etLoginDbHelper extends SQLiteOpenHelper {

    private static final String ET_LOGIN_DB_NAME = "ET.db";
    private static final int VERSION = 1;

    public etLoginDbHelper(Context context) {
        //第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类
        super(context, ET_LOGIN_DB_NAME, null, VERSION);
    }

    private static final String CREATE_ET_LOGIN = "create table et_login (" +
            "id integer primary key autoincrement," +
            "stu_id text," +
            "stu_pwd text)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ET_LOGIN);
        LogUtil.d("et_login create success!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 保存学生的信息
     *
     * @param info 学号和密码
     */
    public void saveStuInfo(StuInfo info) {
        if (info != null) {

            getWritableDatabase().execSQL("insert into et_login(stu_id,stu_pwd) values(?,?)",
                    new Object[]{info.getStuid(), info.getStupwd()});

            LogUtil.d("save stu_info success!");
        }
    }

    /**
     * 查询学生的全部信息
     *
     * @return 全部信息
     */
    public StuInfo queryStuInfo() {
        StuInfo stuInfo = new StuInfo();
        String sql = "select * from et_login";
        Cursor cursor = null;
        try {
            cursor = getWritableDatabase().rawQuery(sql, null);
            cursor.moveToLast();//移动到最后也就是最新添加的数据
            stuInfo.setStuid(cursor.getString(1));
            stuInfo.setStupwd(cursor.getString(2));

            LogUtil.d(" query stu_info success!");

            return stuInfo;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * 是否存有当前学号信息
     *
     * @param stuId 学号
     * @return 有true
     */
    public boolean hasUser(String stuId) {
        Cursor cursor = null;
        try {
            cursor = getWritableDatabase().rawQuery("select * from et_login where stu_id=?",
                    new String[]{stuId});
            if (cursor != null && cursor.getCount() > 0)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }
}
