package main.com.project_p;

import android.util.Log;

/**
 * Created by ABHILASH on 3/28/2017.
 */

public class Message {
    String message;
    String phone;
    String id;
    String date;

    public Message() {

    }

    public Message(String message, String phone, String id, String date) {
        this.message = message;
        this.phone = phone;
        this.id = id;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public boolean equals(Object ob)
    {
        Message ob1 = (Message) ob;
        if (this.phone.equals(ob1.phone))
            return true;
        else
            return false;
    }
    public int hashCode() {
        int hash = 3;
        hash = 7 * hash + this.phone.hashCode();
        return hash;
    }
//    public String toString()
//    {
//        return id+" "+Fname+" "+Lname;
//    }
}



