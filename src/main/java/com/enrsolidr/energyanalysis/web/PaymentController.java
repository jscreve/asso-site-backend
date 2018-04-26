package com.enrsolidr.energyanalysis.web;

import com.enrsolidr.energyanalysis.entity.Member;
import com.enrsolidr.energyanalysis.entity.Payment;
import com.enrsolidr.energyanalysis.exceptions.AlreadyPaidException;
import com.enrsolidr.energyanalysis.exceptions.UserAlreadyExistException;
import com.enrsolidr.energyanalysis.exceptions.UserNotFoundException;
import com.enrsolidr.energyanalysis.model.TransactionType;
import com.enrsolidr.energyanalysis.resources.MemberPaymentResource;
import com.enrsolidr.energyanalysis.resources.PaymentResource;
import com.enrsolidr.energyanalysis.resources.SimplePaymentResource;
import com.enrsolidr.energyanalysis.services.MemberService;
import com.enrsolidr.energyanalysis.services.PaymentService;
import com.enrsolidr.energyanalysis.services.ReceiptService;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private StripeClient stripeClient;

    @Autowired
    PaymentService paymentService;

    @Autowired
    MemberService memberService;

    @Autowired
    ReceiptService receiptService;

    @Autowired
    PaymentController(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");

    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    public Charge chargeCard(@RequestBody PaymentResource paymentResource) throws Exception {
        return makeTransactionFromResource(paymentResource);
    }

    @RequestMapping(value = "/charge/existingmember", method = RequestMethod.POST)
    public Charge chargeExistingMember(@RequestBody SimplePaymentResource paymentResource) throws Exception {
        //ADHESION
        //fetch member
        Optional<Member> member  = memberService.getMemberByEmail(paymentResource.getEmail());
        if(!member.isPresent()) {
            throw new UserNotFoundException();
        }
        if(memberService.hasMemberPaid(paymentResource.getEmail(), simpleDateFormat.format(new Date()))) {
            throw new AlreadyPaidException();
        }
        //try transaction
        Charge charge = makeTransactionFromSimpleResourceAndMember(paymentResource, member.get());

        //update member ship
        memberService.setMemberPaid(member.get().getUser().getEmail(), simpleDateFormat.format(new Date()));
        return charge;
    }

    @RequestMapping(value = "/charge/newmember", method = RequestMethod.POST)
    public Charge chargeNewMember(@RequestBody PaymentResource paymentResource) throws Exception {
        //fetch member
        if(memberService.getMemberByEmail(paymentResource.getUser().getEmail()).isPresent()) {
            throw new UserAlreadyExistException();
        }
        //try transaction
        Payment payment = new Payment();
        payment.setPaymentDate(new Date());
        payment.setTransactionType(TransactionType.ADHESION);
        payment.setToken(paymentResource.getToken());
        payment.setUser(paymentResource.getUser());
        payment.setAmount(paymentResource.getAmount());
        Charge charge = this.stripeClient.chargeCreditCard(payment.getToken(), payment.getAmount());
        paymentService.savePayment(payment);

        //create member
        Member member = new Member();
        member.setUser(paymentResource.getUser());
        member.getMemberPayments().add(simpleDateFormat.format(new Date()));
        memberService.addMember(member);

        return charge;
    }

    private Charge makeTransactionFromResource(PaymentResource paymentResource) throws Exception {
        //try transaction
        Payment payment = fromPaymentResource(paymentResource);
        Charge charge  = this.stripeClient.chargeCreditCard(payment.getToken(), payment.getAmount());
        paymentService.savePayment(payment);
        return charge;
    }

    private Charge makeTransactionFromSimpleResourceAndMember(SimplePaymentResource paymentResource, Member member) throws Exception {
        //try transaction
        Payment payment = fromSimplePaymentResourceAndMember(paymentResource, member);
        Charge charge  = this.stripeClient.chargeCreditCard(payment.getToken(), payment.getAmount());
        paymentService.savePayment(payment);
        return charge;
    }

    private Payment fromSimplePaymentResourceAndMember(SimplePaymentResource paymentResource, Member member) {
        Payment payment = new Payment();
        payment.setPaymentDate(new Date());
        payment.setAmount(paymentResource.getAmount());
        payment.setToken(paymentResource.getToken());
        payment.setUser(member.getUser());
        return payment;
    }

    private Payment fromPaymentResource(PaymentResource paymentResource) {
        Payment payment = new Payment();
        payment.setPaymentDate(new Date());
        payment.setAmount(paymentResource.getAmount());
        payment.setToken(paymentResource.getToken());
        payment.setUser(paymentResource.getUser());
        return payment;
    }

    @RequestMapping(value = "/receipt", method = RequestMethod.GET)
    public void generateReceipt(@RequestParam(value = "year") String year) throws Exception {
        receiptService.sendReceipts(year);
    }
}