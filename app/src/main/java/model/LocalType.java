package model;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-22 上午10:41:28
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class LocalType {

    private int photoId;

    private String message;

    private int number;

    public LocalType(int photoId, String message, int number) {
	super();
	this.photoId = photoId;
	this.message = message;
	this.number = number;
    }

    public int getPhotoId() {
	return photoId;
    }

    public void setPhotoId(int photoId) {
	this.photoId = photoId;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public int getNumber() {
	return number;
    }

    public void setNumber(int number) {
	this.number = number;
    }

}
