package codesquad.web;

import codesquad.common.exception.CustomException;
import codesquad.common.exception.NotFoundException;
import codesquad.common.exception.PermissionDeniedException;
import codesquad.common.exception.UnAuthenticationException;
import codesquad.common.exception.UnAuthorizedException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(basePackages = "codesquad.web",
                  annotations = Controller.class)
@Order(1)
public class WebControllerAdvice {

    private final MessageSourceAccessor accessor;

    public WebControllerAdvice(MessageSourceAccessor accessor) {
        this.accessor = accessor;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFoundException(NotFoundException ex) {
        return createView(getCustomErrorMessage(ex));
    }

    @ExceptionHandler({PermissionDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handlePermissionDeniedException(PermissionDeniedException e) {
        return createView(getCustomErrorMessage(e));
    }

    @ExceptionHandler(value = {UnAuthorizedException.class,
                               UnAuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView handleUnAuthorizedException(CustomException e) {
        return createView(getCustomErrorMessage(e));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleInternalServerError(RuntimeException ex) {
        return createView(ex.getLocalizedMessage());
    }

    private ModelAndView createView(String errorMessage) {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("message", errorMessage);

        return mv;
    }

    private String getCustomErrorMessage(CustomException e) {
        return accessor.getMessage(e.getMessageCode(), e.getArguments(), e.getMessage());
    }
}