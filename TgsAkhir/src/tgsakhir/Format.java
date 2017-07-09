/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tgsakhir;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static jdk.nashorn.internal.objects.ArrayBufferView.length;

/**
 *
 * @author Rhesa
 */
class Format {

    DecimalFormat df = new DecimalFormat("#.##");
    private String word1;
    private ArrayList<Words> KataTerkait1List;
    private String word2;
    private ArrayList<Words> KataTerkait2List;
    private float goldStandard;
    private float vectorScore;
    private float X;
    private float XX;
    private float Y;
    private float YY;
    private float XY;
    private float banyak;
    private float vectorWord1;
    private float vectorWord2;
    boolean tfidf = false, kemunculan = false, sinonim = false, arti = false;

    public Format() {
        this.KataTerkait1List = new ArrayList<>();
        this.KataTerkait2List = new ArrayList<>();
        this.word1 = "";
        this.word2 = "";
        this.goldStandard = (float) 0.0;
        this.vectorScore = (float) 0.0;
    }

    public Format(String word1, String word2, float goldStandard) {
        this.KataTerkait1List = new ArrayList<>();
        this.KataTerkait2List = new ArrayList<>();
        this.word1 = word1;
        this.word2 = word2;
        goldStandard = goldStandard;
        this.goldStandard = goldStandard;
    }

    public void addRelatedWordForWord1(String word) { //nyimpen nilai definisi kata 1
        boolean isInList = false;
        Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher m = p.matcher(word);
        String hasil = "";
        int i = 0;
        while (m.find(i)) {
            hasil = hasil + m.group() + "\n";
            i = m.start() + 1;
        }
        if (i != 0) {
            for (Words KataTerkait : KataTerkait1List) {
                if (word.equals(KataTerkait.getWord())) {
                    //System.out.println(word);
                    isInList = true;
                    KataTerkait.increaseTotal();
                }
            }

            if (!isInList) {
                this.KataTerkait1List.add(new Words(word));
            }
        }

    }
public void addRelatedWordForWord11(String word) { //nyimpen nilai definisi kata 1
        boolean isInList = false;
        Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher m = p.matcher(word);
        String hasil = "";
        int i = 0;
        while (m.find(i)) {
            hasil = hasil + m.group() + "\n";
            i = m.start() + 1;
        }
        if (i != 0) {
            for (Words KataTerkait : KataTerkait1List) {
                if (word.equals(KataTerkait.getWord())) {
                    //System.out.println(word);
                    isInList = true;
                    KataTerkait.setTotal((float) (KataTerkait.getTotal()+0.3));
                }
            }

            if (!isInList) {
                this.KataTerkait1List.add(new Words(word));
            }
        }

    }
    public void addRelatedWordForWord2(String word) { //nyimpen nilai definisi kata 2
        boolean isInList = false;
        Pattern p = Pattern.compile("^[a-zA-Z0-9.-]+$");
        Matcher m = p.matcher(word);
        String hasil = "";
        int i = 0;
        while (m.find(i)) {
            hasil = hasil + m.group() + "\n";
            i = m.start() + 1;
        }
        if (i != 0) {
            for (Words KataTerkait : KataTerkait2List) {
                if (word.equals(KataTerkait.getWord())) {
                    //System.out.println(word);
                    isInList = true;
                    KataTerkait.increaseTotal();
                }
            }
            if (!isInList) {
                this.KataTerkait2List.add(new Words(word));
            }
        }
    }
public void addRelatedWordForWord22(String word) { //nyimpen nilai definisi kata 2
        boolean isInList = false;
        Pattern p = Pattern.compile("^[a-zA-Z0-9.-]+$");
        Matcher m = p.matcher(word);
        String hasil = "";
        int i = 0;
        while (m.find(i)) {
            hasil = hasil + m.group() + "\n";
            i = m.start() + 1;
        }
        if (i != 0) {
            for (Words KataTerkait : KataTerkait2List) {
                if (word.equals(KataTerkait.getWord())) {
                    //System.out.println(word);
                    isInList = true;
                    KataTerkait.setTotal((float) (KataTerkait.getTotal()+0.3));
                }
            }
            if (!isInList) {
                this.KataTerkait2List.add(new Words(word));
            }
        }
    }
    public String getWord1() {
        return word1;
    }

