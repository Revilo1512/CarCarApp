package at.carcar.carcarbackend.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class VerifyUserFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Skip /login and /register
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/users/login") || requestURI.equals("/users/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Retrieve userId from session
        HttpSession session = request.getSession(false);  // Don't create a session if it doesn't exist
        if (session != null) {
            Long userId = (Long) session.getAttribute("userId");
            //System.out.println("uAHFhufo");
            //System.out.println(session.getAttribute("userId"));
            //System.out.println(session);
            if (userId != null) {
                // User is authenticated, continue processing the request // could double check ids
                //System.out.println("ARSkol");
                Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);  // Set authentication in the context

                filterChain.doFilter(request, response);
            } else {
                // No userId in session, unauthorized request
                //System.out.println("USERID WRONG");
                //response.getWriter().write("No valid ID in session");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated. Invalid ID");
            }
        } else {
            // No session, unauthorized request
            //System.out.println("novalid sesison");

            //response.getWriter().write("No valid session");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated. No session found");
        }
    }

}
