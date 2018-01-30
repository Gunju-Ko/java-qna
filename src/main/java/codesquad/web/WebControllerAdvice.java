package codesquad.web;

import codesquad.NotFoundException;
import codesquad.UnAuthorizedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(basePackages = "codesquad.web",
                  annotations = Controller.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFoundException(NotFoundException ex) {
        return createView(ex);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleUnAuthorizedException(UnAuthorizedException ex) {
        return createView(ex);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleInternalServerError(RuntimeException ex) {
        return createView(ex);
    }

    private ModelAndView createView(RuntimeException ex) {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("message", ex.getMessage());

        return mv;
    }
}