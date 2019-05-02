/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package friendsbook;

import static friendsbook.FBDateAndTime.DateTime;
import java.util.Date;

/**
 *
 * @author ankcs
 */
public class FBUpdateAndPost {

    
    private String postID;
    private String acctID;
    private int type;
    private String content;
    private Date date;
    
    public FBUpdateAndPost(String postID, String acctID,int type, String content,Date date) {
        this.postID = postID;
        this.acctID = acctID;
        this.type = type;
        this.content = content;
        this.date=date;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getAcctID() {
        return acctID;
    }

    public void setAcctID(String acctID) {
        this.acctID = acctID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date content) {
        this.date = date;
    }
}
