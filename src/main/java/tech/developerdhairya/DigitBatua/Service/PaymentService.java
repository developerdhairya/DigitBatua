package tech.developerdhairya.DigitBatua.Service;

import com.razorpay.PaymentLink;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
public class PaymentService {

    @Value("${razorpay.key}")
    private String key;

    @Value("${razorpay.key}")
    private String secret;

    public String generatePaymentLink(int amount,String transactionId,String email) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(key, secret);
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount",amount);
        paymentLinkRequest.put("currency","INR");
        paymentLinkRequest.put("accept_partial",false);
        paymentLinkRequest.put("first_min_partial_amount",amount);
        paymentLinkRequest.put("expire_by",1691097057);
        paymentLinkRequest.put("reference_id",transactionId);
        paymentLinkRequest.put("description","Payment for transaction");
        JSONObject customer = new JSONObject();
        customer.put("name","null");
        customer.put("contact","null");
        customer.put("email",email);
        paymentLinkRequest.put("customer",customer);
        JSONObject notify = new JSONObject();
        notify.put("sms",false);
        notify.put("email",true);
        paymentLinkRequest.put("notify",notify);
        paymentLinkRequest.put("reminder_enable",true);
        JSONObject notes = new JSONObject();
        notes.put("null","null");
        paymentLinkRequest.put("notes",notes);
        paymentLinkRequest.put("callback_url","https://github.com/developerdhairya");
        paymentLinkRequest.put("callback_method","get");
        PaymentLink paymentLink = razorpay.paymentLink.create(paymentLinkRequest);
        return paymentLink.toString();
    }


}