    public void setWord1(String word1) {
        this.word1 = word1;
    }

    public ArrayList<Words> getRelatedWord1List() {
        return KataTerkait1List;
    }

    public void setRelatedWord1List(ArrayList<Words> KataTerkait1List) {
        this.KataTerkait1List = KataTerkait1List;
    }

    public String getWord2() {
        return word2;
    }

    public void setWord2(String word2) {
        this.word2 = word2;
    }

    public ArrayList<Words> getRelatedWord2List() {
        return KataTerkait2List;
    }

    public void setRelatedWord2List(ArrayList<Words> KataTerkait2List) {
        this.KataTerkait2List = KataTerkait2List;
    }

    public float getGoldStandard() {
        return goldStandard;
    }

    public void setGoldStandard(float goldStandard) {
        goldStandard = goldStandard;
        this.goldStandard = goldStandard;
    }

    public float getVectorScore() {
        return vectorScore;
    }

    public void setVectorScore(float vectorScore) {
        this.vectorScore = vectorScore;
    }

    public float getVectorWord1() {
        return vectorWord1;
    }

    public void setVectorWord1(float vectorWord1) {
        this.vectorWord1 = vectorWord1;
    }

    public float getVectorWord2() {
        return vectorWord2;
    }

    public void setVectorWord2(float vectorWord2) {
        this.vectorWord2 = vectorWord2;
    }

    public void show() {
        System.out.println("Word 1        = " + this.word1);
        System.out.println("Word 2        = " + this.word2);
        System.out.println("Gold Standard = " + this.goldStandard);
        System.out.println("Vector Score  = " + this.vectorScore);
        System.out.println("\n");
    }

