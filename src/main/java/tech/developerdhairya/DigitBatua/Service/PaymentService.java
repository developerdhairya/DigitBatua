package tech.developerdhairya.DigitBatua.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Value("${razorpay.key}")
    private String key;

    @Value("${razorpay.key}")
    private String secret;

    public void generatePaymentLink(){
        System.out.println(key+" "+secret);
    }


}
