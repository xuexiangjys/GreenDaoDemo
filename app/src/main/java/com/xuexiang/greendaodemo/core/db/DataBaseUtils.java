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

package com.xuexiang.greendaodemo.core.db;


import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.xuexiang.greendaodemo.core.db.entity.DaoMaster;
import com.xuexiang.greendaodemo.core.db.entity.DaoSession;

/**
 * 数据库工具类
 */
public final class DataBaseUtils {

    private DataBaseUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static DaoSession sDaoSession;

    public static void init(Application application) {
        if (sDaoSession == null) {
            // 相当于得到数据库帮助对象，用于便捷获取db
            // 这里会自动执行upgrade的逻辑.backup all table→del all table→create all new table→restore data
            UpgradeHelper helper = new UpgradeHelper(application, "greendao.db", null);
            // 得到可写的数据库操作对象
            SQLiteDatabase db = helper.getWritableDatabase();
            // 获得Master实例,相当于给database包装工具
            DaoMaster daoMaster = new DaoMaster(db);
            // 获取类似于缓存管理器,提供各表的DAO类
            sDaoSession = daoMaster.newSession();
        }
    }

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }


}
