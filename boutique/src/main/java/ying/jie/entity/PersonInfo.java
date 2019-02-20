package ying.jie.entity;

/**
 * Created by Weibj on 2016/1/4.
 */
public class PersonInfo {
    private int photoImageId = 0;
    private int photoDesc = 0;
    private String mail;
    private float rating;
    private String tel;

    public PersonInfo(String mail, int photoDesc, int photoImageId, float rating, String tel) {
        this.mail = mail;
        this.photoDesc = photoDesc;
        this.photoImageId = photoImageId;
        this.rating = rating;
        this.tel = tel;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getPhotoDesc() {
        return photoDesc;
    }

    public void setPhotoDesc(int photoDesc) {
        this.photoDesc = photoDesc;
    }

    public int getPhotoImageId() {
        return photoImageId;
    }

    public void setPhotoImageId(int photoImageId) {
        this.photoImageId = photoImageId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

}
