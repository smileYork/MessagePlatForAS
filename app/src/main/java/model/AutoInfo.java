package model;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-24 下午3:11:38
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class AutoInfo {

    private int id;
    private String autoInfo;
    private String key;
    private int type;

    
    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getAutoInfo() {
	return autoInfo;
    }

    public void setAutoInfo(String autoInfo) {
	this.autoInfo = autoInfo;
    }

    public String getKey() {
	return key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public int getType() {
	return type;
    }

    public void setType(int type) {
	this.type = type;
    }

}
