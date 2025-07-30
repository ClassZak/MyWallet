package com.example.mywallet

import androidx.room.*
import java.io.Serializable


@Entity(
    tableName = "Operation",
    foreignKeys = [
        ForeignKey(
            entity = OperationType::class,
            parentColumns = ["id"],
            childColumns = ["OperationType_id"],
            onDelete = ForeignKey.CASCADE // Удаление операций при удалении типов операций
        ),
        ForeignKey(
            entity = BankBalanceType::class,
            parentColumns = ["id"],
            childColumns = ["BankBalance_id"],
            onDelete = ForeignKey.CASCADE // Удаление операций при удалении типов операций
        )
    ]
)
data class Operation(
    @PrimaryKey(autoGenerate = true) var id : Int=0,
    @ColumnInfo(index = true) var OperationType_id : Int,
    @ColumnInfo(name = "Operation_description") var Operation_description: String,
    @ColumnInfo(name = "Operation_amount") var Operation_amount: Double,
    @ColumnInfo(name = "Operation_date") var Operation_date: String,
    @ColumnInfo(index = true) var BankBalance_id : Int):Serializable{
}