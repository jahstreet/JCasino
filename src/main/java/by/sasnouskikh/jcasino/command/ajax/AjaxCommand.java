package by.sasnouskikh.jcasino.command.ajax;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AjaxCommand {

    Map<String, Object> execute(HttpServletRequest request);
}
