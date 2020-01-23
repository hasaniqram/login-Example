package org.o7planning.securitywebapp.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.o7planning.securitywebapp.bean.UserAccount;
import org.o7planning.securitywebapp.request.UserRoleRequestWrapper;
import org.o7planning.securitywebapp.utils.AppUtils;
import org.o7planning.securitywebapp.utils.SecurityUtils;

@WebFilter("/*")
public class SecurityFilter implements Filter {

	public SecurityFilter() {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		String servletPath = request.getServletPath();

		// login and session
		UserAccount loginedUser = AppUtils.getLoginedUser(request.getSession());

		if (servletPath.equals("/login")) {
			chain.doFilter(request, response);
			return;
		}
		HttpServletRequest wrapRequest = request;

		if (loginedUser != null) {
			// User
			String userName = loginedUser.getUserName();

			// Roles
			List<String> roles = loginedUser.getRoles();

			// old and new request
			wrapRequest = new UserRoleRequestWrapper(userName, roles, request);
		}

		// Pages must be signed in.
		if (SecurityUtils.isSecurityPage(request)) {

			// send to login page to the login page.
			if (loginedUser == null) {

				String requestUri = request.getRequestURI();

				// Store and login.
				int redirectId = AppUtils.storeRedirectAfterLoginUrl(request.getSession(), requestUri);

				response.sendRedirect(wrapRequest.getContextPath() + "/login?redirectId=" + redirectId);
				return;
			}

			// Validation
			boolean hasPermission = SecurityUtils.hasPermission(wrapRequest);
			if (!hasPermission) {

				RequestDispatcher dispatcher //
						= request.getServletContext().getRequestDispatcher("/WEB-INF/views/accessDeniedView.jsp");

				dispatcher.forward(request, response);
				return;
			}
		}

		chain.doFilter(wrapRequest, response);
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {

	}

}