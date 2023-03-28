package nuc.util;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse)servletResponse;
		res.setHeader("X-Frame-Options", "allow-from https://www.youtube.com/");
		res.setHeader("X-Frame-Options", "allow-from https://youtu.be/");
		res.setHeader("X-Frame-Options", "allow-from https://m.youtube.com/");
		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
