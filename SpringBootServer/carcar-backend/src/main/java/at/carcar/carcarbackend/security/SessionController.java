package at.carcar.carcarbackend.security;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SessionController {

    @GetMapping("/set-session")
    public String setSession(HttpSession session, @RequestParam String value) {
        session.setAttribute("myKey", value);
        return "Session value set";
    }

    @GetMapping("/get-session")
    public String getSession(HttpSession session) {
        return (String) session.getAttribute("myKey");
    }
}
