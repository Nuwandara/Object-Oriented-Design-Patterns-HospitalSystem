package main;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

class HospitalDatabase {

    private static HospitalDatabase hospitalDatabase;
    public ArrayList<MedicalRecord> medicaleRecordItem;

    private HospitalDatabase() {
        medicaleRecordItem = new ArrayList<>();
    }

    public static HospitalDatabase getInstance() {

        if (hospitalDatabase == null) {
            hospitalDatabase = new HospitalDatabase();
        }

        return hospitalDatabase;
    }

    public ArrayList<MedicalRecord> getItem() {
        return medicaleRecordItem;
    }

    public void addItem(MedicalRecord medicalRecord) {
        medicaleRecordItem.add(medicalRecord);
    }
}

class StratergyHandler {

    private PaymentStratergy paymentStratergy;
    private double amount;

    public void setPaymentStratergy(PaymentStratergy paymentStratergy) {
        this.paymentStratergy = paymentStratergy;
    }

    public void perform() {
        if (paymentStratergy != null) {
            paymentStratergy.pay(amount);
        } else {
            System.out.println("Payment stratergy not set");
        }
    }
}

abstract class MedicalRecord {

    private int id;
    private String name;
    private String status;

    public MedicalRecord(int id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public abstract void performAction();

    public final void MedicalProcessTemplate() {
        collectData();
        process();
        generateReport();
    }

    private void collectData() {
        System.out.println("Collecting data....");
    }

    private void generateReport() {
        System.out.println("Report is generating...");
    }

    protected abstract void process();

}

class MedicalRecordFactory {

    public MedicalRecord getMecMedicalRecordType(String type) {

        if (type == null) {
            return null;
        }

        if (type.equalsIgnoreCase("labReport")) {
            return new LabReport(1001, "Blood Test Report", "Relesecd");
        } else if (type.equalsIgnoreCase("prescription")) {
            return new Prescription(1002, "Prescription", "Released");
        } else if (type.equalsIgnoreCase("appointment")) {
            return new Appointment(100, "Appointment-morning time", "Booked");
        } else if (type.equalsIgnoreCase("invoice")) {
            return new Invoice(111, "Invoice", "Released", 1800);
        }

        return null;
    }
}

class PaymentStratergyHandler {

    private PaymentStratergy paymentStratergy;
    private MedicalRecord medicalRecord;

    public void setPaymentStratergy(PaymentStratergy paymentStratergy) {
        this.paymentStratergy = paymentStratergy;

    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public void perform(double amount) {
        if (paymentStratergy != null) {
            paymentStratergy.pay(amount);

        }
    }
}

class HospitalFacade {

    private LabReport labReport;
    private Prescription prescription;
    private Appointment appointment;
    private Invoice invoice;
    private PaymentStratergyHandler paymentStratergyHandler;

    public HospitalFacade(LabReport labReport, Prescription prescription, Appointment appointment,
            Invoice invoice) {
        this.labReport = labReport;
        this.prescription = prescription;
        this.appointment = appointment;
        this.invoice = invoice;
        this.paymentStratergyHandler = new PaymentStratergyHandler();
    }

    public void bookAppointment(double amount, String paymentType) {
        System.out.println("Appointment is scheduling...");
        appointment.performAction(); // <-- perform actual appointment logic
        makePayment(paymentType, amount);
        System.out.println("\n");

    }

    public void generateLabReport(double amount, String paymentType) {
        System.out.println("Lab report is generating...");
        labReport.performAction(); // <-- perform lab action
        makePayment(paymentType, amount);
        System.out.println("\n");
    }

    public void issuePrescription(double amount, String paymentType) {
        System.out.println("Prescription is issuing...");
        prescription.performAction(); // <-- perform prescription logic
        makePayment(paymentType, amount);
        System.out.println("\n");
    }

    public void makePayment(String paymnetType, double amount) {

        paymentStratergyHandler.setMedicalRecord(invoice);

        switch (paymnetType.toLowerCase()) {
            case "card":
                paymentStratergyHandler.setPaymentStratergy(new CardPayment());
                break;
            case "cash":
                paymentStratergyHandler.setPaymentStratergy(new CashPayment());
                break;
            case "insurance":
                paymentStratergyHandler.setPaymentStratergy(new InsurancePayment());
                break;
            default:
                throw new IllegalArgumentException("Unknown paymnet type ====" + paymnetType);
        }

        paymentStratergyHandler.perform(amount);

    }
}

class LabReport extends MedicalRecord {

    public LabReport(int id, String name, String status) {
        super(id, name, status);
    }

    @Override
    public void performAction() {
        System.out.println("Lab Report generated.");
    }

    @Override
    protected void process() {
        System.out.println("Lab report is processing.");
    }

}

class Prescription extends MedicalRecord {

    public Prescription(int id, String name, String status) {
        super(id, name, status);
    }

    private String ptName;
    private String medicine;

    public Prescription(int id, String name, String status, String ptName, String medicine) {
        super(id, name, status);
        this.ptName = ptName;
        this.medicine = medicine;
    }

    @Override
    public void performAction() {
        System.out.println("Prescription generated.");
    }

    @Override
    protected void process() {
        System.out.println("Prescription is processing.");
    }

    public String getptName() {
        return ptName;
    }

    public String getMedicine() {
        return medicine;
    }

}

class Appointment extends MedicalRecord {

    public Appointment(int id, String name, String status) {
        super(id, name, status);
    }

    @Override
    public void performAction() {
        System.out.println("Appointmnent sheduled & document printed.");
    }

