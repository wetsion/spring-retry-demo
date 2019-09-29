package site.wetsion.study.springretrydemo.imperative;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * @author weixin
 * @version 1.0
 * @CLassName ImperativeService
 * @date 2019/9/29 3:43 PM
 */
@Slf4j
public class ImperativeService {

    public static void main(String[] args) {
        RetryTemplate template = new RetryTemplate();

        TimeoutRetryPolicy timeoutRetryPolicy = new TimeoutRetryPolicy();
        timeoutRetryPolicy.setTimeout(1000L); // 设置超时时间

        template.setRetryPolicy(timeoutRetryPolicy);

        String rt = null;

        try {
            rt = template.execute(new RetryCallback<String, Throwable>() {

                @Override
                public String doWithRetry(RetryContext context) throws Throwable {
                    RestTemplate restTemplate = new RestTemplate();
                    String result = null;
                    try {
                        result = restTemplate.getForObject("http://localhost:9001/health", String.class);
                        log.info("result: [{}]", result);
                    } catch (Exception e) {
                        log.info("exception: [{}]", e.getMessage());
                        throw e;
                    }
                    return result;
                }
            });
        } catch (Throwable throwable) {
//            throwable.printStackTrace();
            log.error("error: [{}]", throwable.getMessage());
        }
        log.info("result: [{}]", rt);
    }
}
