package model;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-24 下午5:24:17
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class ShowItem implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private String phone;
    private String content;
    private String time;
    private String uuid;

    public ShowItem() {
	super();
    }

    public ShowItem(int id, String phone, String content, String time,
	    String uuid) {
	super();
	this.id = id;
	this.phone = phone;
	this.content = content;
	this.time = time;
	this.uuid = uuid;
    }

    public String getUuid() {
	return uuid;
    }

    public void setUuid(String uuid) {
	this.uuid = uuid;
    }

    public String getTime() {
	return time;
    }

    public void setTime(String time) {
	this.time = time;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public String toString()

    {

	return

	"id = " + id + ";" +

	"phone = " + phone + ";" +

	"body = " + content + "time = " + time;

    }

}
