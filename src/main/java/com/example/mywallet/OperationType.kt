package com.example.mywallet

import androidx.room.*

@Entity(tableName = "OperationType")
data class OperationType(
    @PrimaryKey(autoGenerate = true) val id:Int=0,
    @ColumnInfo(name = "OperationType") var OperationType_name : String){
}