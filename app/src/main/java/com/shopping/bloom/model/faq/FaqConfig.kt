package com.shopping.bloom.model.faq


data class FaqConfig(
    val Order_Issues: List<faqModel>,
    val Delivery:List<faqModel>,
    val Return_Refund: List<faqModel>,
    val Payment_Promos: List<faqModel>,
    val Product_Stock: List<faqModel>,
    val Account: List<faqModel>

)
