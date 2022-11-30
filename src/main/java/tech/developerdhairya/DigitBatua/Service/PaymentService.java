package tech.developerdhairya.DigitBatua.Service;

import com.razorpay.PaymentLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import tech.developerdhairya.DigitBatua.Entity.AppUser;
import tech.developerdhairya.DigitBatua.Util.AuthenticationUtil;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Value("${razorpay.key}")
    private String key;

    @Value("${razorpay.secret}")
    private String secret;

    @Autowired
    private AuthenticationUtil authenticationUtil;

    public PaymentLink generatePaymentLink(AppUser appUser, int amount, String ref_id) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(key, secret);
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount",amount);
        paymentLinkRequest.put("currency","INR");
        paymentLinkRequest.put("accept_partial",false);
        paymentLinkRequest.put("first_min_partial_amount",amount);
        paymentLinkRequest.put("expire_by",authenticationUtil.calculateExpirationTime(24*60*150).toInstant().getEpochSecond());
        paymentLinkRequest.put("reference_id",ref_id);
        paymentLinkRequest.put("description","Payment for transaction");
        JSONObject customer = new JSONObject();
        customer.put("name",appUser.getFirstName()+" "+appUser.getLastName());
        customer.put("contact",appUser.getMobileNumber());
        customer.put("email",appUser.getEmailId());
        paymentLinkRequest.put("customer",customer);
        JSONObject notify = new JSONObject();
        notify.put("sms",true);
        notify.put("email",true);
        paymentLinkRequest.put("notify",notify);
        paymentLinkRequest.put("reminder_enable",true);
        JSONObject notes = new JSONObject();
//        notes.put("null","null");
        paymentLinkRequest.put("notes",notes);
        paymentLinkRequest.put("callback_url","https://aws.developerdhairya.tech/DigitBatua/wallet/funding/verify?fundingId="+ref_id);
        paymentLinkRequest.put("callback_method","get");
        return razorpay.paymentLink.create(paymentLinkRequest);
    }

    public boolean verifyPayment(String id,Integer amount) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(key, secret);
        PaymentLink paymentLink=razorpay.paymentLink.fetch(id);
        Integer amountPaid=paymentLink.get("amount_paid");
        System.out.println(amountPaid+" "+ (amountPaid.equals(amount)));
        return amountPaid.equals(amount);
    }


}
