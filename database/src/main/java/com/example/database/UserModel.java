package com.example.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Administrator on 11/20/2016.
 */

@ModelContainer
@Table(database = DBFlowDatabase.class)
public class UserModel extends BaseModel {
    //自增ID
    @Column
    @PrimaryKey(autoincrement = true)
    public Long id;
    @Column
    public String name;
    @Column
    public int sex;
}