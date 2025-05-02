package com.example.buildatipcalculatorapp.util

fun calculateTotalTip(totalBill: Double, tipPertegance: Int): Double{
    return if(totalBill>1 &&
        totalBill.toString().isNotEmpty())
        (totalBill*tipPertegance) /100 else 0.0
}
fun calculateTotalPerPerson(totalBill: Double,
                            splitBy:Int,
                            tipPertegance: Int):Double{
    val bill = calculateTotalTip(totalBill=totalBill,tipPertegance=tipPertegance) + totalBill

    return  bill/splitBy
}