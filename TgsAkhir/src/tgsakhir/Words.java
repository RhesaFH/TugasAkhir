/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tgsakhir;

/**
 *
 * @author Rhesa
 */
public class Words {

    private String word;
    private float total;

    public Words(String word) {
        this.word = word;
        total = 1;
    }

    public Words(String word, float total) {
        this.word = word;
        this.total = total;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void increaseTotal() {
        total++;
    }

    public void show() {
        System.out.println(word + " = " + total);
    }

}
