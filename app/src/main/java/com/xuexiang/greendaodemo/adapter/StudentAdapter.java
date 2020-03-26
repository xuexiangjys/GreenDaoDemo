/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.greendaodemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuexiang.greendaodemo.R;
import com.xuexiang.greendaodemo.core.db.entity.Student;
import com.xuexiang.greendaodemo.core.db.entity.StudentDao;

import java.util.List;

/**
 * <pre>
 *     desc   : 学生信息列表
 *     author : xuexiang
 *     time   : 2018/5/10 上午1:16
 * </pre>
 */
public class StudentAdapter extends BaseContentAdapter<Student> {

    private StudentDao mStudentDao;

    public StudentAdapter(Context context, List<Student> list, StudentDao dao) {
        super(context, list);
        mStudentDao = dao;
    }

    @Override
    public View getConvertView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_list_student_item, null);
            holder = new ViewHolder();
            holder.tvId = convertView.findViewById(R.id.tvId);
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.tvAge = convertView.findViewById(R.id.tvAge);
            holder.tvSex = convertView.findViewById(R.id.tvSex);
            holder.tvUpdate = convertView.findViewById(R.id.tvUpdate);
            holder.tvDelete = convertView.findViewById(R.id.tvDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Student student = getItem(position);
        holder.tvId.setText(String.valueOf(student.getId()));
        holder.tvName.setText(student.getUserName());
        holder.tvAge.setText(String.valueOf(student.getAge()));
        holder.tvSex.setText(student.getSex());
        holder.tvUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                update(position);
            }
        });
        holder.tvDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                delete(position);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView tvId, tvName, tvAge, tvSex, tvUpdate, tvDelete;
    }

    public void update(int position) {
        Student student = getItem(position);
        student.setUserName("xxxx");
        student.setAge(19);
        student.setSex("女");
        mStudentDao.update(student);
        updateList(mStudentDao.loadAll());
    }

    public void delete(int position) {
        Student student = getItem(position);
        mStudentDao.delete(student);
        updateList(mStudentDao.loadAll());
    }

}
