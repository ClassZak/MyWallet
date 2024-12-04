package com.example.mywallet

import androidx.room.*

@Entity(tableName = "OperationType")
data class OperationType(
    @PrimaryKey(autoGenerate = true) val OperationType_id:Int,
    @ColumnInfo(name = "OperationType") var OperationType_name : String){
}