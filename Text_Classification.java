
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Text_Classification {

    private double Counter_Of_All_The_Emails;//μετρητης ολων των e-mails στο training
    private double Counter_Of_All_The_Words;//μετρητης ολων των λεξεων στο training
    private double Counter_Of_Spam_Emails;//μετρητης ολων των spam  e-mails στο training
    private double Counter_Of_Legitimate_Emails;////μετρητης ολων των Legitimate  e-mails στο training
    private HashSet<String> Vocablulary = new HashSet<String>();//To λεξικο περιεχει ολες τις μοναδικες λεξεις του trainging set
    private ArrayList<String> Positions = new ArrayList<String>();//To Positions περιεχει ολες τις  λεξεις του test set και ταυτοχρονα εμπεριεχονται καιστο vocabulary
    private ArrayList<String> Text_Of_Spam = new ArrayList<String>();// To Text_Of_Spam εχει ολες τις λεξεις απο τα  spam τχτ αρχεια  1ο υποσυνολο του τραινινγ σετ  
    private ArrayList<String> Text_Of_Legitimate = new ArrayList<String>();// To Text_Of_Legitimate εχει ολες τις λεξεις απο τα Legitimate τχτ αρχεια 20 υποσυνολο του τραινινγ σετ
    private double n_Text_Of_Spam;//  n_Text_Of_Spam = Text_Of_Spam.size();
    private double n_Text_Of_Legitimate;// n_Text_Of_Legitimate = Text_Of_Legitimate.size();
    HashMap<String, Double> n_k_spam = new HashMap<String, Double>();//plithos emfanisis  kathe lexis τις κατηγοριας spam
    HashMap<String, Double> n_k_Legitimate = new HashMap<String, Double>();//plithos emfanisis  kathe lexis τις κατηγοριας Legitimate
    HashMap<String, Double> Pn_k__spam = new HashMap<String, Double>();//Πιθανοτητα καθε λεξης της κατηγοριας spam 
    HashMap<String, Double> Pn_k_Legitimate = new HashMap<String, Double>();//Πιθανοτητα καθε λεξης της κατηγοριας Legitimate 
    HashMap<String, String> Clasifiacation_test = new HashMap<String, String>();
    private double Spam_recall;
    private double Spam_precision;
//*****************************************************************************************************************************

    public void Calculate_n_k() {//plithos emfanisis  kathe lexis 
        System.out.println(n_Text_Of_Spam);
        System.out.println(n_Text_Of_Legitimate);
        System.out.println("********************************************************************");
        String word = null;
        String Str = null;
        double count = 0;
        Object[] array = Vocablulary.toArray();
        for (int i = 0; i < array.length; i++) {
            Str = (String) array[i];
            for (int j = 0; j < n_Text_Of_Spam; j++) {

                if (Str.equals(Text_Of_Spam.get(j))) {
                    count++;

                }

            }
            n_k_spam.put(Str, count);
            count = 0;

        }

        count = 0;
        Str = null;

        for (int i = 0; i < array.length; i++) {
            Str = (String) array[i];
            for (int k = 0; k < n_Text_Of_Legitimate; k++) {

                if (Str.equals(Text_Of_Legitimate.get(k))) {
                    count++;

                }

            }

            n_k_Legitimate.put(Str, count);
            count = 0;
        }

    }
//********************************************************************************************************************************************

    public void Create_Texts(File[] listOfFiles, int i) {//δημιουργουμε τα δυο υποσηνολα του τραινινγ σετ (σπαμ και λεγιτμ)
        int count = 0;
        int countofspm = 0;
        int countoflegit1 = 0;
        BufferedReader in;
        int countwords = 0;
        String names = null;

        for (File file : listOfFiles) {
            names = file.getName();

            if (names.indexOf("spm") != -1) {
                countofspm++;

                try {
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    Scanner scanFile = new Scanner(new FileReader("test1/pu1/part" + i + "/" + file.getName()));

                    String theWord;

                    while (scanFile.hasNext()) {

                        theWord = scanFile.next();

                        Text_Of_Spam.add(theWord);

                    }
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                } catch (FileNotFoundException e) {
                    System.out.println("File not found");
                } catch (IOException e) {
                    System.out.println("Read error");
                }

            } else {
                try {

                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    Scanner scanFile = new Scanner(new FileReader("test1/pu1/part" + i + "/" + file.getName()));

                    //  ArrayList<String> words = new ArrayList<String>();
                    String theWord;

                    while (scanFile.hasNext()) {

                        theWord = scanFile.next();

                        Text_Of_Legitimate.add(theWord);

                    }
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                } catch (FileNotFoundException e) {
                    System.out.println("File not found");
                } catch (IOException e) {
                    System.out.println("Read error");
                }

            }

        }

    }
//********************************************************************************************************************************************

    public void Create_Vocabulary(File[] listOfFiles, int i) {//dimiourgoyme to lexiko
        int countwords = 0;
        int size = 0;
        for (File file : listOfFiles) {
            try {
                Scanner scanFile = new Scanner(new FileReader("test1/pu1/part" + i + "/" + file.getName()));
                String theWord;
                while (scanFile.hasNext()) {
                    theWord = scanFile.next();
                    Vocablulary.add(theWord);
                    countwords++;
                    Counter_Of_All_The_Words++;
                }

            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } catch (IOException e) {
                System.out.println("Read error");
            }

        }
    }
//********************************************************************************************************************************************

    public void Count_All_The_Emails(File[] listOfFiles, int i) {//μετρηση ολων των emails ανα κατηγορια 
        //  System.out.println(" Count_All_The_Emails");
        int count = 0;//metritis olvn tvn emails(txt arxeia )
        int countofspm = 0;
        int countoflegit1 = 0;
        String names = null;
        for (File file : listOfFiles) {
            //System.out.println(file.getName());
            names = file.getName();

            if (names.indexOf("spm") != -1) {
                countofspm++;

            } else {
                countoflegit1++;

            }

            count++;
        }

        Counter_Of_All_The_Emails = Counter_Of_All_The_Emails + count;
        Counter_Of_Spam_Emails = Counter_Of_Spam_Emails + countofspm;
        Counter_Of_Legitimate_Emails = Counter_Of_Legitimate_Emails + countoflegit1;
    }
//********************************************************************************************************************************************

    public void Count_All_The_Emails_Before_clasification() {//metrame to plithow twn  spam, twn legit kai to sinoliko apo ola ta emais
        //System.out.println("General Information about the Emails");

        int count = 0;//metritis olvn tvn emails(txt arxeia )
        int countofspm = 0;
        int countoflegit1 = 0;
        String names = null;
        for (int i = 1; i < 11; i++) {
            File folder = new File("test1/pu1/part" + i);
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                names = file.getName();
                if (names.indexOf("spm") != -1) {
                    countofspm++;

                } else {
                    countoflegit1++;

                }

                count++;
            }
        }
        System.out.println("Count Of All The Emails : " + count);
        System.out.println("Count Of Spam Emails : " + countofspm);
        System.out.println("Counter Of Legitimate Emails : " + countoflegit1);

    }