    public void HitungNilaiVektor(Idf idf, boolean tfidf, boolean sinonim, boolean arti){ //hitung nilai vektor
        this.tfidf = tfidf;
        this.kemunculan = kemunculan;
        this.sinonim = sinonim;
        this.arti = arti;
        
        HashMap<String, Float> hashmap = new HashMap<String, Float>();
        if (sinonim == false){   // ambil nilai idf yang telah disimpan di file txt
            try
            {
               BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("src/idf.txt")));
               String l;
               while((l = br.readLine()) != null)
               {
                  String[] args = l.split("[,]", 2);
                  if(args.length != 2)continue;
                  String p = args[0].replaceAll(" ", "");
                  String b = args[1].replaceAll(" ", "");
                  float q = Float.parseFloat(b);
                  hashmap.put(p, q);
                  
               }
               br.close();
            }catch (IOException e){}            
        } else if (sinonim == true){
            try
            {
               BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("src/idfsinonim.txt")));
               String l;
               while((l = br.readLine()) != null)
               {
                  String[] args = l.split("[,]", 2);
                  if(args.length != 2)continue;
                  String p = args[0].replaceAll(" ", "");
                  String b = args[1].replaceAll(" ", "");
                  float q = Float.parseFloat(b);
                  hashmap.put(p, q);
                  
               }
               br.close();
            }catch (IOException e){}             
        }
        
        Map<String, Float> nilaiIdf = hashmap; // nilai idf = nilai pada file txt yg telah dibaca
        
        ArrayList<String> temporaryWord1 = new ArrayList<>();
        for (Words relword11 : KataTerkait1List) {
            temporaryWord1.add(relword11.getWord());
        }

        ArrayList<String> temporaryWord2 = new ArrayList<>();
        for (Words relword21 : KataTerkait2List) {
            temporaryWord2.add(relword21.getWord());
        }

        temporaryWord1.removeAll(temporaryWord2);
        temporaryWord1.addAll(temporaryWord2);
        System.out.println("token yang ada : "+temporaryWord1);
        
        ArrayList<Words> VektorKataTerkait = new ArrayList<>();
        for (String word : temporaryWord1) {
            VektorKataTerkait.add(new Words(word, 0));
        }
        
        for (Words VKT : VektorKataTerkait) {
            for (Words KataTerkait1 : KataTerkait1List) {
                if (VKT.getWord().equals(KataTerkait1.getWord())) {
                    if (tfidf == true) {
                        for (Map.Entry<String, Float> ceck : nilaiIdf.entrySet()) {
                            String d = ceck.getKey();
                            float s = ceck.getValue();
                            if (KataTerkait1.getWord().equals(d)) { //hitung tfidf token kata1
                                System.out.println("token1 "+KataTerkait1.getWord() + " TFIDF : " + KataTerkait1.getTotal() + " * " + s);
                                KataTerkait1.setTotal(KataTerkait1.getTotal() * s);                              
                                VKT.setTotal(KataTerkait1.getTotal());
                            }
                        }
                    }
                }
            }
        }

        for (Words VKT : VektorKataTerkait) {
            boolean isSame = false;
            for (Words KataTerkait2 : KataTerkait2List) {
                if (VKT.getWord().equals(KataTerkait2.getWord())) {
                    isSame = true;
                    if (tfidf == true) {
                        for (Map.Entry<String, Float> ceck : nilaiIdf.entrySet()) {
                            String d = ceck.getKey();
                            float s = ceck.getValue();
                            if (KataTerkait2.getWord().equals(d)) { //hitung tfidf token kata2
                                KataTerkait2.setTotal((float) (1 + Math.log(KataTerkait2.getTotal())));
                                System.out.println("token2 "+KataTerkait2.getWord() + " TFIDF : " + KataTerkait2.getTotal() + " * " + s);
                                KataTerkait2.setTotal(KataTerkait2.getTotal() * s);
                            }
                        }

                        if (VKT.getTotal() != 0) { // jika ada token yang sama pada kata1 dan kata2
                        System.out.println("");
                        System.out.println("token yang sama : "+VKT.getWord() + " " + KataTerkait2.getTotal() + " " + VKT.getTotal());
                        System.out.println("");
                        VKT.setTotal(KataTerkait2.getTotal() * VKT.getTotal());

                            break;
                        }
                    }

                }
            }
            if (!isSame) {
                VKT.setTotal(0);
            }
        }

        float totalVectorRelated = HitungTFIDFSama(VektorKataTerkait);

        System.out.println("Total nilai TFIDF1.TFIDF2 : " + totalVectorRelated);
        System.out.println("----------------------------------------------------------");
 
        
        vectorWord1 = HitungTokenKata(word1, KataTerkait1List);
        System.out.println("Panjang Vektor kata "+word1+": " + vectorWord1);
        System.out.println("----------------------------------------------------------");
        
        vectorWord2 = HitungTokenKata(word2, KataTerkait2List);
        System.out.println("Panjang Vektor kata "+word2+": " + vectorWord2);
        System.out.println("----------------------------------------------------------");
        
        if (vectorWord1 == 0 || vectorWord2 == 0) {
            this.vectorScore = 0;
        } else {
            this.vectorScore = totalVectorRelated / (vectorWord1 * vectorWord2);
        }

        System.out.println();
    }

    private float HitungTFIDFSama(ArrayList<Words> relatedwordList) { // hitung tfidf1.tfidf2
        float total = 0;
        int juml = 0;
        System.out.println("----------------------------------------------------------");
        System.out.print("Token TFIDF yang sama : ");
        for (Words relatedwordList1 : relatedwordList) {
            total += relatedwordList1.getTotal();
            if (relatedwordList1.getTotal() != 0) {
                juml++;
                System.out.print(relatedwordList1.getWord() + " = " + df.format(relatedwordList1.getTotal()) + ", ");
            }
        }
        System.out.println();
        System.out.println("Jumlah Token yang sama = " + juml);

        return total;
    }

    private float HitungTokenKata(String word, ArrayList<Words> relatedwordList) { //memunculkan token tfidf dari kata
        float total = 0;
        int juml = 0;
        System.out.print("Token TFIDF " + word + " : ");
        for (Words relatedwordList1 : relatedwordList) {
            if (kemunculan == true) {
                total += Math.pow(1, 2);
                System.out.print(relatedwordList1.getWord() + " : " + df.format(1) + ", ");
            } else {
                total += Math.pow(relatedwordList1.getTotal(), 2);
                System.out.print(relatedwordList1.getWord() + " : " + df.format(relatedwordList1.getTotal()) + ", ");
            }

            juml++;

        }

        System.out.println();
        System.out.println("Jumlah Token yang tersedia = " + juml);
        return (float) Math.sqrt(total);

    }

}