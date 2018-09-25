package com.profoundtechs.copticbookscollection;

/**
 * Created by HP on 3/3/2018.
 */

public class Content {
    int id;
    String chapter;
    String content;
    byte[] imaget;
    byte[] imageb;

    public Content(){
    }

    public Content(int id, String chapter, String content, byte[] imaget, byte[] imageb) {
        this.id = id;
        this.chapter = chapter;
        this.content = content;
        this.imaget = imaget;
        this.imageb = imageb;
    }

    public Content(String chapter, String content, byte[] imaget, byte[] imageb) {
        this.chapter = chapter;
        this.content = content;
        this.imaget = imaget;
        this.imageb = imageb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImaget() {
        return imaget;
    }

    public byte[] getImageb() {
        return imageb;
    }

    public void setImaget(byte[] imaget) {
        this.imaget = imaget;
    }

    public void setImageb(byte[] imageb) {
        this.imageb = imageb;
    }
}