//********************************************************************************************************************************************

    public void Propability_Of_Each_Word_For_Spam_Text() {
        double PropabilityNumber = 0;
        for (String name : n_k_spam.keySet()) {
            PropabilityNumber = (double) ((double) (n_k_spam.get(name) + 18500) / (double) (Counter_Of_Spam_Emails + Vocablulary.size()));
             Pn_k__spam.put(name, PropabilityNumber);
            //**************************************************************************************************************************************
            //PropabilityNumber = (double) ((double) (n_k_spam.get(name) + 350000) / (double) (Text_Of_Spam.size() + Vocablulary.size()));
            //Pn_k__spam.put(name, PropabilityNumber);
            //**************************************************************************************************************************************
//            PropabilityNumber = (double) ((double) (n_k_spam.get(name)) / (double) (Text_Of_Spam.size()));
//            if (PropabilityNumber == 0.0) {
//                PropabilityNumber = 1 / 100;
//                Pn_k__spam.put(name, PropabilityNumber);
//            } else {
//                Pn_k__spam.put(name, PropabilityNumber);
//            }
        }

    }
//********************************************************************************************************************************************

    public void Propability_Of_Each_Word_For_Legitimate_Text() {
        double PropabilityNumber = 0;
        for (String name : n_k_Legitimate.keySet()) {
            PropabilityNumber = (double) ((double) (n_k_Legitimate.get(name) + 18500) / (double) (Counter_Of_Legitimate_Emails + Vocablulary.size()));
             Pn_k_Legitimate.put(name, PropabilityNumber);
            //**************************************************************************************************************************************
            // PropabilityNumber = (double) ((double) (n_k_Legitimate.get(name) + 350000) / (double) (Text_Of_Legitimate.size() + Vocablulary.size()));
            // Pn_k_Legitimate.put(name, PropabilityNumber);
            //**************************************************************************************************************************************
//            PropabilityNumber = (double) ((double) (n_k_Legitimate.get(name)) / (double) (Text_Of_Legitimate.size()));
//            if (PropabilityNumber == 0.0) {
//                PropabilityNumber = 1 / 100;
//                Pn_k_Legitimate.put(name, PropabilityNumber);
//            } else {
//                Pn_k_Legitimate.put(name, PropabilityNumber);
//            }
        }

    }
