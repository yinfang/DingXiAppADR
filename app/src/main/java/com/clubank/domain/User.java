package com.clubank.domain;

import com.clubank.util.MyData;

public class User {
    private static User ourInstance;
    private String Token;
    private String userId;//会员id(手机号)
    private boolean isAuthorized;//会员是否已认证
    private String name;//姓名
    private String memNo;
    private String mobileNo;
    private String headIcon;
    private String sex;
    private String nickName;
    private MyData memberCards;//会籍卡
    private MyData memValueCards;//储值卡
    private String address;//默认地址的json字符串

    public synchronized static User getIn() {
        if (ourInstance == null) {
            ourInstance = new User();
        }
        return ourInstance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemNo() {
        return memNo;
    }

    public void setMemNo(String memNo) {
        this.memNo = memNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public MyData getMemberCards() {
        return memberCards;
    }

    public void setMemberCards(MyData memberCards) {
        this.memberCards = memberCards;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
