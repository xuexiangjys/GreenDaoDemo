/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.greendaodemo.fragment;

import android.view.View;
import android.widget.ListView;

import com.xuexiang.greendaodemo.R;
import com.xuexiang.greendaodemo.adapter.StudentAdapter;
import com.xuexiang.greendaodemo.core.BaseFragment;
import com.xuexiang.greendaodemo.core.db.DataBaseUtils;
import com.xuexiang.greendaodemo.core.db.entity.Student;
import com.xuexiang.greendaodemo.core.db.entity.StudentDao;
import com.xuexiang.greendaodemo.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 这个只是一个空壳Fragment，只是用于演示而已
 *
 * @author xuexiang
 * @since 2019-07-08 00:52
 */
@Page(name = "简单使用")
public class SimpleUseFragment extends BaseFragment {

    @BindView(R.id.lv_data)
    ListView mLvData;

    private StudentAdapter mStudentAdapter;

    private List<Student> mTempList;

    private StudentDao mStudentDao;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_simple_use;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        mStudentDao = DataBaseUtils.getDaoSession().getStudentDao();
        mStudentAdapter = new StudentAdapter(getContext(), null, mStudentDao);
        mLvData.setAdapter(mStudentAdapter);
    }

    /**
     * 初始化监听
     */
    @Override
    protected void initListeners() {
        mTempList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Student student = new Student();
            student.setUserName("xuexiang");
            student.setSex("男");
            student.setAge((int) (Math.random() * 100));
            mTempList.add(student);
        }
    }

    @OnClick({R.id.btn_add, R.id.btn_query, R.id.btn_update, R.id.btn_delete, R.id.btn_add_by_transaction, R.id.btn_delete_by_transaction})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                int age = (int) (Math.random() * 100);
                Student student = new Student();
                student.setUserName("xuexiang");
                student.setSex("男");
                student.setAge(age);
                student.setHappy(age % 2 == 0);
                mStudentDao.insert(student);
                break;
            case R.id.btn_query:
                mStudentAdapter.updateList(mStudentDao.loadAll());
                break;
            case R.id.btn_update:
                List<Student> students = mStudentDao.queryBuilder().where(StudentDao.Properties.UserName.eq("xuexiang")).build().list();
                if (students != null && !students.isEmpty()) {
                    for (int i = 0; i < students.size(); i++) {
                        students.get(i).setUserName("xxxx");
                    }
                }
                mStudentDao.updateInTx(students);
                break;
            case R.id.btn_delete:
                mStudentDao.deleteAll();
                mStudentAdapter.updateList(mStudentDao.loadAll());
                break;
            case R.id.btn_add_by_transaction:
                try {
                    DataBaseUtils.getDaoSession().callInTx(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            for (int i = 0; i < mTempList.size(); i++) {
                                mStudentDao.insert(mTempList.get(i));
                            }
                            return true;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    XToastUtils.error("事务执行失败！");
                }
                break;
            case R.id.btn_delete_by_transaction:
                try {
                    DataBaseUtils.getDaoSession().callInTx(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            for (int i = 0; i < mTempList.size(); i++) {
                                mStudentDao.delete(mTempList.get(i));
                            }
                            return true;
                        }
                    });
                    mStudentAdapter.updateList(mStudentDao.loadAll());
                } catch (Exception e) {
                    e.printStackTrace();
                    XToastUtils.error("事务执行失败！");
                }
                break;
        }
    }
}
