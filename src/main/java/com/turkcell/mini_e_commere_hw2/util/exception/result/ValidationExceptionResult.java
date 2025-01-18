package com.turkcell.mini_e_commere_hw2.util.exception.result;

import java.util.List;

public class ValidationExceptionResult extends ExceptionResult{
  private List<String> errors;

  public ValidationExceptionResult(List<String> errors) {
    super("ValidationError");
    this.errors = errors;
  }

  public List<String> getErrors() {
    return errors;
  }

  public void setErrors(List<String> errors) {
    this.errors = errors;
  }
}
