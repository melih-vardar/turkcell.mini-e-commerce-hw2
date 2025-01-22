package com.turkcell.mini_e_commere_hw2.util.exception.result;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BusinessExceptionResult extends ExceptionResult {
  private String errorMessage;

  public BusinessExceptionResult(String errorMessage) {
    super("BusinessException");
    this.errorMessage = errorMessage;
  }
}