//********************************************************************************************************************************************

    public void TenFoldCrossValidation() {
        Learn_naive_Bayes_text();
    }
//********************************************************************************************************************************************

    public void Learn_naive_Bayes_text() {
        int count = 10;
        System.out.println("General Info Before 10 fold Cross validation");
        Count_All_The_Emails_Before_clasification();
        System.out.println("********************************************************************");
        System.out.println(" Info After 10 fold Cross validation");

        System.out.println("Create_Vocabulary");
        for (int i = 1; i < 11; i++) {
            if (i != count) {
                File folder = new File("test1/pu1/part" + i);
                File[] listOfFiles = folder.listFiles();
                Count_All_The_Emails(listOfFiles, i);
                Create_Vocabulary(listOfFiles, i);
                Create_Texts(listOfFiles, i);
            }
        }
        Calculate_Total_Number_Of_Words_On_Texts();
        Print_Info();
        Calculate_n_k();
        Propability_Of_Each_Word_For_Legitimate_Text();
        Propability_Of_Each_Word_For_Spam_Text();
        System.out.println("Counter_Of_Spam_Emails-->" + Counter_Of_Spam_Emails);
        System.out.println("Text_Of_Legitimate.size()" + Text_Of_Legitimate.size());
        Classify_naine_Bayes_text(new File("test1/pu1/part" + count).listFiles(), count);
    }
//********************************************************************************************************************************************

    public void Classify_naine_Bayes_text(File[] listOfFiles, int i) {
        int countwords = 0;
        int size = 0;
        double SpamProp = 1.0;
        double apotelesma = 0.0;

        double LegitimateProp = 1.0;
        String name = null;
        String txtclassification = null;
        int correctly_identified_spam_messages = 0;
        int total_messages_identified_as_spam = 0;
        int Count_Of_All_Files = 0;
        int total_spam_messages = 0;
        int c = 0;
        for (File file : listOfFiles) {
            SpamProp = 1.0;
            LegitimateProp = 1.0;
            try {
                //*********************************************************************************************************
                name = file.getName();

                Scanner scanFile = new Scanner(new FileReader("test1/pu1/part" + i + "/" + file.getName()));//mpainw mesa sto arxeio.
                String theWord = null;
                //*********************************************************************************************************
                while (scanFile.hasNext()) {
                    theWord = scanFile.next();
                    if (Vocablulary.contains(theWord)) {
                        Positions.add(theWord);
                    }
                }
                //*********************************************************************************************************         
                for (int j = 0; j < Positions.size(); j++) {// ypologismos tis pithanotitas     ΠιP(a(i)/Legitimate)

                    for (String names : Pn_k_Legitimate.keySet()) {
                        if (Positions.get(j).equals(names)) {

                            LegitimateProp = (double) ((double) LegitimateProp * (double) Pn_k_Legitimate.get(names));///(double) Math.pow(10, -x1);

                        }

                    }

                }
                //*********************************************************************************************************
                for (int j = 0; j < Positions.size(); j++) {//ypologismos tis pithanotitas  P(a(i)/spam)

                    for (String names : Pn_k__spam.keySet()) {
                        if (Positions.get(j).equals(names)) {
                            SpamProp = (double) ((double) SpamProp * (double) Pn_k__spam.get(names));

                        }

                    }
                }
                //*********************************************************************************************************
                LegitimateProp = (double) (Propability_Of_Legitimate_Message() * LegitimateProp);
                SpamProp = (double) (Propability_Of_Spam_Message() * SpamProp);
                System.out.println("SpamProp" + SpamProp);
                System.out.println("LegitimateProp" + LegitimateProp);
                txtclassification = max(LegitimateProp, SpamProp);
                if (txtclassification.equals("spm")) {
                    total_messages_identified_as_spam++;
                }
                if (name.indexOf("spm") != -1) {
                    if (name.indexOf(txtclassification) != -1) {
                        correctly_identified_spam_messages++;

                    }
                    total_spam_messages++;
                }

                //*********************************************************************************************************
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } catch (IOException e) {
                System.out.println("Read error");
            }

            Count_Of_All_Files++;
            System.out.println("txtclassification" + txtclassification);

        }

        Spam_recall = (double) ((double) (correctly_identified_spam_messages) / (double) (total_spam_messages));
        Spam_precision = (double) ((double) (correctly_identified_spam_messages) / (double) (total_messages_identified_as_spam));
        System.out.println("Spam Recall = " + Spam_recall);
        System.out.println("Spam Precision = " + Spam_precision);
        System.out.println("correctly_identified_spam_messages-->" + correctly_identified_spam_messages);
        System.out.println("total_spam_messages-->" + total_spam_messages);
        System.out.println("total_messages_identified_as_spam--->" + total_messages_identified_as_spam);
        System.out.println("total Number of all Files(spam +legit)" + Count_Of_All_Files);

    }
