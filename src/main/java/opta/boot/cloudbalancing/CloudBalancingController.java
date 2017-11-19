package opta.boot.cloudbalancing;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CloudBalancingController {

    @RequestMapping("/cloudbalancing")
    public String cloudbalancing() {
        return "cloudbalancing/loaded";
    }

    @RequestMapping("/cloudbalancing/solve")
    public String solve() {
        return "cloudbalancing/solve";
    }

    @RequestMapping("/cloudbalancing/solving")
    public String solving() {
        return "cloudbalancing/solving";
    }

    @RequestMapping("/cloudbalancing/terminateEarly")
    public String terminateEarly() {
        return "cloudbalancing/terminateEarly";
    }

    @RequestMapping("/cloudbalancing/terminated")
    public String terminated() {
        return "cloudbalancing/terminated";
    }

}