    @Override
    protected void process() {
        System.out.println("Appointment is processing.");
    }

}

class Invoice extends MedicalRecord {

    private double amount;

    public Invoice(int id, String name, String status, double amount) {
        super(id, name, status);
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public void performAction() {
        System.out.println("Invoice  generated.");
        System.out.println("You have to pay " + getAmount());
    }

    @Override
    protected void process() {
        System.out.println("Invoice is processing.");
    }

}

interface Observer {    //like Subscriber

    void update(String event);
}

interface Subject {  //like publisher

    void attach(Observer o);

    void detach(Observer o);

    void notifyObserver(String event);

    void generateLabReport();

    void cancelledApt();
}

class HospitalSystem implements Subject {

    private List<Observer> observers = new ArrayList<>();

    @Override
    public void attach(Observer o) {
        observers.add(o);
    }

    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObserver(String event) {
        for (Observer observer : observers) {
            observer.update(event);
        }
    }

    @Override
    public void generateLabReport() {
        System.out.println("Lab report generated.");
        notifyObserver("LabReport");
    }

    @Override
    public void cancelledApt() {
        System.out.println("Appointment Cancelled");
        notifyObserver("AppointmentCancelled");
    }

}

class Doctor implements Observer {

    @Override
    public void update(String event) {

        if (event.equals("LabReport")) {
            System.out.println("Doctor :  Lab report is ready. Review it now.");
        } else if (event.equals("AppointmentCancelled")) {
            System.out.println("Doctor: Appointment has been cancelled. Adjust your schedule.");

        }

    }
}

class Patient implements Observer {

    private String name;

    public Patient(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void update(String event) {

        if (event.equals("LabReport")) {
            System.out.println("Patient name : " + getName() + " Your Lab report is ready.");
        }

    }

}

class PharmacyAdapter {

    private ExternalPharmacyAPI externalPharmacyAPI;

    public PharmacyAdapter(ExternalPharmacyAPI externalPharmacyAPI) {
        this.externalPharmacyAPI = externalPharmacyAPI;
    }

    public void sendPrescription(Prescription prescription) {
        JSONObject json = new JSONObject();
        json.put("patientName", prescription.getptName());
        json.put("medicine", prescription.getMedicine());

        externalPharmacyAPI.sendJSON(json.toString());
    }

}

class ExternalPharmacyAPI {

    public void sendJSON(String jsonData) {
        System.out.println("Sending prescription to extrenal pharmacy");
        System.out.println("JSON Data " + jsonData);
        System.out.println("Prescription successfully sent!...");
    }
}

class Receptionist implements Observer {

    @Override
    public void update(String event) {
        if (event.equals("AppointmentCancelled")) {
            System.out.println("Receptionist: Appointment cancelled. Update records accordingly.");
        }
    }

}

public class Main {

    public static void main(String[] args) {

        HospitalDatabase hospitalDatabase = HospitalDatabase.getInstance();

        MedicalRecordFactory medicalRecordFactory = new MedicalRecordFactory();

        LabReport labReport = (LabReport) medicalRecordFactory.getMecMedicalRecordType("labreport");
        Prescription prescription = (Prescription) medicalRecordFactory.getMecMedicalRecordType("prescription");
        Appointment appointment = (Appointment) medicalRecordFactory.getMecMedicalRecordType("appointment");
        Invoice invoice = (Invoice) medicalRecordFactory.getMecMedicalRecordType("invoice");

        hospitalDatabase.addItem(labReport);
        hospitalDatabase.addItem(prescription);
        hospitalDatabase.addItem(appointment);
        hospitalDatabase.addItem(invoice);

        System.out.println("--------------- Report action----------------\n");

        for (MedicalRecord mr : hospitalDatabase.getItem()) {

            mr.MedicalProcessTemplate();
            mr.performAction();
            System.out.println("\n-------------------------------------------");
        }

        System.out.println("\n--------------- Processing Actions----------------\n");

        HospitalFacade hospitalFacade = new HospitalFacade(labReport, prescription, appointment, invoice);

        hospitalFacade.bookAppointment(1700, "Cash");

        hospitalFacade.generateLabReport(2000, "Card");

        hospitalFacade.issuePrescription(2500, "Insurance");

//        hospitalFacade.makePayment("cash", 1700);
//        hospitalFacade.makePayment("card", 2000);
//        hospitalFacade.makePayment("insurance", 2500);
//        System.out.println("\n");

        HospitalSystem hospitalSystem = new HospitalSystem();

        Observer doctor = new Doctor();
        Observer patient = new Patient("Nuwandara Abeykoon");
        Observer receptionist = new Receptionist();

        hospitalSystem.attach(doctor);
        hospitalSystem.attach(patient);
        hospitalSystem.attach(receptionist);

        System.out.println("-----------YOUR REPORTS SECTION------------");
        hospitalSystem.generateLabReport();
        System.out.println("\n---------------------CANCELLING APPOINTMENT--------------");
        hospitalSystem.cancelledApt();

        System.out.println("\n-------------------------SENDING PRESCRIPTION TO OUTDOOR PHARMACY------------");
        Prescription prescription1 = new Prescription(1001, "Prescription for ", "Active", "Nuwandara Abeykoon", "Metphomine");

        ExternalPharmacyAPI externalAPI = new ExternalPharmacyAPI();
        PharmacyAdapter adapter = new PharmacyAdapter(externalAPI);

        adapter.sendPrescription(prescription1);
    }

}
