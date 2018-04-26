package com.enrsolidr.energyanalysis.services;

import com.enrsolidr.energyanalysis.entity.Payment;
import com.ibm.icu.text.RuleBasedNumberFormat;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ReceiptService {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private static final String inputFile = "/templates/recu.pdf";

    private static final String receiptMailObject = "Reçu fiscal Watt4All";

    private static final String receiptMailText = "Bonjour, voici votre reçu fiscal pour votre don à Watt4All. \nMerci pour tout !";

    @Value("${receipt.destinationFolder}")
    private String destinationFolder;

    @Value("${receipt.email.from}")
    private String emailFrom;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private EmailService emailService;

    private PDAcroForm acroForm;

    private PDDocument pdfDocument;

    public void sendReceipts(String year) throws IOException, ParseException {
        Map<Payment, String> paymentReceipts = getReceipts(year);
        Iterator<Map.Entry<Payment, String>> iterator = paymentReceipts.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<Payment, String> entry = iterator.next();
            Payment payment = entry.getKey();
            String receipt = entry.getValue();
            emailService.sendMessageWithAttachment(emailFrom, payment.getUser().getEmail(), receiptMailObject, receiptMailText, receipt);
        }
    }

    private Map<Payment, String> getReceipts(String year) throws IOException, ParseException {
        String fromDate = "01/01/" + year;
        String toDate = "31/12/" + year;
        List<Payment> payments = paymentService.getPaymentsByDate(dateFormat.parse(fromDate), dateFormat.parse(toDate));
        List<String> files = generatePdfs(payments);

        Iterator<Payment> paymentsIterator = payments.iterator();
        Iterator<String> fileIterator = files.iterator();
        return IntStream.range(0, payments.size()).boxed()
                .collect(Collectors.toMap(_i -> paymentsIterator.next(), _i -> fileIterator.next()));
    }

    private List<String> generatePdfs(List<Payment> payments) throws IOException {
        int i = 0;
        List<String> outputFiles = new ArrayList<String>();
        pdfDocument = PDDocument.load(this.getClass().getResourceAsStream(inputFile));
        PDDocumentCatalog docCatalog = pdfDocument.getDocumentCatalog();
        acroForm = docCatalog.getAcroForm();
        for (Payment payment : payments) {
            String outputFile = destinationFolder + "/receipt_" + i + ".pdf";
            Map<String, String> values = generateDocumentMapping(payment, i);
            replaceInPdf(values, outputFile);
            outputFiles.add(outputFile);
            i++;
        }
        pdfDocument.close();
        return outputFiles;
    }

    private Map<String, String> generateDocumentMapping(Payment payment, int index) {
        Map<String, String> mapping = new HashMap<String, String>();

        mapping.put("z1", new Integer(index).toString());

        //asso
        mapping.put("z2", "Watt4All");
        mapping.put("z3", "33");
        mapping.put("z4", "Bastion Saint André");
        mapping.put("z5", "59000");
        mapping.put("z5b", "LILLE");

        //asso type
        mapping.put("z12", "Oui");

        //the payer
        mapping.put("z29", payment.getUser().getLast_name());
        mapping.put("z30", payment.getUser().getNames());
        mapping.put("z31", payment.getUser().getAddress().getStreet());
        mapping.put("z32", payment.getUser().getAddress().getPostalCode());
        mapping.put("z33", payment.getUser().getAddress().getCity());

        //amount
        mapping.put("z34", new Double(payment.getAmount()/100).toString());
        RuleBasedNumberFormat ruleBasedNumberFormat = new RuleBasedNumberFormat(Locale.FRANCE, RuleBasedNumberFormat.SPELLOUT);
        mapping.put("z35", ruleBasedNumberFormat.format(new Double(payment.getAmount()/100)) + " euros");

        //date
        Date paymentDate = payment.getPaymentDate();
        String formattedDate = dateFormat.format(paymentDate);
        String day = formattedDate.substring(0, 2);
        String month = formattedDate.substring(3, 5);
        String year = formattedDate.substring(6, 10);
        mapping.put("z36", day);
        mapping.put("z37", month);
        mapping.put("z38", year);

        //gift type
        mapping.put("z39", "Oui");
        mapping.put("z40", "Oui");
        mapping.put("z44", "Oui");
        mapping.put("z46", "Oui");

        //today date
        formattedDate = dateFormat.format(new Date());
        day = formattedDate.substring(0, 2);
        month = formattedDate.substring(3, 5);
        year = formattedDate.substring(6, 10);
        mapping.put("z52", day);
        mapping.put("z53", month);
        mapping.put("z54", year);

        return mapping;
    }

    private void replaceInPdf(Map<String, String> values, String outputFile) throws IOException {
        // as there might not be an AcroForm entry a null check is necessary
        if (acroForm != null) {
            Iterator<Map.Entry<String, String>> iterator = values.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                PDField field = (PDField) acroForm.getField(entry.getKey());
                if (field != null) {
                    field.setValue(entry.getValue());
                }
            }
        }
        pdfDocument.save(outputFile);
    }
}