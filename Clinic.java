package Clinic;

import java.util.Scanner;

//Base class for all people in the clinic
abstract class Person {
 protected String firstName;
 protected String lastName;
 protected String address;

 public Person(String firstName, String lastName, String address) {
     this.firstName = firstName;
     this.lastName = lastName;
     this.address = address;
 }

 public String getFirstName() { 
	 return firstName; }
 public String getLastName() { 
	 return lastName; }
 public String getAddress() { 
	 return address; }

 public void setFirstName(String firstName) { 
	 this.firstName = firstName; }
 public void setLastName(String lastName) { 
	 this.lastName = lastName; }
 public void setAddress(String address) { 
	 this.address = address; }

 public String toString() {
     return firstName + " " + lastName + ", Address: " + address;
 }
}

//Abstract Staff class
abstract class Staff extends Person {
 public Staff(String firstName, String lastName, String address) {
     super(firstName, lastName, address);
 }
 public abstract double calculatePaycheck();
}

//Doctor class
class Doctor extends Staff {
 private String specialty;
 private double officeVisitFee;
 private int numVisitedPatients;
 private boolean gen = true;

 public Doctor(String firstName, String lastName, String address, String specialty, double officeVisitFee) {
     super(firstName, lastName, address);
     this.specialty = specialty;
     this.officeVisitFee = officeVisitFee;
 }

 public void visit() { numVisitedPatients++; }

 public double calculatePaycheck() {
     return numVisitedPatients * officeVisitFee;
 }

 public String getSpecialty() { return specialty; }
 public double getOfficeVisitFee() { return officeVisitFee; }
 public int getNumVisitedPatients() { return numVisitedPatients; }

 public String toString() {
     return "=============================" +
            "\nDoctor's Information:" +
            "\nName: " + firstName + " " + lastName +
            "\nSpecialty: " + specialty +
            "\nOffice visit fee: $" + String.format("%.2f", officeVisitFee) +
            "\nNumber of visits: " + numVisitedPatients +
            "\nPaid: $" + String.format("%.2f", calculatePaycheck()) +
            "\n=============================\n";
 }
}

//Employee class
class Employee extends Staff {
 private int monthlyHours;
 private double salary;

 public Employee(String firstName, String lastName, String address, double salary, int hours) {
     super(firstName, lastName, address);
     this.salary = salary;
     this.monthlyHours = hours;
 }

 public double calculatePaycheck() {
     if (monthlyHours <= 165) return salary;
     double overtime = (monthlyHours - 165) * (salary / 165) * 1.5;
     return salary + overtime;
 }

 public String toString() {
     return "=============================" +
            "\nEmployee's Information:" +
            "\nName: " + firstName + " " + lastName +
            "\nSalary Rate: $" + String.format("%.2f", salary) +
            "\nHours: " + monthlyHours +
            "\nPaid: $" + String.format("%.2f", calculatePaycheck()) +
            "\n=============================\n";
 }
}

//Patient class
class Patient extends Person {
 private String pcpLastName;
 private String lastVisited;

 public Patient(String firstName, String lastName, String address, String pcpLastName) {
     super(firstName, lastName, address);
     this.pcpLastName = pcpLastName;
 }

 public void visit(String doctorLastName) {
     this.lastVisited = doctorLastName;
 }

 public String getPCP() { return pcpLastName; }
 public String getLastVisited() { return lastVisited; }

 public String toString() {
     return super.toString() + ", PCP: Dr. " + pcpLastName + ", Last Visited: Dr. " + (lastVisited != null ? lastVisited : "None");
 }
}

//Main class
public class Clinic {
 private static Scanner scanner = new Scanner(System.in);
 private static Staff[] staff = new Staff[10];
 private static Patient[] patients = new Patient[100];
 private static int staffCount = 0, patientCount = 0;

 public static void main(String[] args) {
     createStaff();
     handleVisits();
     printPaychecks();
 }

 private static void createStaff() {
     staff[staffCount++] = new Doctor("Michael", "Willis", "4601 Brightwater Ct", "PCP", 100);
     staff[staffCount++] = new Doctor("John", "Blake", "4603 Brightwater Ct", "Dermatologist", 150);
     staff[staffCount++] = new Doctor("Sara", "Frost", "35 Straw Hat Rd", "Pediatric", 150);
     staff[staffCount++] = new Employee("John", "Jones", "53 Straw Hat Rd", 2000, 170);
     staff[staffCount++] = new Employee("James", "Harrison", "10 Chatfield Ct", 1200, 160);
 }

 private static void handleVisits() {
     boolean more = true;
     while (more) {
         System.out.print("Is this a new patient? ");
         String isNew = scanner.nextLine().trim();
         Patient p;
         if (isNew.equalsIgnoreCase("yes")) {
             System.out.print("Patient's first name: ");
             String first = scanner.nextLine();
             System.out.print("Patient's last name: ");
             String last = scanner.nextLine();
             System.out.print("Patient's address: ");
             String addr = scanner.nextLine();
             System.out.print("Patient's primary care doctor: ");
             String pcp = scanner.nextLine();
             p = new Patient(first, last, addr, pcp);
             patients[patientCount++] = p;
         } else {
             System.out.print("Patient's last name: ");
             String last = scanner.nextLine();
             p = findPatient(last);
             if (p == null) {
                 System.out.println("Patient not found.");
                 continue;
             }
         }
         System.out.print("Doctor's name: ");
         String docName = scanner.nextLine();
         Doctor doc = findDoctor(docName);
         if (doc == null) {
             System.out.println("Doctor not found.");
             continue;
         }
         p.visit(docName);
         doc.visit();
         if (!docName.equalsIgnoreCase(p.getPCP())) {
             Doctor pcpDoc = findDoctor(p.getPCP());
             if (pcpDoc != null) pcpDoc.visit();
         }
         System.out.print("More patient? ");
         more = scanner.nextLine().trim().equalsIgnoreCase("yes");
     }
 }

 private static Doctor findDoctor(String lastName) {
     for (int i = 0; i < staffCount; i++) {
         if (staff[i] instanceof Doctor && staff[i].getLastName().equalsIgnoreCase(lastName)) {
             return (Doctor) staff[i];
         }
     }
     return null;
 }

 private static Patient findPatient(String lastName) {
     for (int i = 0; i < patientCount; i++) {
         if (patients[i].getLastName().equalsIgnoreCase(lastName)) {
             return patients[i];
         }
     }
     return null;
 }

 private static void printPaychecks() {
     System.out.println("\n========== FINAL OUTPUT ==========");
     for (int i = 0; i < staffCount; i++) {
         System.out.println(staff[i]);
     }
 }
}
