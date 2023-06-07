package com.lgy.spring_mvc_board.command;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.lgy.spring_mvc_board.dao.BDao;
import com.lgy.spring_mvc_board.dto.BDto;

public class BContentCommand implements BCommand {

	@Override
	public void execute(Model model) {
		Map<String, Object> map = model.asMap();
//		request :컨트롤러 단에서 보내주는 이름
		//BController에서request로 담은걸 request로꺼내옴
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		String bid = request.getParameter("bid");
		
//		dao 호출
		BDao dao = new BDao();
//		게시글 하나의 내용을 BDto 객체로 받음(bid(글번호)를 넘겨서 selet 쿼리 처리)
		BDto dto = dao.contentView(bid);
//		dto 객체를 모델에 추가(뷰에서 꺼내 사용할 수 있음)
		model.addAttribute("content_view", dto);
		
	}

}
