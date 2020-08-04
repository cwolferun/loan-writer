package com.wolfe.kommerce.util

import java.nio.CharBuffer


fun main() {
    val things = "approval_date,approval_fiscal_year,as_of_date,bank_city,bank_name,bank_state,bank_street,bank_zip,borr_city,borr_name,borr_state,borr_street,borr_zip,business_type,charge_off_date,congressional_district,delivery_method,first_disbursement_date,franchise_code,franchise_name,gross_approval,gross_charge_off_amount,initialinterestrate,jobs_supported,loan_status,naics_code,naics_description,paid_in_full_date,program,project_county,project_state,revolver_status,sba_district_office,sbaguaranteedapproval,termin_months,subpgmdesc"
//    things.split(",").forEach {
//        println("val ${toGoodCase(it)}: String?,")
//    }

    things.split(",").indices.forEach { print("topRow.get($it), ") }


}
fun toGoodCase(word: String): String {
    val chars = word.toCharArray()
    val newChars = CharBuffer.allocate(50)
    var skipIt = false
    (chars.indices).forEach {
        if (!skipIt) {
            val c = chars[it]
            if (c != '_')
                newChars.put(c)
            else {
                newChars.put(chars[it + 1].toUpperCase())
                skipIt = !skipIt
            }
        } else skipIt = !skipIt
    }
    return String(newChars.array())
}