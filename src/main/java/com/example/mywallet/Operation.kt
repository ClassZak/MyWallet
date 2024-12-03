package com.example.mywallet

import java.util.Date


class Operation(var type : OperationType,var balance: BankBalance, var value: Double, var date: Date, var description : String){
}