package model;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-29 上午9:33:50
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class Send {

    private int id;
    private String comFrom;
    private String returnBody;
    private String recvDate;
    private long lastSendTime;
    private int sendCount;

    public long getLastSendTime() {
	return lastSendTime;
    }

    public void setLastSendTime(long lastSendTime) {
	this.lastSendTime = lastSendTime;
    }

    public int getSendCount() {
	return sendCount;
    }

    public void setSendCount(int sendCount) {
	this.sendCount = sendCount;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getComFrom() {
	return comFrom;
    }

    public void setComFrom(String comFrom) {
	this.comFrom = comFrom;
    }

    public String getReturnBody() {
	return returnBody;
    }

    public void setReturnBody(String returnBody) {
	this.returnBody = returnBody;
    }

    public String getRecvDate() {
	return recvDate;
    }

    public void setRecvDate(String recvDate) {
	this.recvDate = recvDate;
    }

    @Override
    public String toString() {
	return "Send [id=" + id + ", comFrom=" + comFrom + ", returnBody="
		+ returnBody + ", recvDate=" + recvDate + ", lastSendTime="
		+ lastSendTime + ", sendCount=" + sendCount + "]";
    }

}
