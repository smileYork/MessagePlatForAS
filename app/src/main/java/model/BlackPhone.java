package model;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-22 下午5:49:05
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class BlackPhone {

    private int id;
    private String phone;

    public BlackPhone(int id, String phone) {
	super();
	this.id = id;
	this.phone = phone;
    }

    public BlackPhone() {
	super();
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

}
