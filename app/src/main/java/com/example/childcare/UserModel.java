package com.example.childcare;

public class UserModel
{
    private String email,username, mobile, ParentId,photo;

    public UserModel() {
    }

    /**
     * to save childUsers parent id in database
     * @param email
     * @param username
     * @param mobile
     * @param ParentId
     * @param photo
     */
    public UserModel(String email, String username, String mobile, String ParentId, String photo) {
        this.email = email;
        this.username = username;
        this.mobile = mobile;
        this.ParentId = ParentId;
        this.photo = photo;
    }

    /**
     * to save parentUsers
     * @param email
     * @param username
     * @param mobile
     * @param photo
     */
    public UserModel(String email, String username, String mobile, String photo) {
        this.email = email;
        this.username = username;
        this.mobile = mobile;
        this.photo = photo;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String parentId) {
        this.ParentId = parentId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
