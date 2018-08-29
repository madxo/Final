package com.example.mohit.afinal;

public class Project {
    private String desc;
    private String image;
    private String title;



    private String username;

    public Project(){

    }
    public Project(String title, String desc, String image){
             this.title=title;
             this.desc=desc;
             this.image=image;
             this.username=username;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
