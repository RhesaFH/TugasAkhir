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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Rhesa
 */
class Format {

    DecimalFormat df = new DecimalFormat("#.##");
    private String word1;
    private ArrayList<Words> KataTemp1List;
    private String word2;
    private ArrayList<Words> KataTemp2List;
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
    boolean stop = false, tfidf = false, sinonim = false, arti = false;

    public Format() {
        this.KataTemp1List = new ArrayList<>();
        this.KataTemp2List = new ArrayList<>();
        this.word1 = "";
        this.word2 = "";
        this.goldStandard = (float) 0.0;
        this.vectorScore = (float) 0.0;
    }

    public Format(String word1, String word2, float goldStandard) {
        this.KataTemp1List = new ArrayList<>();
        this.KataTemp2List = new ArrayList<>();
        this.word1 = word1;
        this.word2 = word2;
        goldStandard = goldStandard;
        this.goldStandard = goldStandard;
    }

    public void simpanDaftarKata1(String word) { //nyimpen nilai definisi kata 1
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
            for (Words KataTemp : KataTemp1List) {
                if (word.equals(KataTemp.getWord())) {
                    //System.out.println(word);
                    isInList = true;
                    KataTemp.increaseTotal();
                }
            }

            if (!isInList) {
                this.KataTemp1List.add(new Words(word));
            }
        }

    }

    public void simpanDaftarKata2(String word) { //nyimpen nilai definisi kata 2
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
            for (Words KataTemp : KataTemp2List) {
                if (word.equals(KataTemp.getWord())) {
                    //System.out.println(word);
                    isInList = true;
                    KataTemp.increaseTotal();
                }
            }
            if (!isInList) {
                this.KataTemp2List.add(new Words(word));
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
        return KataTemp1List;
    }

    public void setRelatedWord1List(ArrayList<Words> KataTemp1List) {
        this.KataTemp1List = KataTemp1List;
    }

    public String getWord2() {
        return word2;
    }

    public void setWord2(String word2) {
        this.word2 = word2;
    }

    public ArrayList<Words> getRelatedWord2List() {
        return KataTemp2List;
    }

    public void setRelatedWord2List(ArrayList<Words> KataTemp2List) {
        this.KataTemp2List = KataTemp2List;
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

    public void HitungNilaiVektor(Idf idf, boolean tfidf, boolean sinonim, boolean arti, boolean stop){ //hitung nilai vektor
        this.tfidf = tfidf;
        this.sinonim = sinonim;
        this.arti = arti;
        this.stop = stop;
        
        HashMap<String, Float> hashmap = new HashMap<String, Float>();
        if (sinonim == false && stop == true){   // ambil nilai idf yang telah disimpan di file txt
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
        } else if (sinonim == true && stop == true){
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
        } else if (sinonim == false && stop == false){
            try
            {
               BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("src/idfnonstop.txt")));
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
        } else if (sinonim == true && stop == false){
            try
            {
               BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("src/idfsinnonstop.txt")));
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
        for (Words dftKata1 : KataTemp1List) {
            temporaryWord1.add(dftKata1.getWord());
        }

        ArrayList<String> temporaryWord2 = new ArrayList<>();
        for (Words dftKata2 : KataTemp2List) {
            temporaryWord2.add(dftKata2.getWord());
        }

        temporaryWord1.removeAll(temporaryWord2);
        temporaryWord1.addAll(temporaryWord2);
        System.out.println("token yang ada : "+temporaryWord1);
        
        ArrayList<Words> VektorKataTemp = new ArrayList<>();
        for (String word : temporaryWord1) {
            VektorKataTemp.add(new Words(word, 0));
        }
        
        for (Words VKT : VektorKataTemp) {
            for (Words KataTemp1 : KataTemp1List) {
                if (VKT.getWord().equals(KataTemp1.getWord())) {
                    if (tfidf == true) {
                        for (Map.Entry<String, Float> ceck : nilaiIdf.entrySet()) {
                            String d = ceck.getKey();
                            float s = ceck.getValue();
                            if (KataTemp1.getWord().equals(d)) { //hitung tfidf token kata1
                                KataTemp1.setTotal((float) (1 + Math.log(KataTemp1.getTotal())));
                                System.out.println("token1 "+KataTemp1.getWord() + " TFIDF : " + KataTemp1.getTotal() + " * " + s);
                                KataTemp1.setTotal(KataTemp1.getTotal() * s);                              
                                VKT.setTotal(KataTemp1.getTotal());
                            }
                        }
                    }
                }
            }
        }

        for (Words VKT : VektorKataTemp) {
            boolean isSame = false;
            for (Words KataTemp2 : KataTemp2List) {
                if (VKT.getWord().equals(KataTemp2.getWord())) {
                    isSame = true;
                    if (tfidf == true) {
                        for (Map.Entry<String, Float> ceck : nilaiIdf.entrySet()) {
                            String d = ceck.getKey();
                            float s = ceck.getValue();
                            if (KataTemp2.getWord().equals(d)) { //hitung tfidf token kata2
                                KataTemp2.setTotal((float) (1 + Math.log(KataTemp2.getTotal())));
                                System.out.println("token2 "+KataTemp2.getWord() + " TFIDF : " + KataTemp2.getTotal() + " * " + s);
                                KataTemp2.setTotal(KataTemp2.getTotal() * s);
                            }
                        }

                        if (VKT.getTotal() != 0) { // jika ada token yang sama pada kata1 dan kata2
                        System.out.println("");
                        System.out.println("token yang sama : "+VKT.getWord() + " " + KataTemp2.getTotal() + " * " + VKT.getTotal());
                        System.out.println("");
                        VKT.setTotal(KataTemp2.getTotal() * VKT.getTotal());

                            break;
                        }
                    }

                }
            }
            if (!isSame) {
                VKT.setTotal(0);
            }
        }

        float totalVectorRelated = hitJumlTokenSama(VektorKataTemp);

        System.out.println("Total nilai TFIDF1.TFIDF2 : " + totalVectorRelated);
        System.out.println("----------------------------------------------------------");
 
        
        vectorWord1 = hitJumlTokenKata(word1, KataTemp1List);
        System.out.println("Panjang Vektor kata "+word1+": " + vectorWord1);
        System.out.println("----------------------------------------------------------");
        
        vectorWord2 = hitJumlTokenKata(word2, KataTemp2List);
        System.out.println("Panjang Vektor kata "+word2+": " + vectorWord2);
        System.out.println("----------------------------------------------------------");
        
        if (vectorWord1 == 0 || vectorWord2 == 0) {
            this.vectorScore = 0;
        } else {
            this.vectorScore = totalVectorRelated / (vectorWord1 * vectorWord2);
        }

        System.out.println();
    }

    private float hitJumlTokenSama(ArrayList<Words> relatedwordList) { // hitung jumlah token yang sama
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

    private float hitJumlTokenKata(String word, ArrayList<Words> relatedwordList) { //menghitung jumalh token
        float total = 0;
        int juml = 0;
        System.out.print("Token TFIDF " + word + " : ");
        for (Words relatedwordList1 : relatedwordList) {
                total += Math.pow(relatedwordList1.getTotal(), 2);
                System.out.print(relatedwordList1.getWord() + " : " + df.format(relatedwordList1.getTotal()) + ", ");
            
            juml++;
        }
        System.out.println();
        System.out.println("Jumlah Token yang tersedia = " + juml);
        return (float) Math.sqrt(total);

    }

}