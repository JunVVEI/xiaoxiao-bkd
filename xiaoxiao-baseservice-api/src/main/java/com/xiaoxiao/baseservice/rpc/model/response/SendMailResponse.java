package com.xiaoxiao.baseservice.rpc.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * SendMailResponse
 * </p>
 *
 * @author Junwei
 * @since 2023/1/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMailResponse {
	private Boolean isSuccess;

	private String message;
}
