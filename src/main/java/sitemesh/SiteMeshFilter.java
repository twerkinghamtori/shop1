package sitemesh;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

@WebFilter("/*")
public class SiteMeshFilter extends ConfigurableSiteMeshFilter{
	//doFilter 메서드가 가장 먼저 실행
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) //FilterChain => 다음 요청?
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		String url = request.getRequestURI(); //요청된 URL정보
		if(url.contains("/user/")) url="user";
		else if(url.contains("/admin/")) url="user";
		else if(url.contains("/board/")) url="board";
		else if(url.contains("/item/")) url="item";
		else if(url.contains("/cart/")) url="item";
		else if(url.contains("/chat/")) url="chat";
		else url = "";
		request.setAttribute("url", url);
		super.doFilter(servletRequest, servletResponse, filterChain); //다음 프로세스로 진행
	}
		
	@Override
	protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
		builder.addDecoratorPath("/*", "/layout/mainlayout.jsp")
		.addExcludedPath("/user/idsearch*")
		.addExcludedPath("/user/pwsearch*");
	}
	
	
}