//********************************************************************************************************************************************

    public String max(double m1, double m2) {
        String maximum = null;
        if (m1 > m2) {
            maximum = "legit";
        } else //if (m1 < m2) 
        {
            maximum = "spm";
        }
        return maximum;
    }
//********************************************************************************************************************************************

    public void Print_Info() {
        System.out.println("All the emails(spm+legit1) " + Counter_Of_All_The_Emails);
        System.out.println("number of spam emails  ---> " + Counter_Of_Spam_Emails);
        System.out.println("number of legitimate emails  ---> " + Counter_Of_Legitimate_Emails);
        System.out.println("**********************************************************************************");
        System.out.println(" number of words in Vocablulary-->" + Vocablulary.size());
        System.out.println("Number of all the words --->" + Counter_Of_All_The_Words);
        System.out.println("Text_Of_Legitimate_emails.size-->" + Text_Of_Legitimate.size());
        System.out.println("Text_Of_Spam_emails.size-->" + Text_Of_Spam.size());
        System.out.println("total(Text_Of_Legitimate_emails + Text_Of_Spam_emails=EXAMPLES)-->" + (Text_Of_Legitimate.size() + Text_Of_Spam.size()));
        System.out.println("****************************************************************************************************");
        System.out.println("Propability_Of_Legitimate_Message--->" + Propability_Of_Legitimate_Message());
        System.out.println("Propability_Of_Spam_Message" + Propability_Of_Spam_Message());

    }
//********************************************************************************************************************************************

    public double Propability_Of_Spam_Message() {

        return (double) Counter_Of_Spam_Emails / Counter_Of_All_The_Emails;
    }
//********************************************************************************************************************************************

    public double Propability_Of_Legitimate_Message() {

        return (double) Counter_Of_Legitimate_Emails / Counter_Of_All_The_Emails;
    }
//********************************************************************************************************************************************

    public void Calculate_Total_Number_Of_Words_On_Texts() {
        System.out.println("********************************************************************");
        n_Text_Of_Spam = Text_Of_Spam.size();
        n_Text_Of_Legitimate = Text_Of_Legitimate.size();
        double total = n_Text_Of_Spam + n_Text_Of_Legitimate;//plithos lexewn sinolika

    }
//********************************************************************************************************************************************

    public static void main(String[] args) {
        Text_Classification t1 = new Text_Classification();
        // t1.Learn_naive_Bayes_text();
        t1.TenFoldCrossValidation();
    }

}
