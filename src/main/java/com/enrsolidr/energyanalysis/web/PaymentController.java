package com.enrsolidr.energyanalysis.web;

import com.enrsolidr.energyanalysis.entity.Member;
import com.enrsolidr.energyanalysis.entity.Payment;
import com.enrsolidr.energyanalysis.exceptions.AlreadyPaidException;
import com.enrsolidr.energyanalysis.exceptions.UserAlreadyExistException;
import com.enrsolidr.energyanalysis.exceptions.UserNotFoundException;
import com.enrsolidr.energyanalysis.model.Linky;
import com.enrsolidr.energyanalysis.model.TransactionType;
import com.enrsolidr.energyanalysis.resources.OutputPaymentResource;
import com.enrsolidr.energyanalysis.resources.OutputPaymentResourceAssembler;
import com.enrsolidr.energyanalysis.resources.PaymentResource;
import com.enrsolidr.energyanalysis.resources.SimplePaymentResource;
import com.enrsolidr.energyanalysis.services.MemberService;
import com.enrsolidr.energyanalysis.services.PaymentService;
import com.enrsolidr.energyanalysis.services.ReceiptService;
import com.stripe.model.Charge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.enrsolidr.energyanalysis.util.SecurityConstants.MEMBER_ROLE;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private StripeClient stripeClient;

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

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

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");

    @RequestMapping(value = "/charge", method = RequestMethod.POST)
    public ResponseEntity<Charge> chargeCard(@RequestBody PaymentResource paymentResource) throws Exception {
        Charge charge = makeTransactionFromResource(paymentResource);
        return new ResponseEntity(charge, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/charge/member", method = RequestMethod.POST)
    public ResponseEntity<Charge> chargeExistingMember(@RequestBody SimplePaymentResource paymentResource) throws Exception {
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
        return new ResponseEntity(charge, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/charge/newmember", method = RequestMethod.POST)
    public ResponseEntity<Charge> chargeNewMember(@RequestBody PaymentResource paymentResource) throws Exception {
        //fetch member
        if (memberService.getMemberByEmail(paymentResource.getUser().getEmail()).isPresent() ||
                memberService.getMemberByUsername(paymentResource.getUsername()).isPresent()) {
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

        Member member = new Member();
        member.setUser(paymentResource.getUser());
        Linky linky = new Linky();
        linky.setActivated(false);
        member.setLinky(linky);
        member.setUsername(paymentResource.getUsername());
        member.setPassword(bCryptPasswordEncoder().encode(paymentResource.getPassword()));
        member.setAuthorities(Collections.singletonList(MEMBER_ROLE));
        member.getMemberPayments().add(simpleDateFormat.format(new Date()));
        memberService.addMember(member);

        return new ResponseEntity(charge, HttpStatus.CREATED);
    }

    private Charge makeTransactionFromResource(PaymentResource paymentResource) throws Exception {
        //try transaction
        Payment payment = fromPaymentResource(paymentResource);
        Charge charge  = this.stripeClient.chargeCreditCard(payment.getToken(), payment.getAmount());
        paymentService.savePayment(payment);
        return charge;
    }

    public Charge makeTransactionFromSimpleResourceAndMember(SimplePaymentResource paymentResource, Member member) throws Exception {
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
    public ResponseEntity<?> generateReceipt(@RequestParam(value = "year") String year) throws Exception {
        receiptService.sendReceipts(year);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/fetch", method = RequestMethod.GET)
    public ResponseEntity<List<OutputPaymentResource>> fetchAll() {
        logger.info("Get payments : {}");

        List<Payment> payments = paymentService.getAllPayments();
        Collections.reverse(payments);

        OutputPaymentResourceAssembler resourceAssembler = new OutputPaymentResourceAssembler(PaymentController.class, OutputPaymentResource.class);
        List<OutputPaymentResource> resources = resourceAssembler.toResources(payments);
        return new ResponseEntity<List<OutputPaymentResource>>(resources, HttpStatus.OK);
    }
}