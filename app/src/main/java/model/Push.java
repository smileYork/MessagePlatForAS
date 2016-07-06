package model;

/**
 * * @author chenyao E-mail: 326907403@qq.com
 * 
 * @date 创建时间：2016-2-29 上午9:29:53
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class Push {

	// 一条短信的ID
	private int id;

	// 短信接收号码
	private String comefrom;

	// 短信内容
	private String messageBody;

	// 短信接收到的时间
	private String recvDate;

	// 短信类型
	private int msgType;

	// 短信唯一标示
	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getComefrom() {
		return comefrom;
	}

	public void setComefrom(String comefrom) {
		this.comefrom = comefrom;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public String getRecvDate() {
		return recvDate;
	}

	public void setRecvDate(String recvDate) {
		this.recvDate = recvDate;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public Push(int id, String comefrom, String messageBody, String recvDate, int msgType, String uuid) {
		super();
		this.id = id;
		this.comefrom = comefrom;
		this.messageBody = messageBody;
		this.recvDate = recvDate;
		this.msgType = msgType;
		this.uuid = uuid;
	}

	public Push() {
		super();
	}

	@Override
	public String toString() {
		return "Push [id=" + id + ", comefrom=" + comefrom + ", messageBody=" + messageBody + ", recvDate=" + recvDate
				+ ", msgType=" + msgType + ", uuid=" + uuid + "]";
	}

}
