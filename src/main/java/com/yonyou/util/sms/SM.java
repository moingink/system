package com.yonyou.util.sms;


public class SM {
	public static String sendMassage(String mn,String ct) throws Exception{
		SendMassage s = new SendMassage();
		SmsVo vo = new SmsVo();
		vo.setContent(ct);
		vo.setMobileNumber(mn);
		vo.setName("资金管理系统");
		return s.sendMassage(vo);
	}
	public static void main(String[] args) throws Exception {
		System.out.println(SM.sendMassage("18613879857","给你转款100000000，请注意查收11"));
		System.out.println(SM.sendMassage("18613879857","给你转款100000000，请注意查收21"));
		System.out.println(SM.sendMassage("18613879857,18611073327","给你转款100000000，请注意查收31"));
		System.out.println(SM.sendMassage("18613879857","给你转款100000000，请注意查收41"));
		System.out.println(SM.sendMassage("18613879857","给你转款100000000，请注意查收51"));
	
		
	}
}
	