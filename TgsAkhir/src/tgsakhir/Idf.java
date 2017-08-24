/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tgsakhir;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;;
import org.xml.sax.SAXException;
/**
 *
 * @author Rhesa
 */
public class Idf {

    boolean sinonim = false, arti = false, tfidf = false, stop = false;
    final int ALL_DATA = -1;
    final int NUM_DATA_1 = 1;
    final int NUM_DATA_2 = 2;
    String sentence1, sentence2;
    String stopword = "src/stopwords.txt";
    String stopwords = "src/stop.txt";
    ArrayList<String> komp1 = new ArrayList();
    ArrayList<String> komp2 = new ArrayList();
    Map<String, Float> jumlah = new HashMap<>();
    int nilai;
    Parser ps = new Parser();

    public Idf(boolean sinonim, boolean arti, boolean tfidf, boolean stop) throws ParserConfigurationException, IOException, MalformedURLException, SAXException, InterruptedException{
        this.sinonim = sinonim;
        this.arti = arti;
        this.tfidf = tfidf;
        this.stop = stop;

        if (stop == true){
        ps.stopWord(ps.hitungKata(stopword));
        } else if (stop == false) {
        ps.stopWords(ps.hitungKata(stopwords));
                };

        ps.load(ALL_DATA);

        for (Format data : ps.dataset) {
            setArrayIdf(data, NUM_DATA_1, tfidf);
            setArrayIdf(data, NUM_DATA_2, tfidf);
        }
        hitung(jumlah, nilai);//hitung nilai idf
        akhir(jumlah); //check idf
        DecimalFormat df = new DecimalFormat("#.##");
        
        //simpan nilai idf kedalam file txt
        if (tfidf == true) {
            if (sinonim == false && stop == true){
                try
                {
                   BufferedWriter bw = new BufferedWriter(new FileWriter("src/idf.txt"));
                   for(String p:jumlah.keySet())
                   {
                      bw.write(p + "," + jumlah.get(p));
                      bw.newLine();
                   }
                   bw.flush();
                   bw.close();
                }catch (IOException e){}
            } else if (sinonim == true && stop == true){
                try
                {
                   BufferedWriter bw = new BufferedWriter(new FileWriter("src/idfsinonim.txt"));
                   for(String p:jumlah.keySet())
                   {
                      bw.write(p + "," + jumlah.get(p));
                      bw.newLine();
                   }
                   bw.flush();
                   bw.close();
                }catch (IOException e){}                
            } else if (sinonim == false && stop == false){
                try
                {
                   BufferedWriter bw = new BufferedWriter(new FileWriter("src/idfnonstop.txt"));
                   for(String p:jumlah.keySet())
                   {
                      bw.write(p + "," + jumlah.get(p));
                      bw.newLine();
                   }
                   bw.flush();
                   bw.close();
                }catch (IOException e){}
            } else if (sinonim == true && stop == false){
                try
                {
                   BufferedWriter bw = new BufferedWriter(new FileWriter("src/idfsinnonstop.txt"));
                   for(String p:jumlah.keySet())
                   {
                      bw.write(p + "," + jumlah.get(p));
                      bw.newLine();
                   }
                   bw.flush();
                   bw.close();
                }catch (IOException e){}                
            }
            System.out.println(jumlah);
        }
    }

    public void setArrayIdf(Format data, int NUM_DATA,boolean tfidf) throws ParserConfigurationException, IOException, MalformedURLException, SAXException, InterruptedException{ // array untuk token idf
        if (NUM_DATA == 1) {
            nilai++;
            String DefinisiData = "";
            if (sinonim == true) {
                String synonym = ps.getSinonim(data.getWord1());
                DefinisiData = DefinisiData + synonym + " ";
            }
            if (arti == true) {
                String arti = ps.getDefinisi(data.getWord1());
                DefinisiData = DefinisiData + arti + " ";
            }

            ArrayList<String> DK = ps.kataNorm(DefinisiData);
            for (int i = 0; i < DK.size(); i++) {
                if (!komp1.contains(DK.get(i))) {
                    komp1.add(DK.get(i));

                    if (i == 0) {
                        sentence1 = DK.get(i);
                    } else {
                        sentence1 = sentence1 + " " + DK.get(i);
                    }                    
                }
            }
            for (int i = 0; i < komp1.size(); i++) {
                if (jumlah.containsKey(komp1.get(i))) {
                    jumlah.put(komp1.get(i), jumlah.get(komp1.get(i)) + 1);
                } else {
                    jumlah.put(komp1.get(i), (float) 1);
                }
            }
            komp1.clear();
            if (tfidf == true) {
                System.out.println(jumlah);
            }
        }
 
        if (NUM_DATA == 2) {
            nilai++;
            String DefinisiData = "";
            if (sinonim == true) {
                String synonym = ps.getSinonim(data.getWord2());
                DefinisiData = DefinisiData + synonym + " ";
            }
            if (arti == true) {
                String arti = ps.getDefinisi(data.getWord2());
                DefinisiData = DefinisiData + arti + " ";
            }
            
            ArrayList<String> DK = ps.kataNorm(DefinisiData);
            for (int i = 0; i < DK.size(); i++) {
                if (!komp2.contains(DK.get(i))) {
                    komp2.add(DK.get(i));
                    if (i == 0) {
                        sentence2 = DK.get(i);
                    } else {
                        sentence2 = sentence2 + " " + DK.get(i);
                    }
                }
            }
            for (int i = 0; i < komp2.size(); i++) {
                if (jumlah.containsKey(komp2.get(i))) {
                    jumlah.put(komp2.get(i), jumlah.get(komp2.get(i)) + 1);
                } else {
                    jumlah.put(komp2.get(i), (float) 1);
                }
            }
            komp2.clear();
            if (tfidf == true) {
               System.out.println(jumlah);
            }
        }
    }


    public void hitung(Map<String, Float> coun, int nilai) {
        for (Map.Entry<String, Float> cek : coun.entrySet()) {
            String d = cek.getKey();
            float s = cek.getValue();
            float idf = (float) Math.log10(nilai / s);
            coun.put(d, idf);
        }

    }

    public void akhir(Map<String, Float> cecking) {
    }
}
