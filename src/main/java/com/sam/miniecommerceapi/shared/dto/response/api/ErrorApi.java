package com.sam.miniecommerceapi.shared.dto.response.api;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorApi extends BaseApi {
  int errorCode;
  String error;
  String path;
  Object data;
}
